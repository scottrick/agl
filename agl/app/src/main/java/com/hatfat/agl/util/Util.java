package com.hatfat.agl.util;

public class Util {

    public final static float NORMALIZATION_ALLOWABLE_ERROR = 0.001f;

    private static float xHalf;
    private static int helperI;

    //Fast approximation of  "1 / sq rt(x)"
    //http://www.beyond3d.com/content/articles/8/
    public static float invSqrt(float x) {
        xHalf = 0.5f * x;
        helperI = Float.floatToIntBits(x);
        helperI = 0x5f3759df - (helperI >> 1);
        x = Float.intBitsToFloat(helperI);
        x = x * (1.5f - xHalf * x * x);

        return x;
    }
}
