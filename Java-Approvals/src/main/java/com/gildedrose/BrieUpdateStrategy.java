package com.gildedrose;

public class BrieUpdateStrategy implements UpdateStrategy {


    static final BrieUpdateStrategy INSTANCE = new BrieUpdateStrategy();
    private BrieUpdateStrategy() {}

    @Override
    public void update(Item item) {
        item.decreaseRemainingDays();

        if (item.isExpired()) {
            item.increaseQualityBy(2);
        } else {
            item.increaseQuality();
        }
    }
}
