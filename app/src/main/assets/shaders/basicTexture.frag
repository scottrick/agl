#version 100

precision mediump float;
precision mediump int;

varying vec2 textureCoord;
varying vec3 fragNormal;

uniform vec3 lightPos;
uniform vec4 lightColor;

uniform sampler2D textureSampler;

//basic texturing
void main()
{
    gl_FragColor = texture2D(textureSampler, textureCoord);
}
