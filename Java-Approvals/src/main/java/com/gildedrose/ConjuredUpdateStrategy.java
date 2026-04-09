package com.gildedrose;

public class ConjuredUpdateStrategy implements UpdateStrategy {

    static final ConjuredUpdateStrategy INSTANCE = new ConjuredUpdateStrategy();

    private ConjuredUpdateStrategy() {
    }

    @Override
    public void update(Item item) {
        item.decreaseQualityBy(2);
        item.decreaseRemainingDays();

        if (item.isExpired()) {
            item.decreaseQualityBy(2);
        }
    }
}
