package com.solar.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.solar.data.models.InventoryData;
import com.solar.data.models.ItemData;
import com.solar.data.models.PlanetData;

import java.util.ArrayList;
import java.util.HashMap;

public class DataManager {
    // 1. Singleton: Chỉ có 1 instance duy nhất trong toàn bộ game
    private static DataManager instance;

    // 2. Nơi chứa dữ liệu sau khi load lên RAM
    private HashMap<String, PlanetData> planetMap; // Map để tìm nhanh theo ID
    private HashMap<String, ItemData> itemMap;
    private InventoryData playerInventory; // Dữ liệu người chơi hiện tại (Save game)

    private Json json;

    // Private Constructor (Không cho bên ngoài tự tạo mới)
    private DataManager() {
        json = new Json();
        json.setIgnoreUnknownFields(true); // Tránh lỗi nếu JSON có trường lạ
        planetMap = new HashMap<>();
        itemMap = new HashMap<>();
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    // --- PHẦN 1: LOAD DỮ LIỆU TĨNH (Assets) ---
    // Gọi hàm này 1 lần duy nhất lúc bật game (trong create())
    public void loadStaticData() {
        try {
            // Load Planets
            FileHandle planetFile = Gdx.files.internal("data/planets.json");
            ArrayList<PlanetData> planets = json.fromJson(ArrayList.class, PlanetData.class, planetFile);
            for (PlanetData p : planets) {
                planetMap.put(p.id, p);
            }

            // Load Items
            FileHandle itemFile = Gdx.files.internal("data/items.json");
            ArrayList<ItemData> items = json.fromJson(ArrayList.class, ItemData.class, itemFile);
            for (ItemData i : items) {
                itemMap.put(i.id, i);
            }

            Gdx.app.log("DataManager", "Loaded: " + planetMap.size() + " planets, " + itemMap.size() + " items.");
        } catch (Exception e) {
            Gdx.app.error("DataManager", "Lỗi load data tĩnh: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- PHẦN 2: SAVE / LOAD GAME (Local Storage) ---

    // Gọi khi bấm "Continue" hoặc bắt đầu game
    public void loadGame() {
        FileHandle saveFile = Gdx.files.local("saves/savegame.json");

        if (saveFile.exists()) {
            try {
                playerInventory = json.fromJson(InventoryData.class, saveFile);
                Gdx.app.log("DataManager", "Save game loaded!");
            } catch (Exception e) {
                Gdx.app.error("DataManager", "File save lỗi, tạo mới!");
                createNewGame();
            }
        } else {
            Gdx.app.log("DataManager", "Chưa có file save, tạo mới!");
            createNewGame();
        }
    }

    // Gọi khi muốn lưu game
    public void saveGame() {
        if (playerInventory == null) return;

        FileHandle saveFile = Gdx.files.local("saves/savegame.json");
        saveFile.writeString(json.prettyPrint(playerInventory), false);
        Gdx.app.log("DataManager", "Game Saved!");
    }

    private void createNewGame() {
        playerInventory = new InventoryData(); // Nó sẽ tự gọi Constructor mặc định (Earth, 100 energy)
    }

    // --- PHẦN 3: GETTERS (Cho các module khác gọi) ---

    public PlanetData getPlanet(String id) {
        return planetMap.get(id);
    }

    public ItemData getItem(String id) {
        return itemMap.get(id);
    }

    public InventoryData getPlayerInventory() {
        // Luôn đảm bảo không null
        if (playerInventory == null) loadGame();
        return playerInventory;
    }
}
