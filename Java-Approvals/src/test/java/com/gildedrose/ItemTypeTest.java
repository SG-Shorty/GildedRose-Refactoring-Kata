package com.gildedrose;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ItemTypeTest {

    public static final String AGED_BRIE_NAME = "Aged Brie";
    public static final String BACKSTAGE_PASSES_NAME = "Backstage passes to a TAFKAL80ETC concert";
    public static final String SULFURAS_NAME = "Sulfuras, Hand of Ragnaros";

    @Test
    void FromNameShouldReturnAgedBrieForExactMatch() {
        assertThat(ItemType.fromName(AGED_BRIE_NAME)).isEqualTo(ItemType.AGED_BRIE);
    }

    @Test
    void FromNameShouldReturnBackstagePassesForExactMatch() {
        assertThat(ItemType.fromName(BACKSTAGE_PASSES_NAME))
                .isEqualTo(ItemType.BACKSTAGE_PASSES);
    }

    @Test
    void FromNameShouldReturnSulfurasForExactMatch() {
        assertThat(ItemType.fromName(SULFURAS_NAME)).isEqualTo(ItemType.SULFURAS);
    }

    @Test
    void FromNameShouldReturnConjuredWhenNameStartsWithConjuredPrefix() {
        assertThat(ItemType.fromName("Conjured Mana Cake")).isEqualTo(ItemType.CONJURED);
    }

    @Test
    void FromNameShouldReturnConjuredWhenNameIsExactlyConjuredPrefix() {
        assertThat(ItemType.fromName("Conjured ")).isEqualTo(ItemType.CONJURED);
    }

    @Test
    void FromNameShouldReturnRegularForUnknownItem() {
        assertThat(ItemType.fromName("Elixir of the Mongoose")).isEqualTo(ItemType.REGULAR);
    }

    @Test
    void FromNameShouldReturnRegularForNonMatchingConjuredLikeName() {
        assertThat(ItemType.fromName("Conjuration Scroll")).isEqualTo(ItemType.REGULAR);
    }

    @Test
    void FromNameShouldThrowWhenNameIsNull() {
        assertThrows(NullPointerException.class, () -> ItemType.fromName(null));
    }

    @Test
    void UpdateQualityForAgedBrieShouldIncreaseQualityAndSellInAndIncreaseAgainWhenExpired() {
        Item item = new Item(AGED_BRIE_NAME, 0, 0);

        ItemType.AGED_BRIE.updateQuality(item);

        assertThat(item.sellIn).isEqualTo(-1);
        assertThat(item.quality).isEqualTo(2);
    }

    @Test
    void UpdateQualityForAgedBrieShouldNotExceedMaxQuality() {
        Item item = new Item(AGED_BRIE_NAME, 5, 50);

        ItemType.AGED_BRIE.updateQuality(item);

        assertThat(item.sellIn).isEqualTo(4);
        assertThat(item.quality).isEqualTo(50);
    }

    @Test
    void UpdateQualityForBackstagePassesShouldIncreaseQualityByOneWhenSellInIsAboveTen() {
        Item item = new Item(BACKSTAGE_PASSES_NAME, 11, 10);

        ItemType.BACKSTAGE_PASSES.updateQuality(item);

        assertThat(item.sellIn).isEqualTo(10);
        assertThat(item.quality).isEqualTo(11);
    }

    @Test
    void UpdateQualityForBackstagePassesShouldIncreaseQualityByTwoWhenSellInIsTenOrLess() {
        Item item = new Item(BACKSTAGE_PASSES_NAME, 10, 10);

        ItemType.BACKSTAGE_PASSES.updateQuality(item);

        assertThat(item.sellIn).isEqualTo(9);
        assertThat(item.quality).isEqualTo(12);
    }

    @Test
    void UpdateQualityForBackstagePassesShouldIncreaseQualityByThreeWhenSellInIsFiveOrLess() {
        Item item = new Item(BACKSTAGE_PASSES_NAME, 5, 10);

        ItemType.BACKSTAGE_PASSES.updateQuality(item);

        assertThat(item.sellIn).isEqualTo(4);
        assertThat(item.quality).isEqualTo(13);
    }

    @Test
    void UpdateQualityForBackstagePassesShouldSetQualityToMinAfterExpiration() {
        Item item = new Item(BACKSTAGE_PASSES_NAME, 0, 20);

        ItemType.BACKSTAGE_PASSES.updateQuality(item);

        assertThat(item.sellIn).isEqualTo(-1);
        assertThat(item.quality).isEqualTo(0);
    }

    @Test
    void UpdateQualityForBackstagePassesShouldNotExceedMaxQualityBeforeExpiration() {
        Item item = new Item(BACKSTAGE_PASSES_NAME, 5, 49);

        ItemType.BACKSTAGE_PASSES.updateQuality(item);

        assertThat(item.sellIn).isEqualTo(4);
        assertThat(item.quality).isEqualTo(50);
    }

    @Test
    void UpdateQualityForSulfurasShouldNotChangeItem() {
        Item item = new Item(SULFURAS_NAME, 0, 80);

        ItemType.SULFURAS.updateQuality(item);

        assertThat(item.sellIn).isEqualTo(0);
        assertThat(item.quality).isEqualTo(80);
    }

    @Test
    void UpdateQualityForConjuredShouldDecreaseQualityByTwoAndSellInByOne() {
        Item item = new Item("Conjured Mana Cake", 3, 6);

        ItemType.CONJURED.updateQuality(item);

        assertThat(item.sellIn).isEqualTo(2);
        assertThat(item.quality).isEqualTo(4);
    }

    @Test
    void UpdateQualityForConjuredShouldDecreaseQualityByFourWhenExpired() {
        Item item = new Item("Conjured Mana Cake", 0, 6);

        ItemType.CONJURED.updateQuality(item);

        assertThat(item.sellIn).isEqualTo(-1);
        assertThat(item.quality).isEqualTo(2);
    }

    @Test
    void UpdateQualityForConjuredShouldNotGoBelowMinQuality() {
        Item item = new Item("Conjured Mana Cake", 0, 1);

        ItemType.CONJURED.updateQuality(item);

        assertThat(item.sellIn).isEqualTo(-1);
        assertThat(item.quality).isEqualTo(0);
    }

    @Test
    void UpdateQualityForRegularShouldDecreaseQualityByOneAndSellInByOne() {
        Item item = new Item("Elixir of the Mongoose", 5, 7);

        ItemType.REGULAR.updateQuality(item);

        assertThat(item.sellIn).isEqualTo(4);
        assertThat(item.quality).isEqualTo(6);
    }

    @Test
    void UpdateQualityForRegularShouldDecreaseQualityByTwoWhenExpired() {
        Item item = new Item("Elixir of the Mongoose", 0, 7);

        ItemType.REGULAR.updateQuality(item);

        assertThat(item.sellIn).isEqualTo(-1);
        assertThat(item.quality).isEqualTo(5);
    }

    @Test
    void UpdateQualityForRegularShouldNotGoBelowMinQuality() {
        Item item = new Item("Elixir of the Mongoose", 0, 0);

        ItemType.REGULAR.updateQuality(item);

        assertThat(item.sellIn).isEqualTo(-1);
        assertThat(item.quality).isEqualTo(0);
    }
}
