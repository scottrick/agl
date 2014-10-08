#version 100

varying vec3 vertexColor;
varying vec3 fragNormal;

uniform vec3 lightDir;
uniform vec3 lightColor;

void main()
{
    //ambient
    vec4 ambient = vec4(vertexColor * 0.2, 1.0f);

    //diffuse
    vec4 diffuse = vec4(lightColor * vertexColor * dot(fragNormal, lightDir), 1.0f);
    diffuse = clamp(diffuse, 0.0, 1.0);

    gl_FragColor = clamp(ambient + diffuse, 0.0, 1.0);
}
