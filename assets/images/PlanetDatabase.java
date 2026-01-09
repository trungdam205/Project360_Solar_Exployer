package com.solar.data;

import java.util.ArrayList;
import java.util.List;

public class PlanetDatabase {

    private static final List<PlanetData> planets = new ArrayList<>();

    static {
        // Cú pháp cũ: Type, Name, "PATH", Size, CanEnter, X, Y
        // Cú pháp MỚI: Type, Name, "REGION_NAME", Size, CanEnter, X, Y

        // Lưu ý: Tên region lấy chính xác từ file assets.atlas (dòng 10, 56, 68...)

        // Mặt trời (Region name trong atlas là "sun")
        planets.add(new PlanetData(PlanetType.SUN, "Sun", "sun", 1500, false, -900, -20));

        // Sao Thủy (Region name: "mercury")
        planets.add(new PlanetData(PlanetType.MERCURY, "Mercury", "mercury", 150, true, -450, -240));

        // Sao Kim (Region name: "venus")
        planets.add(new PlanetData(PlanetType.VENUS, "Venus", "venus", 230, true, -300, 50));

        // Trái Đất (Region name: "earth")
        planets.add(new PlanetData(PlanetType.EARTH, "Earth", "earth", 235, false, -150, -120));

        // Mặt Trăng (Region name: "moon")
        planets.add(new PlanetData(PlanetType.MOON, "Moon", "moon", 60, true, -40, -45));

        // Sao Hỏa (Region name: "mars")
        planets.add(new PlanetData(PlanetType.MARS, "Mars", "mars", 160, true, 0, -280));

        // Sao Mộc (Region name: "jupiter")
        planets.add(new PlanetData(PlanetType.JUPITER, "Jupiter", "jupiter", 450, true, 220, 195));

        // Sao Thổ (Region name: "saturn")
        planets.add(new PlanetData(PlanetType.SATURN, "Saturn", "saturn", 450, true, 400, -300));

        // Sao Thiên Vương (Region name: "uranus")
        planets.add(new PlanetData(PlanetType.URANUS, "Uranus", "uranus", 380, true, 580, 75));

        // Sao Hải Vương (Region name: "neptune")
        planets.add(new PlanetData(PlanetType.NEPTUNE, "Neptune", "neptune", 350, true, 820, -200));
    }

    public static List<PlanetData> getAllPlanets() { return planets; }

    public static PlanetData get(PlanetType type) {
        for (PlanetData p : planets) {
            if (p.type == type) return p;
        }
        return null;
    }
}
