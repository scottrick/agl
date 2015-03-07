package com.hatfat.agl.mesh;

import com.hatfat.agl.util.Vec3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

//a point object used when generating the hex meshes
public class AglPoint implements Comparable<AglPoint> {

    public final Vec3 p;

    public AglPoint(Vec3 p) {
        this.p = p;
    }

    public AglPoint(AglPoint point) {
        this.p = new Vec3(point.p);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AglPoint)) {
            return false;
        }

        if (o == this) {
            return true;
        }

        AglPoint point = (AglPoint) o;

        return (
                this.p.x == point.p.x &&
                this.p.y == point.p.y &&
                this.p.z == point.p.z);
    }

    @Override
    public int hashCode() {
        return Float.valueOf(p.x).hashCode() + Float.valueOf(p.y).hashCode() + Float.valueOf(p.z).hashCode();
    }

    @Override public int compareTo(AglPoint another) {
        if (this == another) {
            return 0;
        }

        if (this.p.x != another.p.x) {
            return Float.compare(this.p.x, another.p.x);
        }

        if (this.p.y != another.p.y) {
            return Float.compare(this.p.y, another.p.y);
        }

        if (this.p.z != another.p.z) {
            return Float.compare(this.p.z, another.p.z);
        }

        return 0;
    }

    public void writeToDataStream(DataOutputStream out) throws IOException {
        out.writeFloat(p.x);
        out.writeFloat(p.y);
        out.writeFloat(p.z);
    }

    public static AglPoint readPointFromStream(DataInputStream in) throws IOException {
        float x = in.readFloat();
        float y = in.readFloat();
        float z = in.readFloat();

        return new AglPoint(new Vec3(x, y, z));
    }
}
