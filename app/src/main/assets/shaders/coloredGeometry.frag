#version 100

precision mediump float;
precision mediump int;

varying vec4 vertexColor;
varying vec3 fragNormal;

varying vec3 vertPos;

uniform vec3 lightPos;
uniform vec4 lightColor;

const float screenGamma = 2.2; // Assume the monitor is calibrated to the sRGB color space

//blinn-phong lighting
void main()
{
    vec3 normal = normalize(fragNormal);
    vec3 lightDir = normalize(lightPos - vertPos);

    //ambient
    float ambient = 0.2;

    //diffuse
    float lambertian = max(dot(lightDir, normal), 0.0);
    float specular = 0.0;

    //specular
    if (lambertian > 0.0) {
        vec3 viewDir = normalize(-vertPos);

        // this is blinn phong specular component
        vec3 halfDir = normalize(lightDir + viewDir);
        float specAngle = max(dot(halfDir, normal), 0.0);
        specular = pow(specAngle, 16.0);
    }

    float colorMultiplier = ambient + lambertian + specular;
    vec4 colorLinear = lightColor * vertexColor * colorMultiplier;

    vec4 colorGammaCorrected = pow(colorLinear, vec4(1.0 / screenGamma));

    gl_FragColor = colorGammaCorrected;
}
