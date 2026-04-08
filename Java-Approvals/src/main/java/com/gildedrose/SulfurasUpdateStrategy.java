package com.gildedrose;

public class SulfurasUpdateStrategy implements UpdateStrategy {

    static final SulfurasUpdateStrategy INSTANCE = new SulfurasUpdateStrategy();
    private SulfurasUpdateStrategy() {}


    @Override
    public void update(Item item) {
        //do nothing as sulfuras is legendary and doesn't change
    }
}
