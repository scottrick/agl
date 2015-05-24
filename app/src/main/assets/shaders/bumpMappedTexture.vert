#version 100

attribute vec3 position;
attribute vec2 texture;
attribute vec3 normal;
attribute vec3 tangent;
attribute vec3 biTangent;

varying vec3 vertPos;
varying vec2 textureCoord;

uniform mat4 model;
uniform mat4 view;
uniform mat4 proj;

void main()
{
    gl_Position = proj * view * model * vec4(position, 1.0);
	textureCoord = texture;
    fragNormal = (model * vec4(normal, 0.0)).xyz;

    vec4 vertPos4 = model * vec4(position, 1.0);
	vertPos = vec3(vertPos4) / vertPos4.w;
}
