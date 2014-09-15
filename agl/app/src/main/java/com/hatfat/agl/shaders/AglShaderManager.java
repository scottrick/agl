package com.hatfat.agl.shaders;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by scottrick on 8/8/14.
 */
public class AglShaderManager {

    private HashMap<String, AglShaderProgram> programMap;
    private Context context;

    private static AglShaderManager singleton;

    public static AglShaderManager get() {
        if (singleton == null) {
            singleton = new AglShaderManager();
        }

        return singleton;
    }

    //Need to set a context to use when looking for the shader asset files
    public void setContext(Context context) {
        this.context = context;
    }

    private AglShaderManager() {
        programMap = new HashMap();
    }

    public AglShaderProgram getShaderProgram(String programName) {
        AglShaderProgram program = programMap.get(programName);

        if (program == null) {
            String vertFilename = programName + ".vert";
            String fragFilename = programName + ".frag";

            program = new AglShaderProgram(vertFilename, fragFilename, context);
            programMap.put(programName, program);
        }

        return program;
    }
}
