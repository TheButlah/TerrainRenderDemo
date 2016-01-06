#version 100

attribute vec3 vPosition;
attribute vec2 vUVCoords;
uniform mat4 mMVPMatrix;

//varying vec4 vColor;
varying vec2 vUV;
void main() {
    vUV = vUVCoords;
    //vColor = vec4(0.0, clamp(vPosition.y/50.0+0.2, 0.0, 1.0), 0.0, 1.0);
    gl_Position = mMVPMatrix * vec4(vPosition,1);
}
