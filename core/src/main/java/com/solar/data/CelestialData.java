package com.solar.data;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

public class CelestialData {
    public String id;
    public String name;
    public String type;
    public String diameter;
    public String mass;
    public String temperature;
    public String distance;
    public String orbitalPeriod;
    public String orbitalSpeed;
    public String[] facts;
    public boolean explorable;
    public ExplorationData exploration;

    // Visual properties
    public float baseSize;
    public float orbitRadius;
    public float orbitSpeed;
    public float orbitPhase;
    public Color innerColor;
    public Color outerColor;

    // Special features
    public boolean hasRings;
    public boolean hasGreatSpot;
    public boolean hasPolarCap;
    public boolean hasCraters;
    public boolean hasContinents;
    public boolean hasHeartRegion;

    public static class ExplorationData {
        public Color skyColorTop;
        public Color skyColorBottom;
        public Color terrainColor;
        public float gravity;
        public Array<Landmark> landmarks;

        public ExplorationData(Color skyTop, Color skyBottom, Color terrain, float gravity) {
            this.skyColorTop = skyTop;
            this. skyColorBottom = skyBottom;
            this.terrainColor = terrain;
            this.gravity = gravity;
            this.landmarks = new Array<>();
        }

        public ExplorationData addLandmark(float x, String name, String description, String emoji) {
            landmarks.add(new Landmark(x, name, description, emoji));
            return this;
        }
    }

    public static class Landmark {
        public float x;
        public String name;
        public String description;
        public String emoji;
        public boolean discovered;

        public Landmark(float x, String name, String description, String emoji) {
            this.x = x;
            this.name = name;
            this.description = description;
            this.emoji = emoji;
            this. discovered = false;
        }
    }

    public static Array<CelestialData> createAllBodies() {
        Array<CelestialData> bodies = new Array<>();

        // ==================== SUN ====================
        CelestialData sun = new CelestialData();
        sun.id = "sun";
        sun.name = "The Sun";
        sun.type = "Star";
        sun.diameter = "1,392,700 km";
        sun.mass = "1.989 x 10^30 kg";
        sun.temperature = "5,500C (surface)";
        sun.distance = "0 (center)";
        sun. orbitalPeriod = "N/A";
        sun. orbitalSpeed = "N/A";
        sun.facts = new String[]{
            "Contains 99.86% of the Solar System mass",
            "Light takes 8 min 20 sec to reach Earth",
            "Core temperature: 15 million C"
        };
        sun. explorable = false;
        sun.baseSize = 100f;
        sun. orbitRadius = 0;
        sun.orbitSpeed = 0;
        sun.innerColor = new Color(1f, 0.95f, 0.7f, 1f);
        sun.outerColor = new Color(1f, 0.6f, 0f, 1f);
        bodies.add(sun);

        // ==================== MERCURY ====================
        CelestialData mercury = new CelestialData();
        mercury.id = "mercury";
        mercury.name = "Mercury";
        mercury. type = "Terrestrial Planet";
        mercury.diameter = "4,879 km";
        mercury. mass = "3.285 x 10^23 kg";
        mercury.temperature = "-173C to 427C";
        mercury.distance = "57.9 million km (0.39 AU)";
        mercury.orbitalPeriod = "88 days";
        mercury.orbitalSpeed = "47.87 km/s";
        mercury.facts = new String[]{
            "Closest planet to the Sun",
            "Has no atmosphere or moons",
            "One day lasts 176 Earth days"
        };
        mercury.explorable = true;
        mercury.baseSize = 18f;
        mercury.orbitRadius = 160f;
        mercury.orbitSpeed = 4.0f;
        mercury.orbitPhase = 0;
        mercury. innerColor = new Color(0.72f, 0.72f, 0.72f, 1f);
        mercury.outerColor = new Color(0.45f, 0.45f, 0.45f, 1f);
        mercury.hasCraters = true;
        mercury.exploration = new ExplorationData(
            new Color(0f, 0f, 0f, 1f),
            new Color(0.1f, 0.1f, 0.1f, 1f),
            new Color(0.42f, 0.42f, 0.42f, 1f),
            0.38f
        ).addLandmark(300, "Caloris Basin", "Massive impact crater 1,550 km wide", "O")
            .addLandmark(900, "Weird Terrain", "Chaotic landscape opposite Caloris", "^")
            .addLandmark(1500, "Ice Deposits", "Water ice in shadowed craters", "*");
        bodies.add(mercury);

        // ==================== VENUS ====================
        CelestialData venus = new CelestialData();
        venus.id = "venus";
        venus.name = "Venus";
        venus. type = "Terrestrial Planet";
        venus.diameter = "12,104 km";
        venus. mass = "4.867 x 10^24 kg";
        venus.temperature = "465C (average)";
        venus.distance = "108. 2 million km (0.72 AU)";
        venus.orbitalPeriod = "225 days";
        venus.orbitalSpeed = "35.02 km/s";
        venus.facts = new String[]{
            "Hottest planet in Solar System",
            "Rotates backwards (retrograde)",
            "Thick CO2 atmosphere"
        };
        venus.explorable = true;
        venus.baseSize = 28f;
        venus.orbitRadius = 220f;
        venus.orbitSpeed = 1.6f;
        venus.orbitPhase = 0.3f;
        venus.innerColor = new Color(0.96f, 0.9f, 0.8f, 1f);
        venus.outerColor = new Color(0.83f, 0.65f, 0.4f, 1f);
        venus.exploration = new ExplorationData(
            new Color(1f, 0.53f, 0.27f, 1f),
            new Color(1f, 0.67f, 0.4f, 1f),
            new Color(0.83f, 0.65f, 0.42f, 1f),
            0.91f
        ).addLandmark(350, "Maxwell Montes", "Highest mountain on Venus (11 km)", "^")
            .addLandmark(850, "Volcanic Plains", "Vast lava flows cover 80% of surface", "~")
            .addLandmark(1400, "Aphrodite Terra", "One of two continental highlands", "M");
        bodies.add(venus);

        // ==================== EARTH ====================
        CelestialData earth = new CelestialData();
        earth.id = "earth";
        earth.name = "Earth";
        earth. type = "Terrestrial Planet";
        earth.diameter = "12,742 km";
        earth. mass = "5.972 x 10^24 kg";
        earth.temperature = "15C (average)";
        earth.distance = "149.6 million km (1 AU)";
        earth. orbitalPeriod = "365.25 days";
        earth.orbitalSpeed = "29.78 km/s";
        earth.facts = new String[]{
            "Only known planet with liquid water",
            "Tilted 23.5 degrees causing seasons",
            "Magnetic field protects from solar wind"
        };
        earth.explorable = true;
        earth.baseSize = 30f;
        earth.orbitRadius = 290f;
        earth.orbitSpeed = 1.0f;
        earth.orbitPhase = 0;
        earth. innerColor = new Color(0.53f, 0.81f, 0.92f, 1f);
        earth.outerColor = new Color(0.18f, 0.35f, 0.62f, 1f);
        earth.hasContinents = true;
        earth.exploration = new ExplorationData(
            new Color(0.29f, 0.62f, 0.85f, 1f),
            new Color(0.53f, 0.81f, 0.92f, 1f),
            new Color(0.24f, 0.55f, 0.25f, 1f),
            1.0f
        ).addLandmark(400, "Mount Everest", "Tallest mountain on Earth (8,849 m)", "^")
            .addLandmark(950, "Ocean", "Covers 71% of Earth's surface", "~")
            .addLandmark(1500, "Forests", "Home to 80% of terrestrial biodiversity", "T");
        bodies.add(earth);

        // ==================== MOON ====================
        CelestialData moon = new CelestialData();
        moon.id = "moon";
        moon.name = "The Moon";
        moon.type = "Natural Satellite";
        moon.diameter = "3,474 km";
        moon.mass = "7.342 x 10^22 kg";
        moon.temperature = "-183C to 127C";
        moon.distance = "384,400 km from Earth";
        moon.orbitalPeriod = "27.3 days";
        moon.orbitalSpeed = "1.022 km/s";
        moon.facts = new String[]{
            "Always shows same face to Earth",
            "Causes ocean tides on Earth",
            "Moving away at 3.8 cm/year"
        };
        moon.explorable = true;
        moon. baseSize = 12f;
        moon.orbitRadius = 55f; // Orbit around Earth
        moon. orbitSpeed = 12.0f;
        moon.orbitPhase = 0;
        moon. innerColor = new Color(0.91f, 0.91f, 0.91f, 1f);
        moon.outerColor = new Color(0.62f, 0.62f, 0.62f, 1f);
        moon.hasCraters = true;
        moon.exploration = new ExplorationData(
            new Color(0f, 0f, 0f, 1f),
            new Color(0.04f, 0.04f, 0.04f, 1f),
            new Color(0.6f, 0.6f, 0.6f, 1f),
            0.166f
        ).addLandmark(400, "Tycho Crater", "Impact crater with bright rays", "O")
            .addLandmark(900, "Mare Tranquillitatis", "Apollo 11 landing site", "!")
            .addLandmark(1400, "South Pole", "Water ice in shadowed craters", "*");
        bodies.add(moon);

        // ==================== MARS ====================
        CelestialData mars = new CelestialData();
        mars.id = "mars";
        mars. name = "Mars";
        mars.type = "Terrestrial Planet";
        mars.diameter = "6,779 km";
        mars.mass = "6.417 x 10^23 kg";
        mars.temperature = "-65C (average)";
        mars.distance = "227.9 million km (1.52 AU)";
        mars.orbitalPeriod = "687 days";
        mars.orbitalSpeed = "24.07 km/s";
        mars.facts = new String[]{
            "Has Olympus Mons, tallest volcano",
            "Two moons: Phobos and Deimos",
            "A day (sol) is 24h 37min"
        };
        mars.explorable = true;
        mars.baseSize = 24f;
        mars.orbitRadius = 370f;
        mars.orbitSpeed = 0.53f;
        mars.orbitPhase = (float)(Math.PI / 3);
        mars.innerColor = new Color(0.91f, 0.66f, 0.49f, 1f);
        mars.outerColor = new Color(0.72f, 0.33f, 0.2f, 1f);
        mars.hasPolarCap = true;
        mars. exploration = new ExplorationData(
            new Color(0.83f, 0.45f, 0.29f, 1f),
            new Color(0.91f, 0.66f, 0.49f, 1f),
            new Color(0.72f, 0.33f, 0.2f, 1f),
            0.38f
        ).addLandmark(350, "Olympus Mons", "Tallest volcano in Solar System (21 km)", "^")
            .addLandmark(900, "Valles Marineris", "Canyon system 4,000 km long", "=")
            .addLandmark(1450, "Polar Ice Cap", "Frozen CO2 and water ice", "*");
        bodies.add(mars);

        // ==================== JUPITER ====================
        CelestialData jupiter = new CelestialData();
        jupiter.id = "jupiter";
        jupiter. name = "Jupiter";
        jupiter.type = "Gas Giant";
        jupiter.diameter = "139,820 km";
        jupiter.mass = "1.898 x 10^27 kg";
        jupiter.temperature = "-108C (cloud tops)";
        jupiter.distance = "778.5 million km (5.2 AU)";
        jupiter.orbitalPeriod = "11.86 years";
        jupiter.orbitalSpeed = "13.07 km/s";
        jupiter.facts = new String[]{
            "Largest planet in Solar System",
            "Great Red Spot storm larger than Earth",
            "Has 95 known moons"
        };
        jupiter.explorable = true;
        jupiter.baseSize = 70f;
        jupiter.orbitRadius = 480f;
        jupiter.orbitSpeed = 0.08f;
        jupiter.orbitPhase = (float)Math.PI;
        jupiter.innerColor = new Color(0.96f, 0.87f, 0.7f, 1f);
        jupiter.outerColor = new Color(0.83f, 0.65f, 0.45f, 1f);
        jupiter.hasGreatSpot = true;
        jupiter. exploration = new ExplorationData(
            new Color(0.83f, 0.65f, 0.42f, 1f),
            new Color(0.76f, 0.56f, 0.35f, 1f),
            new Color(0.91f, 0.77f, 0.55f, 1f),
            2.53f
        ).addLandmark(450, "Great Red Spot", "Giant storm raging for 350+ years", "@")
            .addLandmark(950, "Cloud Bands", "Alternating zones and belts", "=")
            .addLandmark(1400, "Lightning Storms", "3x more powerful than Earth", "!");
        bodies.add(jupiter);

        // ==================== SATURN ====================
        CelestialData saturn = new CelestialData();
        saturn.id = "saturn";
        saturn.name = "Saturn";
        saturn.type = "Gas Giant";
        saturn.diameter = "116,460 km";
        saturn.mass = "5.683 x 10^26 kg";
        saturn.temperature = "-139C (cloud tops)";
        saturn. distance = "1.43 billion km (9.5 AU)";
        saturn.orbitalPeriod = "29.46 years";
        saturn. orbitalSpeed = "9.69 km/s";
        saturn.facts = new String[]{
            "Famous for spectacular ring system",
            "Rings made of ice and rock",
            "Has 146 known moons including Titan"
        };
        saturn. explorable = true;
        saturn.baseSize = 60f;
        saturn. orbitRadius = 600f;
        saturn.orbitSpeed = 0.034f;
        saturn.orbitPhase = (float)(Math.PI / 2);
        saturn.innerColor = new Color(0.96f, 0.9f, 0.78f, 1f);
        saturn.outerColor = new Color(0.72f, 0.6f, 0.4f, 1f);
        saturn.hasRings = true;
        saturn.exploration = new ExplorationData(
            new Color(0.96f, 0.9f, 0.78f, 1f),
            new Color(0.91f, 0.83f, 0.63f, 1f),
            new Color(0.83f, 0.72f, 0.48f, 1f),
            1.07f
        ).addLandmark(400, "Ring System", "Spectacular ice rings 282,000 km wide", "O")
            .addLandmark(900, "Hexagonal Storm", "Mysterious hexagon at north pole", "#")
            .addLandmark(1350, "Titan", "Moon with thicker atmosphere than Earth", "o");
        bodies.add(saturn);

        // ==================== URANUS ====================
        CelestialData uranus = new CelestialData();
        uranus.id = "uranus";
        uranus. name = "Uranus";
        uranus.type = "Ice Giant";
        uranus.diameter = "50,724 km";
        uranus. mass = "8.681 x 10^25 kg";
        uranus.temperature = "-197C (average)";
        uranus.distance = "2.87 billion km (19.2 AU)";
        uranus.orbitalPeriod = "84 years";
        uranus.orbitalSpeed = "6.81 km/s";
        uranus. facts = new String[]{
            "Rotates on its side (98 degree tilt)",
            "Made of water, methane, ammonia ices",
            "Has 27 known moons"
        };
        uranus. explorable = true;
        uranus. baseSize = 45f;
        uranus.orbitRadius = 720f;
        uranus.orbitSpeed = 0.012f;
        uranus.orbitPhase = (float)(Math.PI / 4);
        uranus.innerColor = new Color(0.78f, 0.91f, 0.94f, 1f);
        uranus.outerColor = new Color(0.47f, 0.72f, 0.78f, 1f);
        uranus.exploration = new ExplorationData(
            new Color(0.78f, 0.91f, 0.94f, 1f),
            new Color(0.63f, 0.83f, 0.88f, 1f),
            new Color(0.47f, 0.72f, 0.78f, 1f),
            0.89f
        ).addLandmark(420, "Methane Clouds", "Give Uranus its cyan color", "~")
            .addLandmark(900, "Extreme Tilt", "Rotates nearly on its side", "/")
            .addLandmark(1380, "Ice Mantle", "Ocean of water, ammonia, methane", "*");
        bodies.add(uranus);

        // ==================== NEPTUNE ====================
        CelestialData neptune = new CelestialData();
        neptune. id = "neptune";
        neptune.name = "Neptune";
        neptune.type = "Ice Giant";
        neptune.diameter = "49,244 km";
        neptune.mass = "1.024 x 10^26 kg";
        neptune.temperature = "-201C (average)";
        neptune.distance = "4.5 billion km (30 AU)";
        neptune.orbitalPeriod = "164.8 years";
        neptune.orbitalSpeed = "5.43 km/s";
        neptune. facts = new String[]{
            "Windiest planet - 2,100 km/h",
            "Discovered by mathematical prediction",
            "Has 14 known moons including Triton"
        };
        neptune.explorable = true;
        neptune.baseSize = 42f;
        neptune.orbitRadius = 840f;
        neptune.orbitSpeed = 0.006f;
        neptune.orbitPhase = (float)(Math.PI * 1.5);
        neptune.innerColor = new Color(0.38f, 0.5f, 0.82f, 1f);
        neptune.outerColor = new Color(0.19f, 0.28f, 0.55f, 1f);
        neptune.exploration = new ExplorationData(
            new Color(0.38f, 0.5f, 0.82f, 1f),
            new Color(0.25f, 0.38f, 0.69f, 1f),
            new Color(0.19f, 0.28f, 0.56f, 1f),
            1.14f
        ).addLandmark(400, "Great Dark Spot", "Storm system size of Earth", "@")
            .addLandmark(900, "Supersonic Winds", "Fastest winds in Solar System", ">")
            .addLandmark(1400, "Triton Geysers", "Moon with nitrogen geysers", "^");
        bodies.add(neptune);

        // ==================== PLUTO ====================
        CelestialData pluto = new CelestialData();
        pluto.id = "pluto";
        pluto. name = "Pluto";
        pluto.type = "Dwarf Planet";
        pluto. diameter = "2,377 km";
        pluto.mass = "1.303 x 10^22 kg";
        pluto.temperature = "-223C (average)";
        pluto.distance = "5.9 billion km (39.5 AU)";
        pluto.orbitalPeriod = "248 years";
        pluto.orbitalSpeed = "4.74 km/s";
        pluto. facts = new String[]{
            "Reclassified as dwarf planet in 2006",
            "Has heart-shaped glacier region",
            "Moon Charon is half its size"
        };
        pluto. explorable = true;
        pluto. baseSize = 14f;
        pluto.orbitRadius = 960f;
        pluto.orbitSpeed = 0.004f;
        pluto.orbitPhase = (float)(Math.PI * 0.7);
        pluto.innerColor = new Color(0.85f, 0.78f, 0.69f, 1f);
        pluto.outerColor = new Color(0.53f, 0.47f, 0.38f, 1f);
        pluto.hasHeartRegion = true;
        pluto.exploration = new ExplorationData(
            new Color(0f, 0f, 0f, 1f),
            new Color(0.1f, 0.08f, 0.12f, 1f),
            new Color(0.53f, 0.47f, 0.38f, 1f),
            0.063f
        ).addLandmark(400, "Tombaugh Regio", "Heart-shaped nitrogen ice plain", "<3")
            .addLandmark(900, "Sputnik Planitia", "Frozen nitrogen glacier", "*")
            .addLandmark(1400, "Ice Mountains", "Water ice mountains up to 3 km", "^");
        bodies.add(pluto);

        return bodies;
    }
}
