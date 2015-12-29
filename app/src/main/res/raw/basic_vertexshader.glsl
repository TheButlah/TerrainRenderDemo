#version 100

attribute vec3 vPosition;
uniform mat4 mMVPMatrix;

void main() {
    gl_Position = mMVPMatrix * vec4(vPosition,1);
}
