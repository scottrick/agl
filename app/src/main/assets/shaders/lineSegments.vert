#version 100

attribute vec3 position;
attribute vec4 color;

varying vec4 vertexColor;

uniform mat4 proj;
uniform mat4 view;
uniform mat4 model;

void main()
{
    gl_Position = proj * view * model * vec4(position, 1.0);
    vertexColor = color;
}
