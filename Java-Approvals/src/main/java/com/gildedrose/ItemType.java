package com.gildedrose;

import java.util.Objects;

public enum ItemType {

    AGED_BRIE("Aged Brie") {
        @Override
        public void updateQuality(Item item) {
            updateAgedBrie(item);
        }
    },

    BACKSTAGE_PASSES("Backstage passes to a TAFKAL80ETC concert") {
        @Override
        public void updateQuality(Item item) {
            updateBackstagePasses(item);
        }
    },

    SULFURAS("Sulfuras, Hand of Ragnaros") {
        @Override
        public void updateQuality(Item item) {
            updateSulfuras();
        }
    },

    DEFAULT(null) {
        @Override
        public void updateQuality(Item item) {
            updateDefault(item);
        }
    };

    private static final int MAX_QUALITY = 50;
    private static final int MIN_QUALITY = 0;

    private final String itemName;

    ItemType(String itemName) {
        this.itemName = itemName;
    }

    public static ItemType fromName(String name) {
        Objects.requireNonNull(name, "Item name cannot be null");

        for (ItemType type : ItemType.values()) {
            if (name.equals(type.itemName)) {
                return type;
            }
        }

        return DEFAULT;
    }

    public abstract void updateQuality(Item item);

    public String getName() {
        return itemName;
    }

    private static void updateAgedBrie(Item item) {
        increaseQuality(item);
        decreaseSellIn(item);

        if (isExpired(item)) {
            increaseQuality(item);
        }
    }

    private static void updateBackstagePasses(Item item) {
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

    private static void updateSulfuras() {
        //do nothing
    }

    private static void updateDefault(Item item) {
        decreaseQuality(item);

        decreaseSellIn(item);

        if (isExpired(item)) {
            decreaseQuality(item);
        }
    }

    private static boolean isExpired(Item item) {
        return item.sellIn < MIN_QUALITY;
    }

    private static void decreaseSellIn(Item item) {
        item.sellIn--;
    }

    private static void setQualityToMin(Item item) {
        item.quality = MIN_QUALITY;
    }

    private static void increaseQuality(Item item) {
        item.quality = Math.min(item.quality + 1, MAX_QUALITY);
    }

    private static void decreaseQuality(Item item) {
        item.quality = Math.max(item.quality - 1, MIN_QUALITY);
    }
}
