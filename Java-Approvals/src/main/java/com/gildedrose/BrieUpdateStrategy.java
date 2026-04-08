package com.gildedrose;

public class BrieUpdateStrategy implements UpdateStrategy {


    static final BrieUpdateStrategy INSTANCE = new BrieUpdateStrategy();
    private BrieUpdateStrategy() {}

    @Override
    public void update(Item item) {
        item.increaseQuality();
        item.decreaseSellIn();

        if (item.isExpired()) {
            item.increaseQuality();
        }
    }
}
