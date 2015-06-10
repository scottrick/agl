#version 100

attribute vec3 position;
attribute vec2 texture;
attribute vec3 normal;
attribute vec3 tangent;
attribute vec3 bitangent;

varying vec3 outNormal;
varying vec3 outPosition;
varying vec2 outTexture;
varying vec3 outTangent;
varying vec3 outBitangent;

uniform mat4 model;
uniform mat4 view;
uniform mat4 proj;

void main()
{
    gl_Position = proj * view * model * vec4(position, 1.0);

	outTexture = texture;

    vec4 vertPos4 = view * model * vec4(position, 1.0);
	outPosition = vec3(vertPos4) / vertPos4.w;

    outNormal = (view * model * vec4(normal, 0.0)).xyz;
	outTangent = (view * model * vec4(tangent, 0.0)).xyz;
    outBitangent = (view * model * vec4(outBitangent, 0.0)).xyz;
}
