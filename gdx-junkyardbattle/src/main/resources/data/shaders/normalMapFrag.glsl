#ifdef GL_ES
#define LOWP lowp
    precision mediump float;
#else
    #define LOWP
#endif

varying vec2 v_texCoords;
varying vec3 v_lightDir;

uniform sampler2D u_texture;
uniform sampler2D u_normals;

void main() {
    vec4 color = texture2D(u_texture, v_texCoords);
    
    vec4 finalColor = vec4(0.0, 0.0, 0.0, 0.0);

    float lambert = 1.0;

    vec3 nColor = texture2D(u_normals, v_texCoords).rgb;
    
    // uncompress to [-1, 1]
    vec3 normal = normalize(nColor * 2.0 - 1.0);
    lambert = clamp(dot(normal, v_lightDir), 0.0, 1.0);

    vec3 lColor = vec3(1.0, 1.0, 1.0);
    
    
    vec3 result = clamp(lambert * lColor, 0.0, 1.0);
    result *= color.rgb;
    
    color = vec4(result, color.a);
    
    gl_FragColor = color;
}