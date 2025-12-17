package com.solar.data.models;
import java.util.List;

public class InventoryData {
    public String currentPlanetId;
    public float maxEnergy;
    public float currentEnergy;
    public List<SlotData> slots;

    // Class con để lưu từng ô đồ
    public static class SlotData {
        public String itemId;
        public int quantity;
    }
}
