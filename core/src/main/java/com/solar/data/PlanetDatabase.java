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
        planets.add(
            new PlanetData(
                PlanetType.SUN,
                "Sun",
                "sun",
                "planets/sun1.png",
                1500,
                false,
                -900,
                -20,
                0.3f,
                27.9f,
                "Extrememe Heat",
                "Hydrogen/Helium",
                "Plasma",
                "Solar Energy",
                null
            )
        );

        // Sao Thủy (Region name: "mercury")
        planets.add(
            new PlanetData(
                PlanetType.MERCURY,
                "Mercury",
                "mercury",
                "planets/MercuryMapDemo.png",
                150,
                true,
                -450,
                -240,
                0.16f,
                3.70f,
                "Extreme Heat",
                "None",
                "Craters",
                "Rare Metals",
                List.of(
                    new ObstacleData(
                        ObstacleType.ROCK,
                        "Obstacle/MercuryObstacle1.png",
                        600,
                        40,
                        800,
                        1050,
                        true,
                        false
                    )
                )
            )
        );

        // Sao Kim (Region name: "venus")
        planets.add(
            new PlanetData(
                PlanetType.VENUS,
                "Venus",
                "venus",
                "planets/Venus.png",
                230,
                true,
                -300,
                50,
                0.15f,
                8.87f,
                "Extreme Heat",
                "CO2 (Toxic)",
                "Volcanic",
                "Sulfur",
                List.of(
                    new ObstacleData(
                        ObstacleType.ROCK,
                        "Obstacle/VenusObstacle1.png",
                        60,
                        60,
                        200,
                        100,
                        true,
                        false
                    )
                )
            )
        );

        // Trái Đất (Region name: "earth")
        planets.add(
            new PlanetData(
                PlanetType.EARTH,
                "Earth",
                "earth",
                "planets/earth1.png",
                235,
                false,
                -150,
                -120,
                0.0f,
                9.81f,
                "Habitable",
                "Nitrogen/Oxygen",
                "Water/Land",
                "Organic Life",
                null
            )
        );

        // Mặt Trăng (Region name: "moon")
        planets.add(
            new PlanetData(
                PlanetType.MOON,
                "Moon",
                "moon",
                "planets/MoonMapDemo.png",
                60,
                true,
                -40,
                -45,
                0.19f,
                1.62f,
                "Extreme Thermal Contrast",
                "None",
                "Dust",
                "Helium-3",
                List.of(
                    new ObstacleData(
                        ObstacleType.ROCK,
                        "Obstacle/MoonObstacle4.png",
                        30,
                        30,
                        80,
                        80,
                        true,
                        false
                    )
                )
            )
        );

        // Sao Hỏa (Region name: "mars")
        planets.add(
            new PlanetData(
                PlanetType.MARS,
                "Mars",
                "mars",
                "planets/MarsMapDemo.png",
                160,
                true,
                0,
                -280,
                0.19f,
                3.71f,
                "Cold",
                "CO2",
                "Red Dust",
                "Iron Oxide",
                List.of(
                    new ObstacleData(
                        ObstacleType.ROCK,
                        "Obstacle/MarsObstacle1.png",
                        40,
                        40,
                        120,
                        200,
                        true,
                        false
                    )
                )
            )
        );

        // Sao Mộc (Region name: "jupiter")
        planets.add(
            new PlanetData(
                PlanetType.JUPITER,
                "Jupiter",
                "jupiter",
                "planets/jupiter.png",
                450,
                true,
                220,
                195,
                0.19f,
                24.79f,
                "Cold",
                "H2/He",
                "Gas Giant",
                "Hydrogen",
                List.of(
                    new ObstacleData(
                        ObstacleType.ROCK,
                        "Obstacle/JupiterObstacle2.png",
                        50,
                        50,
                        100,
                        100,
                        true,
                        false
                    ),
                    new ObstacleData(
                        ObstacleType.SPINE,
                        "Obstacle/JupiterObstacle1.png",
                        70,
                        70,
                        300,
                        150,
                        false,
                        true
                    )
                )
            )
        );

        // Sao Thổ (Region name: "saturn")
        planets.add(
            new PlanetData(
                PlanetType.SATURN,
                "Saturn",
                "saturn",
                "planets/saturn.png",
                450,
                true,
                400,
                -300,
                0.19f,
                10.44f,
                "Cold",
                "H2/He",
                "Gas/Rings",
                "Ice Crystals",
                List.of(
                    new ObstacleData(
                        ObstacleType.ROCK,
                        "Obstacle/SaturnObstacle1.png",
                        60,
                        60,
                        150,
                        150,
                        true,
                        false
                    ),
                    new ObstacleData(
                        ObstacleType.SPINE,
                        "Obstacle/SaturnObstacle2.png",
                        80,
                        80,
                        400,
                        200,
                        false,
                        true
                    )
                )
            )
        );

        // Sao Thiên Vương (Region name: "uranus")
        planets.add(
            new PlanetData(
                PlanetType.URANUS,
                "Uranus",
                "uranus",
                "planets/uranus.png",
                380,
                true,
                580,
                75,
                0.19f,
                8.69f,
                "Frozen",
                "H2/He/Methane",
                "Ice Giant",
                "Water Ice",
                List.of(
                    new ObstacleData(
                        ObstacleType.ROCK,
                        "Obstacle/UranusObstacle1.png",
                        50,
                        50,
                        120,
                        180,
                        true,
                        false
                    )
                )
            )
        );

        // Sao Hải Vương (Region name: "neptune")
        planets.add(
            new PlanetData(
                PlanetType.NEPTUNE,
                "Neptune",
                "neptune",
                "planets/neptune.png",
                350,
                true,
                820,
                -200,
                0.21f,
                11.15f,
                "Frozen",
                "H2/He/Methane",
                "Ice Giant",
                "Diamond Rain",
                List.of(
                    new ObstacleData(
                        ObstacleType.ROCK,
                        "Obstacle/NeptuneObstacle1.png",
                        60,
                        60,
                        150,
                        200,
                        true,
                        false
                    ), new ObstacleData(
                        ObstacleType.SPINE,
                        "Obstacle/NeptuneObstacle2.png",
                        80,
                        80,
                        350,
                        150,
                        false,
                        true
                    )
                )
            )
        );
    }

    public static List<PlanetData> getAllPlanets() {
        return planets;
    }

    public static PlanetData get(PlanetType type) {
        for (PlanetData p : planets) {
            if (p.type == type) {
                return p;
            }
        }
        return null;
    }
}
