#version 100

varying vec4 vertexColor;
varying vec3 fragNormal;

uniform vec3 lightDir;
uniform vec4 lightColor;

void main()
{
    //ambient
    vec4 ambient = vertexColor * 0.2;

    //diffuse
    vec4 diffuse = lightColor * vertexColor * dot(fragNormal, lightDir);
    diffuse = clamp(diffuse, 0.0, 1.0);

    gl_FragColor = clamp(ambient + diffuse, 0.0, 1.0);
}
