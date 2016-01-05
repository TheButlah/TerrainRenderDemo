#version 100

attribute vec3 vPosition;
uniform mat4 mMVPMatrix;

varying vec4 vColor;

void main() {
    vColor = vec4(0.0, clamp(vPosition.y/50.0+0.2, 0.0, 1.0), 0.0, 1.0);
    gl_Position = mMVPMatrix * vec4(vPosition,1);
}
