package com.gildedrose;

class GildedRose {
    public static final String AGED_BRIE = "Aged Brie";
    public static final String BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT = "Backstage passes to a TAFKAL80ETC concert";
    public static final String SULFURAS_HAND_OF_RAGNAROS = "Sulfuras, Hand of Ragnaros";
    public static final int MAX_QUALITY = 50;
    public static final int MIN_QUALITY = 0;
    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    public void updateQuality() {
        for (Item item : items) {
            switch (item.name) {
                case AGED_BRIE -> updateAgedBrie(item);
                case BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT -> updateBackstagePasses(item);
                case SULFURAS_HAND_OF_RAGNAROS -> updateSulfuras();
                default -> updateDefault(item);
            }
        }
    }

    private void updateAgedBrie(Item item) {
        increaseQuality(item);
        decreaseSellIn(item);

        if (isExpired(item)) {
            increaseQuality(item);
        }
    }

    private void updateBackstagePasses(Item item) {
        increaseQuality(item);

        if (item.sellIn < 11) {
            increaseQuality(item);
        }

        if (item.sellIn < 6) {
            increaseQuality(item);
        }

        decreaseSellIn(item);

        if (isExpired(item)) {
            setQualityToMin(item);
        }
    }

    private void updateSulfuras() {
        //do nothing
    }

    private void updateDefault(Item item) {
        decreaseQuality(item);

        decreaseSellIn(item);

        if (isExpired(item)) {
            decreaseQuality(item);
        }
    }

    private boolean isExpired(Item item) {
        return item.sellIn < MIN_QUALITY;
    }

    private void decreaseSellIn(Item item) {
        item.sellIn--;
    }

    private void setQualityToMin(Item item) {
        item.quality = MIN_QUALITY;
    }

    private void increaseQuality(Item item) {
        item.quality = Math.min(item.quality + 1, MAX_QUALITY);
    }

    private void decreaseQuality(Item item) {
        item.quality = Math.max(item.quality - 1, MIN_QUALITY);
    }
}
