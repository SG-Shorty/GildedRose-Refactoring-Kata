package com.gildedrose;

public class Item {

    private static final int MAX_QUALITY = 50;
    private static final int MIN_QUALITY = 0;
    private static final String SULFURAS_HAND_OF_RAGNAROS = "Sulfuras, Hand of Ragnaros";
    private static final String AGED_BRIE = "Aged Brie";
    private static final String BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT = "Backstage passes to a TAFKAL80ETC concert";

    private final String name;
    private int sellIn;
    private int quality;

    public Item(String name, int sellIn, int quality) {
        this.name = name;
        this.sellIn = sellIn;
        this.quality = quality;
    }

    @Override
    public String toString() {
        return this.name + ", " + this.sellIn + ", " + this.quality;
    }


    void update() {
        switch (name) {
            case AGED_BRIE -> updateAgedBrie();
            case BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT -> updateBackstagePasses();
            case SULFURAS_HAND_OF_RAGNAROS -> updateSulfuras();
            default -> updateDefault();
        }
    }

    private void updateAgedBrie() {
        increaseQuality();
        decreaseSellIn();

        if (isExpired()) {
            increaseQuality();
        }
    }

    private void updateBackstagePasses() {
        increaseQuality();

        if (sellIn < 11) {
            increaseQuality();
        }

        if (sellIn < 6) {
            increaseQuality();
        }

        decreaseSellIn();

        if (isExpired()) {
            invalidate();
        }
    }

    private void updateSulfuras() {/*do nothing*/}

    private void updateDefault() {
        decreaseQuality();
        decreaseSellIn();

        if (isExpired()) {
            decreaseQuality();
        }
    }

    private void decreaseSellIn() {
        sellIn = sellIn - 1;
    }

    private void increaseQuality() {
        quality = Math.min(quality + 1, MAX_QUALITY);
    }

    private void decreaseQuality() {
        quality = Math.max(quality - 1, MIN_QUALITY);
    }

    private void invalidate() {
        quality = 0;
    }

    private boolean isExpired() {
        return sellIn < 0;
    }
}
