attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;
uniform vec3 u_lightPos;

varying vec2 v_texCoords;
varying vec3 v_lightDir;

void main() {
    v_lightDir = normalize(vec3( (u_projTrans * vec4(u_lightPos - a_position.xyz, 0.0)).xy, u_lightPos.z ));

    v_texCoords = a_texCoord0;
    gl_Position = u_projTrans * a_position;
}