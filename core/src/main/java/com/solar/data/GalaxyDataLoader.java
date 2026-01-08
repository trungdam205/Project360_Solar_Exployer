package com.solar.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.solar.entities.Planet;

public class GalaxyDataLoader {

    public static Array<Planet> loadPlanets() {
        Json json = new Json();

        // Parse the JSON file which has the form: { "planets": [ ... ] }
        JsonReader reader = new JsonReader();
        JsonValue root = reader.parse(Gdx.files.internal("data/galaxy.json"));
        JsonValue planetsJson = root.get("planets");

        Array<Planet> planets = new Array<>();
        if (planetsJson != null) {
            for (JsonValue pv = planetsJson.child; pv != null; pv = pv.next) {
                Planet p = json.readValue(Planet.class, pv);
                planets.add(p);
            }
        }

        // ===== Layout tạm (phần của B) =====
        float startX = 100;
        float y = 300;
        float spacing = 50;

        for (Planet p : planets) {
            p.x = startX;
            p.y = y;
            p.loadTexture();
            startX += spacing;
        }

        return planets;
    }
}
