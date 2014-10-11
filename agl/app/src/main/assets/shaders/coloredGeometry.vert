#version 100

attribute vec3 position;
attribute vec4 color;
attribute vec3 normal;

varying vec4 vertexColor;
varying vec3 fragNormal;

uniform mat4 proj;
uniform mat4 view;
uniform mat4 model;

void main()
{
    gl_Position = proj * view * model * vec4(position, 1.0);
	vertexColor = color;
	vec4 temp = model * vec4(normal, 0.0);
	fragNormal = temp.xyz;
}