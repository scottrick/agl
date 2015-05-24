#version 100

precision mediump float;
precision mediump int;

varying vec4 vertexColor;
varying vec3 fragNormal;

varying vec3 vertPos;

uniform vec3 lightDir;
uniform vec4 lightColor;

//blinn-phong lighting
void main()
{
    //ambient
    float ambient = 0.3;

    //diffuse
    float diffuse = dot(fragNormal, lightDir);
    diffuse = clamp(diffuse, 0.0, 1.0);

    float lambertian = max(dot(lightDir, fragNormal), 0.0);
    float specular = 0.0;

    //specular
    if(lambertian > 0.0) {
        vec3 viewDir = normalize(-vertPos);

        // this is blinn phong specular component
        vec3 halfDir = normalize(lightDir + viewDir);
        float specAngle = max(dot(halfDir, fragNormal), 0.0);
        specular = pow(specAngle, 12.0);
    }

    float colorMultiplier = ambient + lambertian * diffuse + specular;
    vec4 color = lightColor * vertexColor * colorMultiplier;
    color = clamp(color, 0.0, 1.0);

    gl_FragColor = color;
}
