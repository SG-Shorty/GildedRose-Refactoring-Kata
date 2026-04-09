package com.gildedrose;

enum ItemType {

    DEFAULT(null, DefaultUpdateStrategy.INSTANCE),
    AGED_BRIE("Aged Brie", BrieUpdateStrategy.INSTANCE),
    BACKSTAGE_PASSES("Backstage passes to a TAFKAL80ETC concert", BackstagePassesUpdateStrategy.INSTANCE),
    CONJURED("Conjured ", ConjuredUpdateStrategy.INSTANCE),
    SULFURAS("Sulfuras, Hand of Ragnaros", SulfurasUpdateStrategy.INSTANCE);

    private final UpdateStrategy strategy;
    private final String itemName;

    ItemType(String itemName, UpdateStrategy strategy) {
        this.strategy = strategy;
        this.itemName = itemName;
    }

    static ItemType fromName(String name) {
        for (ItemType type : values()) {
            if (type.itemName != null && name.startsWith(type.itemName)) {
                return type;
            }
        }
        return DEFAULT;
    }

    UpdateStrategy getUpdateStrategy() {
        return strategy;
    }

    public String itemName() {
        return itemName;
    }
}
