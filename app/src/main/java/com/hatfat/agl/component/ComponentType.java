package com.hatfat.agl.component;

import java.util.LinkedList;
import java.util.List;

/**
 * Applications can (and should) extend this class to add their own component types.
 */
public class ComponentType {

    private static final List<Integer> usedTypeIds  = new LinkedList<>();

    public static final ComponentType RENDERABLE    = new ComponentType(1000);
    public static final ComponentType TRANSFORM     = new ComponentType(1001);
    public static final ComponentType MOVEMENT      = new ComponentType(1002);
    public static final ComponentType PHYSICS       = new ComponentType(1003);
    public static final ComponentType LIGHT         = new ComponentType(1004);
    public static final ComponentType CAMERA        = new ComponentType(1005);
    public static final ComponentType MODIFIER      = new ComponentType(1006);

    /* fast access since this is accessed a lot */
    public final int typeId;

    protected ComponentType(int typeId) {
        this.typeId = typeId;

        if (usedTypeIds.contains(typeId)) {
            throw new RuntimeException("TypeId's can not be used by more than one ComponentType.");
        }

        usedTypeIds.add(typeId);
    }
}
