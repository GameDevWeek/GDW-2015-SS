#ifdef GL_ES
#define LOWP lowp
    precision mediump float;
#else
    #define LOWP
#endif

varying vec2 v_texCoords;

uniform sampler2D u_texture;

varying vec2 v_blurTexCoords[14];

void main() {
    gl_FragColor = texture2D(u_texture, v_texCoords);

    if(gl_FragColor.a <= 0.01)
        discard;
}

