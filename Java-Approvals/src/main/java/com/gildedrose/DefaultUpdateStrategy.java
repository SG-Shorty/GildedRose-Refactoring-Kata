package com.gildedrose;

public class DefaultUpdateStrategy implements UpdateStrategy {

    static final DefaultUpdateStrategy INSTANCE = new DefaultUpdateStrategy();
    private DefaultUpdateStrategy() {}

    @Override
    public void update(Item item) {
        item.decreaseQuality();
        item.decreaseSellIn();

        if (item.isExpired()) {
            item.decreaseQuality();
        }
    }
}
