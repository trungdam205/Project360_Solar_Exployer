package com.solar.data;

public class PlanetData {

    public PlanetType type;
    public String displayName;
    public String texturePath;
    public float size;
    public boolean canEnter;
    // Vị trí lưu tạm cho Moon
    public float positionX;
    public float x;
    public float y;

    public double gravity;       // Trọng lực
    public String weather;  // Nhiệt độ
    public String atmosphere;   // Khí quyển
    public String surfaceType;  // Bề mặt
    public String primaryRes;   // Tài nguyên


    public PlanetData(PlanetType type, String displayName, String texturePath, float size, boolean canEnter, float x, float y,
                      double gravity, String weather, String atmosphere, String surfaceType, String primaryRes) {
        this.type = type;
        this.displayName = displayName;
        this.texturePath = texturePath;
        this.size = size;
        this.canEnter = canEnter;
        this.positionX = 0;
        this.x = x;
        this.y = y;

        this.gravity = gravity;
        this.weather = weather;
        this.atmosphere = atmosphere;
        this.surfaceType = surfaceType;
        this.primaryRes = primaryRes;

    }
}
