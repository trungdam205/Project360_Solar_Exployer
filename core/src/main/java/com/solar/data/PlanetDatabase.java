package com.solar.data;

import java.util.ArrayList;
import java.util.List;

public class PlanetDatabase {

    private static final List<PlanetData> planets = new ArrayList<>();

    static {
        // Cú pháp: Type, Name, Path, Size, CanEnter, X, Y

        // Mặt trời nằm chính giữa bên trái (hoặc lùi vào trong một chút)
        planets.add(new PlanetData(PlanetType.SUN, "Sun", "planets/sun.png", 1500, false, -900, -20));

        // Sao Thủy nằm thấp xuống xíu
        planets.add(new PlanetData(PlanetType.MERCURY, "Mercury", "planets/mercury.png", 150, true, -450, -240));

        // Sao Kim nằm cao lên xíu
        planets.add(new PlanetData(PlanetType.VENUS, "Venus", "planets/venus.png", 230, true, -300, 50));

        // Trái Đất
        planets.add(new PlanetData(PlanetType.EARTH, "Earth", "planets/earth.png", 235, false, -150, -120));

        // Mặt Trăng (Cạnh Trái Đất: X = 650 + lệch, Y = 500 + lệch)
        planets.add(new PlanetData(PlanetType.MOON, "Moon", "planets/moon.png", 60, true, -40, -45));

        // Sao Hỏa
        planets.add(new PlanetData(PlanetType.MARS, "Mars", "planets/mars.png", 160, true, 0, -280));

        // ... Các hành tinh còn lại bạn tự điền số nhé ...
        planets.add(new PlanetData(PlanetType.JUPITER, "Jupiter", "planets/jupiter.png", 450, true, 220, 195));
        planets.add(new PlanetData(PlanetType.SATURN, "Saturn", "planets/saturn.png", 450, true, 400, -300));
        planets.add(new PlanetData(PlanetType.URANUS, "Uranus", "planets/uranus.png", 380, true, 580, 75));
        planets.add(new PlanetData(PlanetType.NEPTUNE, "Neptune", "planets/neptune.png", 350, true, 820, -200));
    }

    public static List<PlanetData> getAllPlanets() { return planets; }

    public static PlanetData get(PlanetType type) {
        for (PlanetData p : planets) {
            if (p.type == type) return p;
        }
        return null;
    }
}
