#version 100

precision mediump float;
precision mediump int;

varying vec2 textureCoord;
varying vec3 fragNormal;
varying vec3 vertPos;

uniform vec3 lightDir;
uniform vec4 lightColor;

uniform sampler2D textureSampler;

//basic texturing with blinn-phong lighting
void main()
{
    vec4 textureColor = texture2D(textureSampler, textureCoord);

    //ambient
    float ambient = 0.2;

    //diffuse
    float lambertian = max(dot(lightDir, fragNormal), 0.0);
    float specular = 0.0;

    //specular
    if (lambertian > 0.0) {
        vec3 viewDir = normalize(-vertPos);

        // this is blinn phong specular component
        vec3 halfDir = normalize(lightDir + viewDir);
        float specAngle = max(dot(halfDir, fragNormal), 0.0);
        specular = pow(specAngle, 16.0);
    }

    float colorMultiplier = ambient + lambertian + specular;
    vec4 color = lightColor * textureColor * colorMultiplier;
    color = clamp(color, 0.0, 1.0);

    gl_FragColor = color;
}
