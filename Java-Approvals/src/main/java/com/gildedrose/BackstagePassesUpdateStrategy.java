package com.gildedrose;

public class BackstagePassesUpdateStrategy implements UpdateStrategy {

    static final BackstagePassesUpdateStrategy INSTANCE = new BackstagePassesUpdateStrategy();
    private BackstagePassesUpdateStrategy() {}

    @Override
    public void update(Item item) {
        item.increaseQuality();

        if (item.remainingDays() < 11) {
            item.increaseQuality();
        }

        if (item.remainingDays() < 6) {
            item.increaseQuality();
        }

        item.decreaseRemainingDays();

        if (item.isExpired()) {
            item.invalidate();
        }
    }
}
