package com.gildedrose;

public class Item {

    public static final int MAX_QUALITY = 50;
    public static final int MIN_QUALITY = 0;
    public static final String SULFURAS_HAND_OF_RAGNAROS = "Sulfuras, Hand of Ragnaros";
    public static final String AGED_BRIE = "Aged Brie";
    public static final String BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT = "Backstage passes to a TAFKAL80ETC concert";

    public String name;
    public int sellIn;
    public int quality;

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

    void updateAgedBrie() {
        increaseQuality();

        decreaseSellIn();

        if (isExpired()) {
            increaseQuality();
        }
    }

    void updateBackstagePasses() {
        increaseQuality();

        if (sellIn < 11) {
            increaseQuality();
        }

        if (sellIn < 6) {
            increaseQuality();
        }

        decreaseSellIn();

        if (isExpired()) {
            invalidateItem();
        }
    }

    void updateDefault() {
        decreaseQuality();

        decreaseSellIn();

        if (isExpired()) {
            decreaseQuality();
        }
    }

    public void updateSulfuras() {
        //do nothing
    }

    void decreaseSellIn() {
        sellIn = sellIn - 1;
    }

    void increaseQuality() {
        quality = Math.min(quality + 1, MAX_QUALITY);
    }

    void decreaseQuality() {
        quality = Math.max(quality - 1, MIN_QUALITY);
    }

    void invalidateItem() {
        quality = 0;
    }

    boolean isExpired() {
        return sellIn < 0;
    }
}
