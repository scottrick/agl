#version 100

attribute vec3 position;
attribute vec2 texture;

uniform mat4 model;
uniform mat4 view;
uniform mat4 proj;

varying vec2 textureCoord;

void main()
{
    gl_Position = proj * view * model * vec4(position, 1.0);
	textureCoord = texture;
}