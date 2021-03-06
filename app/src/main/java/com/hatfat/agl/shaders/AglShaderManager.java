package com.hatfat.agl.shaders;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by scottrick on 8/8/14.
 */
public class AglShaderManager {

    private HashMap<String, AglShaderProgram> programMap;
    private Context context;

    public AglShaderManager(Context context) {
        this.context = context;
        this.programMap = new HashMap<>();
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

    /*
     * Removes all shader programs.
     * Call this when the surfaceView has been paused, so it doesn't need destroy them.
     */
    public void clearPrograms() {
        programMap.clear();
    }
}
