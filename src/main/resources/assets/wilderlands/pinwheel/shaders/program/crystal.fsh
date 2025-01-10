#include veil:fog
#include veil:deferred_utils

uniform sampler2D ColorTexture;
uniform sampler2D NormalMap;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform vec2 ScreenSize;

in float vertexDistance;
// in vec4 vertexColor;
in vec3 worldNormal;
in vec2 texCoord0;

out vec4 fragColor;

float map(float value,float min1,float max1,float min2,float max2){
    return min2+(value-min1)*(max2-min2)/(max1-min1);
}

float fresnel(float amount, vec3 normal, vec3 view){
	return pow(
		clamp(dot(normalize(normal), normalize(view)), 0.0, 1.0),
		amount
	);
}

vec2 random2( vec2 p ) {
    return fract(sin(vec2(dot(p,vec2(127.1,311.7)),dot(p,vec2(269.5,183.3))))*43758.5453);
}

float voronoi(float size, float time, vec2 uv, vec2 dotVec)
{
    vec2 st = uv.xy/size;

    // Tile the space
    vec2 i_st = floor(st);
    vec2 f_st = fract(st);

    float m_dist = 10.0;  // minimum distance
    vec2 m_point;        // minimum point

    for (int j=-1; j<=1; j++ ) {
        for (int i=-1; i<=1; i++ ) {
            vec2 neighbor = vec2(float(i),float(j));
            vec2 point = random2(i_st + neighbor);
            point = 0.5 + 0.5*sin(time + 6.2831*point);
            vec2 diff = neighbor + point - f_st;
            float dist = length(diff);

            if( dist < m_dist ) {
                m_dist = dist;
                m_point = point;
            }
        }
    }

    // Assign a color using the closest point position
    return dot(m_point, dotVec);
}

void main(){
    // Precalculations
    vec3 viewDir=viewDirFromUv(texCoord0);

    // Definitions
    vec4 baseColor = vec4(0.5294, 0.3765, 0.7569, 1.0);
    vec4 emissionColor = vec4(0.9176, 0.3765, 1.0, 1.0);
    float alpha = 0.85;
    float emissionPower = 2;
    float fresnelPower = 3.5;
    
    // Calculations
    
    // float fresnelResult = fresnel(fresnelPower, worldNormal, viewDir);
    
    vec4 mapNormal=texture(NormalMap,texCoord0);
    vec3 fullNormal=mapNormal.xyz+worldNormal;
    
    
    float dotResult=dot(viewDir, fullNormal);
    float rampPos=map(dotResult,-1,1,0,1);
    
    vec4 textureColor=texture(ColorTexture,vec2(rampPos,0));

    float vor = voronoi(0.2, vertexDistance / 3, texCoord0, vec2(.3,.6));
    float val = map(vor, 0, 1, 0.4, 1.5);


    float glowVor = floor(voronoi(0.1, rampPos * 3, texCoord0, vec2(1, 1)) - 0.9);

    // Output
    vec4 color = textureColor * val;
    color *= baseColor;
    // color *= emissionPower;

    // color += (emissionColor * glowVor);

    // color = (fresnelResult * emissionColor);


    color.a=alpha;

    // fragColor = vec4(vor, vor, vor, 1);
    fragColor=color;
}

