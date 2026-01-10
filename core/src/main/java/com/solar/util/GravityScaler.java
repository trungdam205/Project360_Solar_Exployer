package com.solar.util;

public class GravityScaler {

    // Earth = 9.8 → gravity game = 1.0
    public static float scale(float realGravity) {
        return (realGravity / 9.8f);
    }
}
