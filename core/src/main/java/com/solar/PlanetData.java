package com.solar;

public class PlanetData {
    public String id;
    public String name;
    public float x;
    public float y;
    public float gravity;
    public float radius;
    public String texturePath; // Đường dẫn ảnh trong JSON
    public String description;

    // Bắt buộc phải có Constructor rỗng để LibGDX Json hoạt động
    public PlanetData() {
    }
}
