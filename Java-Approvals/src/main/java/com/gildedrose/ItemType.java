package com.gildedrose;

import java.util.Objects;

public enum ItemType {

    AGED_BRIE("Aged Brie") {
        @Override
        void updateQuality(Item item) {
            increaseQuality(item);
            decreaseSellIn(item);

            if (isExpired(item)) {
                increaseQuality(item);
            }
        }
    },

    BACKSTAGE_PASSES("Backstage passes to a TAFKAL80ETC concert") {
        @Override
        void updateQuality(Item item) {
            increaseQuality(item);

            if (item.sellIn <= 10) {
                increaseQuality(item);
            }

            if (item.sellIn <= 5) {
                increaseQuality(item);
            }

            decreaseSellIn(item);

            if (isExpired(item)) {
                setQualityToMin(item);
            }
        }
    },

    SULFURAS("Sulfuras, Hand of Ragnaros") {
        @Override
        void updateQuality(Item item) {
            // Sulfuras never changes.
        }
    },
    CONJURED("Conjured") {
        @Override
        void updateQuality(Item item) {
            decreaseQuality(item);
            decreaseQuality(item);

            decreaseSellIn(item);

            if (isExpired(item)) {
                decreaseQuality(item);
                decreaseQuality(item);
            }
        }
    },

    REGULAR(null) {
        @Override
        void updateQuality(Item item) {
            decreaseQuality(item);
            decreaseSellIn(item);

            if (isExpired(item)) {
                decreaseQuality(item);
            }
        }
    };

    private static final int MAX_QUALITY = 50;
    private static final int MIN_QUALITY = 0;
    private static final int EXPIRED_SELL_IN = 0;


    private final String itemName;

    ItemType(String itemName) {
        this.itemName = itemName;
    }

    static ItemType fromName(String name) {
        Objects.requireNonNull(name, "Item name cannot be null");
        for (ItemType type : ItemType.values()) {
            if (type.itemName != null && name.startsWith(type.itemName)) {
                return type;
            }
        }

        return REGULAR;
    }

    abstract void updateQuality(Item item);


    private static boolean isExpired(Item item) {
        return item.sellIn < EXPIRED_SELL_IN;
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
