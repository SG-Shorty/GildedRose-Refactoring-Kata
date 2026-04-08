package com.gildedrose;

import java.util.Objects;

public class Item {

    private static final int MAX_QUALITY = 50;
    private static final int MIN_QUALITY = 0;

    private final UpdateStrategy updateStrategy;

    private final String name;
    private int remainingDays;

    private int quality;

    public Item(String name, int sellIn, int quality) {
        this.name = Objects.requireNonNull(name, "Item name must not be null");
        this.remainingDays = sellIn;
        this.quality = quality;
        this.updateStrategy = ItemType.fromName(name).getUpdateStrategy();
    }

    public int remainingDays() {
        return remainingDays;
    }

    public int getQuality() {
        return quality;
    }

    @Override
    public String toString() {
        return this.name + ", " + this.remainingDays + ", " + this.quality;
    }

    void update() {
        updateStrategy.update(this);
    }

    void decreaseRemainingDays() {
        remainingDays = remainingDays - 1;
    }

    void increaseQuality() {
        increaseQualityBy(1);
    }

    void decreaseQuality() {
        changeQualityBy(-1);
    }

    void increaseQualityBy(int amount) {
        changeQualityBy(amount);
    }

    private void changeQualityBy(int amount) {
        quality = Math.clamp(quality + amount, MIN_QUALITY, MAX_QUALITY);
    }

    void invalidate() {
        quality = MIN_QUALITY;
    }

    boolean isExpired() {
        return remainingDays < 0;
    }
}
