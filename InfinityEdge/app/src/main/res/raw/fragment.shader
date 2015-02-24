precision mediump float;       	// Set the default precision to medium. We don't need as high of a 
								// precision in the fragment shader.
uniform vec3 u_LightPos;       	// The position of the light in eye space.
uniform sampler2D u_Texture;    // The input texture.
uniform vec4 u_AmbientColor;
uniform vec4 u_DiffuseColor;
uniform bool u_LightEnabled;
uniform float u_Far;

varying vec3 v_Position;		// Interpolated position for this fragment.
varying vec3 v_Normal;         	// Interpolated normal for this fragment.
varying vec2 v_TexCoordinate;   // Interpolated texture coordinate per fragment.

// The entry point for our fragment shader.
void main()                    		
{
    float distance = length(v_Position);


	// Will be used for attenuation.
    float light_distance = length(u_LightPos - v_Position);
	
	// Get a lighting direction vector from the light to the vertex.
    vec3 lightVector = normalize(u_LightPos - v_Position);              	

	// Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
	// pointing in the same direction then it will get max illumination.
    float diffuse = 1.0;

    if(u_LightEnabled)
        diffuse = max(dot(v_Normal, lightVector), 0.0);

	// Add attenuation. 
   // diffuse = diffuse * (1.0 / light_distance);
    
    // Add ambient lighting
    diffuse = diffuse + 0.2;  

	// Multiply the color by the diffuse illumination level and texture value to get final output color.
    gl_FragColor = texture2D(u_Texture, v_TexCoordinate);
    gl_FragColor.xyz *= diffuse;
    gl_FragColor *= u_DiffuseColor;
    gl_FragColor.xyz += u_AmbientColor.xyz;

    float outside = u_Far - distance;
    if(outside < 64.0){
        gl_FragColor.w *= outside/64.0;
    }

  }

