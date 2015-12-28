#version 100

attribute in vec4 vPosition;
uniform in mat4 mMVPMatrix;

void main() {
    gl_Position = mMVPMatrix * vPosition;
}
