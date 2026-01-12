package com.solar.data;

public class ObstacleData {

    public ObstacleType type;
    public String texturePath;

    public float x;
    public float y;
    public float width;
    public float height;

    public boolean block;
    public boolean kill;

    public ObstacleData(
        ObstacleType type,
        String texturePath,
        float x,
        float y,
        float width,
        float height,
        boolean block,
        boolean kill
    ) {
        this.type = type;
        this.texturePath = texturePath;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.block = block;
        this.kill = kill;
    }
}
