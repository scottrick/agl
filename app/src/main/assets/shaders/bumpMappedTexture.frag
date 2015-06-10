#version 100

precision mediump float;
precision mediump int;

varying vec3 outNormal;
varying vec3 outPosition;
varying vec2 outTexture;
varying vec3 outTangent;
varying vec3 outBitangent;

uniform vec3 lightDir;
uniform vec4 lightColor;
uniform mat4 model;
uniform mat4 view;

uniform sampler2D textureSampler;
uniform sampler2D normalSampler;
uniform sampler2D specularSampler;

//texturing with normal map and blinn-phong lighting
void main()
{
    vec4 textureColor = texture2D(textureSampler, outTexture);
    vec4 normalColor = texture2D(normalSampler, outTexture);
    vec4 specularColor = texture2D(specularSampler, outTexture);

    vec3 normalToUse = vec3(0.0, 0.0, 0.0);
    normalToUse.x = (normalColor.x * 2.0) - 1.0;
    normalToUse.y = (normalColor.y * 2.0) - 1.0;
    normalToUse.z = (normalColor.z * 2.0) - 1.0;

    mat3 TBN = mat3(outTangent, outBitangent, outNormal);
    normalToUse = TBN * normalToUse;

    //ambient
    float ambient = 0.2;

    //diffuse
    float lambertian = max(dot(lightDir, normalToUse), 0.0);
    float specular = 0.0;

    //specular
    if (lambertian > 0.0) {
        vec3 viewDir = normalize(-outPosition);

        // this is blinn phong specular component
        vec3 halfDir = normalize(lightDir + viewDir);
        float specAngle = max(dot(halfDir, normalToUse), 0.0);
        specular = pow(specAngle, 16.0);
    }

    float colorMultiplier = ambient + lambertian;
    vec4 color = lightColor * textureColor * colorMultiplier + specularColor * specular;
    color = clamp(color, 0.0, 1.0);

    gl_FragColor = color;
}
