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

void main(){
    // Precalculations
    vec3 viewDir=viewDirFromUv(texCoord0);

    // Definitions
    vec4 baseColor = vec4(0.5294, 0.3765, 0.7569, 1.0);
    vec4 emissionColor = vec4(0.3686, 0.898, 0.9255, 1.0);
    float alpha = 0.75;
    float emissionPower = 2;
    float fresnelPower = 3.5;
    float size = 0.3;
    

    // Calculations
    
    // float fresnelResult = fresnel(fresnelPower, worldNormal, viewDir);
    
    // vec4 mapNormal=texture(NormalMap,texCoord0);
    // vec3 fullNormal=mapNormal.xyz+worldNormal;
    
    
    // float dotResult=dot(viewDir, fullNormal);
    // float rampPos=map(dotResult,-1,1,0,1);
    
    // vec4 color=texture(ColorTexture,vec2(rampPos,0));

    float u_time = vertexDistance / 10;

    vec2 st = texCoord0.xy/size;

    // Tile the space
    vec2 i_st = floor(st);
    vec2 f_st = fract(st);

    float m_dist = 10.;  // minimum distance
    vec2 m_point;        // minimum point

    for (int j=-1; j<=1; j++ ) {
        for (int i=-1; i<=1; i++ ) {
            vec2 neighbor = vec2(float(i),float(j));
            vec2 point = random2(i_st + neighbor);
            point = 0.5 + 0.5*sin(u_time + 6.2831*point);
            vec2 diff = neighbor + point - f_st;
            float dist = length(diff);

            if( dist < m_dist ) {
                m_dist = dist;
                m_point = point;
            }
        }
    }

    // Assign a color using the closest point position
    float val = dot(m_point, vec2(.3,.6));
    vec4 color = vec4(val, val, val, 1);

    // Output
    // color *= baseColor;
    // color *= emissionPower;

    // color = (fresnelResult * emissionColor);


    // color.a=alpha;

    // fragColor = vec4(squDist, 0, 0, 1);
    fragColor=color;
}

