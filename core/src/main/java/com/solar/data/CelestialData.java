package com.solar.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

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
        public Array<Landmark> landmarks = new Array<>();
    }

    public static class Landmark {
        public float x;
        public String name;
        public String description;
        public String emoji;
        public boolean discovered;
    }

    public static Array<CelestialData> createAllBodies() {
        Json json = new Json();

        // Custom serializer cho Color
        json.setSerializer(Color.class, new Json.Serializer<Color>() {
            @Override
            public void write(Json json, Color object, Class knownType) {
                json.writeObjectStart();
                json.writeValue("r", object.r);
                json.writeValue("g", object.g);
                json.writeValue("b", object.b);
                json.writeValue("a", object.a);
                json.writeObjectEnd();
            }

            @Override
            public Color read(Json json, JsonValue jsonData, Class type) {
                return new Color(
                        jsonData.getFloat("r"),
                        jsonData.getFloat("g"),
                        jsonData.getFloat("b"),
                        jsonData.getFloat("a", 1f)
                );
            }
        });

        return json.fromJson(
                Array.class,
                CelestialData.class,
                Gdx.files.internal("data/celestial_data.json")
        );
    }
}
