package meshgen;

public class MeshGen {
	public static void main(String[] args) {
		int numLevels = 1;

		if (args.length > 0) {
			numLevels = Integer.parseInt(args[0]);
		}

		System.out.println("MeshGen\n");
		System.out.println(" --> generating " + numLevels + " mesh levels.");

		AglMesh testMesh = AglMesh.makeIcosahedron();

		for (int i = 0; i < numLevels; i++) {
			System.out.println(" --> generating level " + i);

            testMesh = testMesh.splitMesh();
            AglShapeMesh shapeMesh = AglShapeMesh.makeFromMesh(testMesh);

            System.out.println(" -->   Pentagons: " + shapeMesh.getNumPentagons());
            System.out.println(" -->   Hexagons:  " + shapeMesh.getNumHexagons());

            // for (AglShape shape : shapeMesh.getHexagons()) {
            // 	System.out.println(" -->   " + shape.toString());
            // }
		}
	}	
}
