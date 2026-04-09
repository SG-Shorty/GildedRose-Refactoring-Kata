package com.gildedrose;

import java.util.Objects;

public class Item {

    private static final int MAX_QUALITY = 50;
    private static final int MIN_QUALITY = 0;
    private static final int SULFURAS_QUALITY = 80;

    private final UpdateStrategy updateStrategy;

    private final String name;
    private int remainingDays;
    private int quality;

    public Item(String name, int sellIn, int quality) {
        this.name = Objects.requireNonNull(name, "Item name must not be null");
        ItemType itemType = ItemType.fromName(this.name);
        this.remainingDays = sellIn;
        this.quality = initialQuality(quality, itemType);
        this.updateStrategy = itemType.getUpdateStrategy();
    }

    private int initialQuality(int quality, ItemType itemType) {
        if (itemType == ItemType.SULFURAS) {
            return SULFURAS_QUALITY;
        }

        return Math.clamp(quality, MIN_QUALITY, MAX_QUALITY);
    }

    public int remainingDays() {
        return remainingDays;
    }

    public int getQuality() {
        return quality;
    }

    @Override
    public String toString() {
        return name + ", " + remainingDays + ", " + quality;
    }

    void update() {
        updateStrategy.update(this);
    }

    void decreaseRemainingDays() {
        remainingDays--;
    }

    void increaseQuality() {
        increaseQualityBy(1);
    }

    void decreaseQuality() {
        adjustQuality(-1);
    }

    void increaseQualityBy(int amount) {
        adjustQuality(amount);
    }

    private void adjustQuality(int amount) {
        quality = Math.clamp(quality + amount, MIN_QUALITY, MAX_QUALITY);
    }

    /**
     * Sets quality to zero for rules that explicitly require it.
     * This is intentionally separated from normal bounded adjustments.
     */
    void setQualityToZero() {
        quality = MIN_QUALITY;
    }

    boolean isExpired() {
        return remainingDays < 0;
    }
}
