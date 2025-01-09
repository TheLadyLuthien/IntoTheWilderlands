#include veil:fog
#include veil:deferred_utils

uniform sampler2D WorldBuffer;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform vec2 ScreenSize;

// in float vertexDistance;
in vec4 vertexColor;
in vec3 worldNormal;
in vec2 texCoord0;

out vec4 fragColor;

float map(float value, float min1, float max1, float min2, float max2) {
  return min2 + (value - min1) * (max2 - min2) / (max1 - min1);
}

void main() {
    // vec4 color = texture(WorldBuffer, vec2(gl_FragCoord.x / ScreenSize.x, gl_FragCoord.y / ScreenSize.y + 0.1)) * vertexColor * ColorModulator;
    
    vec3 viewDir = viewDirFromUv(texCoord0);

    float dotResult = dot(viewDir, worldNormal);
    float rampPos = map(dotResult, -1, 1, 0, 1);

    // fragColor = vec4(worldNormal, 1);
    fragColor = vec4(1, rampPos, 0, 1);


    // fragColor = vec4(0.3, 0.9, 1, 1.0);
}





