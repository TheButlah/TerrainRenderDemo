#version 100

attribute vec4 vPosition;
uniform mat4 mMVPMatrix;

void main() {
    gl_Position = mMVPMatrix * vPosition;
}
