#include veil:fog
#include veil:deferred_utils
#include wilderlands:util

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


    // float glowVor = floor(voronoi(0.1, rampPos * 3, texCoord0, vec2(1, 1)) - 0.9);

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

