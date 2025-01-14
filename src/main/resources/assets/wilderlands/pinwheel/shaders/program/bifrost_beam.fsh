#include veil:fog
#include veil:deferred_utils
#include wilderlands:util

// uniform sampler2D ColorTexture;
uniform sampler2D BifrostGradient;
uniform sampler2D BifrostRainbow;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform float GameTime;
uniform vec4 FogColor;
uniform vec2 ScreenSize;

in float vertexDistance;
// in vec3 pos;
in vec3 worldNormal;
in vec2 texCoord0;

in float beamFullness;

out vec4 fragColor;

vec4 calcEdgeGlow()
{
    vec2 screenspaceUv = gl_FragCoord.xy / ScreenSize;
    vec3 viewDir = viewDirFromUv(screenspaceUv);

    float t = GameTime * 10;
    // vec2 v = vec2(sin(t)/3.0 + 0.4, cos(t)/3.0 + 0.4);
    float vor = fbm(texCoord0 / 0.1) + 0.2;

    vec4 glowColor = texture(BifrostRainbow, vec2(-1 * texCoord0.y, 1));

    float edgeGlow = pow(1.0 - dot(worldNormal * -1.0, viewDir), 1.4);
    glowColor.a = edgeGlow * 0.5;

    return glowColor;
}

float calcNoisePattern(vec2 uv, float speed)
{
    float scale = 0.1;

    vec2 st = uv.xy / scale;
    st.x /= 0.9;

    st.y /= 0.2;

    st.y += (GameTime * speed);

    float fractalNoiseResult = fbm(st);

    // float samplePoint = floor(fractalNoiseResult * 7) / 7;
    // vec4 gradientSample = texture(BifrostGradient, vec2(fractalNoiseResult, 0));

    // // gradientSample.a = fractalNoiseResult + 0.2;
    // gradientSample.a = 0.85;


    return fractalNoiseResult;
    // return vec4(fractalNoiseResult, 0, 0, 1);
}

mat2 rotate2d(float alpha){
	return mat2(
		cos(alpha), -sin(alpha),
		sin(alpha), cos(alpha)
	);
}
vec2 warp (vec2 p) {
	float t = 0.0003;
	float r = length(p);
	float alpha = t * r;
	return rotate2d(alpha) * p;
}

vec4 calcColor(vec2 uv)
{
    uv = warp(uv);
    float val = voronoiRound(0.1, 345, uv * vec2(1, 3));
    val = clamp(val, 0, 1);
    vec4 gradientSample = texture(BifrostGradient, vec2(val, 0));

    return gradientSample;
}

float cube(float x)
{
    return x * (x * ((4 * x) - (6 * x) + 3));
}

vec4 calcLayer(vec2 uv, float globalSpeed)
{
    vec4 color = vec4(0, 0, 0, 0);

    float fractal = clamp(pow(calcNoisePattern(texCoord0, 10000 * globalSpeed), 3), 0, 1);

    
    color = calcColor(texCoord0 + vec2(0, GameTime * 500 * globalSpeed));
    color.a = fractal;

    return color;
}

vec4 blendOver(vec4 a, vec4 b) {
    float newAlpha = mix(b.w, 1.0, a.w);
    vec3 newColor = mix(b.w * b.xyz, a.xyz, a.w);
    float divideFactor = (newAlpha > 0.001 ? (1.0 / newAlpha) : 1.0);
    return vec4(divideFactor * newColor, newAlpha);
}

void main(){
    float globalSpeed = 0.9;


    // color.a += calcNoisePattern(texCoord0 + vec2(13.5448, 984.432), 8450);
    // color.a += calcNoisePattern(texCoord0 + vec2(3.4648, 23.849), 5624);

    // color = vec4(fractal, fractal, fractal, 1);

    vec4 color = calcLayer(texCoord0, 1 * globalSpeed);

    vec4 layer = calcLayer(texCoord0 + vec2(482.39, 31.3), 0.9083 * globalSpeed);
    color = blendOver(color, layer);

    layer = calcLayer(texCoord0 + vec2(-333.39, 34382.3), 1.0843 * globalSpeed);
    color = blendOver(color, layer);

    layer = calcLayer(texCoord0 + vec2(382913.39, 33) * vec2(-1, 1), 1.0123 * globalSpeed);
    color = blendOver(color, layer);

    // layer = calcLayer(texCoord0 + vec2(-83348.39,-3904), 1.0022 * globalSpeed);
    // color = blendOver(color, layer);

    // layer = calcLayer(texCoord0 + vec2(-28.39,-204.34), 0.93394 * globalSpeed);
    // color = blendOver(color, layer);


    layer = calcEdgeGlow();
    color = blendOver(color, layer);

    float beamFadeDist = 0.06;

    if (texCoord0.y < (1.0 - beamFullness))
    {
        float val = map(texCoord0.y, (1.0 - beamFullness) - beamFadeDist, (1.0 - beamFullness), 0, 1);
        color.a *= clamp(val, 0, 1);
    }

    // color = vec4(beamFullness, beamFullness, beamFullness, 1);
    

    /* 
        1 ------------

        ...


      > 0.2 ----------

        0.1 ----------
    
        0 ------------
    
     */



    // color = calcEdgeGlow();

    fragColor = color;
}

