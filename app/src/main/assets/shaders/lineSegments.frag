#version 100

precision mediump float;
precision mediump int;

varying vec4 vertexColor;

void main()
{
    gl_FragColor = vertexColor;
}
