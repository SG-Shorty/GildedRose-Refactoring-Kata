package com.gildedrose;

enum ItemType {

    DEFAULT(DefaultUpdateStrategy.INSTANCE),
    AGED_BRIE(BrieUpdateStrategy.INSTANCE),
    BACKSTAGE_PASSES(BackstagePassesUpdateStrategy.INSTANCE),
    SULFURAS(SulfurasUpdateStrategy.INSTANCE);

    private final UpdateStrategy strategy;

    ItemType(UpdateStrategy strategy) {
        this.strategy = strategy;
    }

    public static final String AGED_BRIE_NAME = "Aged Brie";
    public static final String BACKSTAGE_PASSES_NAME = "Backstage passes to a TAFKAL80ETC concert";
    public static final String SULFURAS_NAME = "Sulfuras, Hand of Ragnaros";

    static ItemType fromName(String name) {
        return switch (name) {
            case AGED_BRIE_NAME -> AGED_BRIE;
            case BACKSTAGE_PASSES_NAME -> BACKSTAGE_PASSES;
            case SULFURAS_NAME -> SULFURAS;
            default -> DEFAULT;
        };
    }

    UpdateStrategy getUpdateStrategy() {
        return strategy;
    }

}
