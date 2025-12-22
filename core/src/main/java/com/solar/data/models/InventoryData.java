package com.solar.data.models;

import java.util.ArrayList;
import java.util.List;

public class InventoryData {
    public String currentPlanetId;
    public float maxEnergy;
    public float currentEnergy;
    public List<SlotData> slots;
    public InventoryData() {
        // Khởi tạo mặc định để tránh lỗi NullPointerException
        slots = new ArrayList<>();
        currentPlanetId = "earth";
        currentEnergy = 100f;
    }

    public static class SlotData {
        public String itemId;
        public int quantity;

        public SlotData() {}

        // Constructor tiện ích để code cho nhanh
        public SlotData(String itemId, int quantity) {
            this.itemId = itemId;
            this.quantity = quantity;
        }
    }
}
