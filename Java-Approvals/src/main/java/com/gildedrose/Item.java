package com.gildedrose;

public class Item {

    private static final int MAX_QUALITY = 50;
    private static final int MIN_QUALITY = 0;
    private static final String SULFURAS_HAND_OF_RAGNAROS = "Sulfuras, Hand of Ragnaros";
    private static final String AGED_BRIE = "Aged Brie";
    private static final String BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT = "Backstage passes to a TAFKAL80ETC concert";

    private final UpdateStrategy updateStrategy;

    private final String name;
    private int remainingDays;
    private int quality;

    public Item(String name, int sellIn, int quality) {
        this.name = name;
        this.remainingDays = sellIn;
        this.quality = quality;
        this.updateStrategy = getUpdateStrategy(name);
    }

    private UpdateStrategy getUpdateStrategy(String name) {
        return switch (name) {
            case AGED_BRIE -> BrieUpdateStrategy.INSTANCE;
            case BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT -> BackstagePassesUpdateStrategy.INSTANCE;
            case SULFURAS_HAND_OF_RAGNAROS -> SulfurasUpdateStrategy.INSTANCE;
            default -> DefaultUpdateStrategy.INSTANCE;
        };
    }

    public int remainingDays() {
        return remainingDays;
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
        quality = Math.min(quality + 1, MAX_QUALITY);
    }

    void decreaseQuality() {
        quality = Math.max(quality - 1, MIN_QUALITY);
    }

    void invalidate() {
        quality = 0;
    }

    boolean isExpired() {
        return remainingDays < 0;
    }
}
