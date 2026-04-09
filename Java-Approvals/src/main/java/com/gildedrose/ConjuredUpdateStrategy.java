package com.gildedrose;

public class ConjuredUpdateStrategy implements UpdateStrategy {

    static final ConjuredUpdateStrategy INSTANCE = new ConjuredUpdateStrategy();

    private ConjuredUpdateStrategy() {
    }

    @Override
    public void update(Item item) {
        item.decreaseQuality();
        item.decreaseQuality();
        item.decreaseRemainingDays();

        if (item.isExpired()) {
            item.decreaseQuality();
            item.decreaseQuality();
        }
    }
}
