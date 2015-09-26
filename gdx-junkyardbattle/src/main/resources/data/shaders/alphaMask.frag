#ifdef GL_ES
#define LOWP lowp
    precision mediump float;
#else
    #define LOWP
#endif

varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform sampler2D u_alphaMask;
uniform float u_alphaModifier;

void main() {
    gl_FragColor = texture2D(u_texture, v_texCoords);
    float alpha = texture2D(u_alphaMask, v_texCoords).a;
    gl_FragColor.a -= alpha * u_alphaModifier;
    
    if(gl_FragColor.a <= 0.01)
        discard;
}

