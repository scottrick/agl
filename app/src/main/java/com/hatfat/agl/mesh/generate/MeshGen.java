package com.hatfat.agl.mesh.generate;

import com.hatfat.agl.mesh.AglBBMesh;
import com.hatfat.agl.mesh.AglMesh;

import java.io.File;

public class MeshGen {

    public static void main(String[] args) {
        int numLevels = 8;

        if (args.length > 0) {
            numLevels = Integer.parseInt(args[0]);
        }

        System.out.println("MeshGen\n");
        System.out.println(" --> generating " + numLevels + " mesh levels.");

        AglMesh testMesh = AglMesh.makeIcosahedron();

        File rawDirectory = new File("app/src/main/res/raw");
        rawDirectory.mkdir();

        for (int i = 1; i <= numLevels; i++) {
            testMesh = testMesh.splitMesh();
            AglBBMesh shapeMesh = AglBBMesh.makeFromMesh(testMesh).createNewMeshWithNoVertexSharing();

            String filename = String.format("app/src/main/res/raw/mesh%d.bb", i);

            try {
                System.out.println(" --> Writing " + filename +".");
                shapeMesh.writeToDiskAsBytes(filename);
                System.out.println("       Finished.");
            }
            catch (Exception e) {
                System.out.println(" --> Failed writing " + filename +"!");
            }
        }
    }
}