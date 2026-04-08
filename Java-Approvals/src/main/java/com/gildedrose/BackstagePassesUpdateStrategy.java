package com.gildedrose;

public class BackstagePassesUpdateStrategy implements UpdateStrategy {

    static final BackstagePassesUpdateStrategy INSTANCE = new BackstagePassesUpdateStrategy();
    private BackstagePassesUpdateStrategy() {}

    @Override
    public void update(Item item) {
        increaseQuality(item);
        item.decreaseRemainingDays();

        if (item.isExpired()) {
            item.invalidate();
        }
    }

    private void increaseQuality(Item item) {
        if (item.remainingDays() < 6) {
            item.increaseQualityBy(3);
        } else if (item.remainingDays() < 11) {
            item.increaseQualityBy(2);
        } else {
            item.increaseQuality();
        }
    }
}
