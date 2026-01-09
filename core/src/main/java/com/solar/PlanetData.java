package com.solar;

import java.util.*;

public class PlanetData {
    public String id;
    public String name;
    public float x;
    public float y;
    public float gravity;
    public float radius;
    public String texturePath;
    public String description;

    public HashMap<String, Float> dropRates;

    private static final Random random = new Random();

    public PlanetData() {
        dropRates = new HashMap<>();
    }

    public float getDropRate(String resourceId) {
        return dropRates.getOrDefault(resourceId, 0f);
    }

    public String randomResource() {
        if (dropRates.isEmpty()) return null;

        float rand = random.nextFloat();
        float sum = 0f;

        for (Map.Entry<String, Float> entry : dropRates.entrySet()) {
            sum += entry.getValue();
            if (rand <= sum) {
                return entry.getKey();
            }
        }

        return null; // Nếu tổng tỷ lệ < 1.0
    }

    public Map<String, Integer> randomResources(int count) {
        Map<String, Integer> results = new HashMap<>();

        for (int i = 0; i < count; i++) {
            String resource = randomResource();
            if (resource != null) {
                results.put(resource, results.getOrDefault(resource, 0) + 1);
            }
        }

        return results;
    }

    public boolean hasResources() {
        return !dropRates.isEmpty();
    }

    public float getTotalDropRate() {
        float total = 0f;
        for (float rate : dropRates.values()) {
            total += rate;
        }
        return total;
    }

    public String getMostCommonResource() {
        if (dropRates.isEmpty()) return null;

        String mostCommon = null;
        float maxRate = 0f;

        for (Map.Entry<String, Float> entry : dropRates.entrySet()) {
            if (entry.getValue() > maxRate) {
                maxRate = entry.getValue();
                mostCommon = entry.getKey();
            }
        }

        return mostCommon;
    }

    public String randomResourceAlways() {
        if (dropRates.isEmpty()) return null;

        float total = getTotalDropRate();
        if (total <= 0) return null;

        float rand = random.nextFloat() * total;
        float sum = 0f;

        for (Map.Entry<String, Float> entry : dropRates.entrySet()) {
            sum += entry.getValue();
            if (rand <= sum) {
                return entry.getKey();
            }
        }

        // Fallback: trả về tài nguyên phổ biến nhất
        return getMostCommonResource();
    }

    @Override
    public String toString() {
        return String.format("Planet[id=%s, name=%s, resources=%d]",
            id, name, dropRates.size());
    }
}
