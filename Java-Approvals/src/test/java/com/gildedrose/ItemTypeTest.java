package com.gildedrose;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class ItemTypeTest {

    private static final String AGED_BRIE_NAME = "Aged Brie";
    private static final String BACKSTAGE_PASSES_NAME = "Backstage passes to a TAFKAL80ETC concert";
    private static final String SULFURAS_NAME = "Sulfuras, Hand of Ragnaros";
    private static final String CONJURED_PREFIX = "Conjured ";

    @ParameterizedTest(name = "[{index}] {0} -> {1}")
    @CsvSource({
            "Aged Brie, AGED_BRIE",
            "Aged Brie Extra, AGED_BRIE",
            "Backstage passes to a TAFKAL80ETC concert, BACKSTAGE_PASSES",
            "Backstage passes to a TAFKAL80ETC concert VIP, BACKSTAGE_PASSES",
            "'Sulfuras, Hand of Ragnaros', SULFURAS",
            "Conjured Mana Cake, CONJURED",
            "'Conjured', CONJURED"
    })
    void FromNameShouldResolveKnownItemTypes(String itemName, ItemType expectedType) {
        assertThat(ItemType.fromName(itemName)).isEqualTo(expectedType);
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @ValueSource(strings = {
            "Elixir of the Mongoose",
            "Foo",
            "Conjuration Scroll",
            "conjured Mana Cake",
            " Conjured Mana Cake",
            "Random item"
    })
    void FromNameShouldReturnRegularForUnknownOrNonMatchingNames(String itemName) {
        assertThat(ItemType.fromName(itemName)).isEqualTo(ItemType.REGULAR);
    }

    @Test
    void FromNameShouldThrowWhenNameIsNull() {
        assertThrows(NullPointerException.class, () -> ItemType.fromName(null));
    }

    @ParameterizedTest(name = "[{index}] sellIn={0}, quality={1}")
    @CsvSource({
            "1, 0, 0, 1",
            "0, 0, -1, 2",
            "5, 49, 4, 50",
            "5, 50, 4, 50"
    })
    void UpdateQualityForAgedBrieShouldIncreaseQualityAndSellInAndRespectMaxQuality(
            int initialSellIn,
            int initialQuality,
            int expectedSellIn,
            int expectedQuality
    ) {
        Item item = new Item(AGED_BRIE_NAME, initialSellIn, initialQuality);

        ItemType.AGED_BRIE.updateQuality(item);

        assertThat(item.sellIn).isEqualTo(expectedSellIn);
        assertThat(item.quality).isEqualTo(expectedQuality);
    }

    @ParameterizedTest(name = "[{index}] sellIn={0}, quality={1}")
    @CsvSource({
            "11, 10, 10, 11",
            "10, 10, 9, 12",
            "5, 10, 4, 13",
            "0, 20, -1, 0",
            "5, 49, 4, 50",
            "1, 49, 0, 50"
    })
    void UpdateQualityForBackstagePassesShouldHandleAllSellInThresholds(
            int initialSellIn,
            int initialQuality,
            int expectedSellIn,
            int expectedQuality
    ) {
        Item item = new Item(BACKSTAGE_PASSES_NAME, initialSellIn, initialQuality);

        ItemType.BACKSTAGE_PASSES.updateQuality(item);

        assertThat(item.sellIn).isEqualTo(expectedSellIn);
        assertThat(item.quality).isEqualTo(expectedQuality);
    }

    @ParameterizedTest(name = "[{index}] sellIn={0}, quality={1}")
    @CsvSource({
            "0, 80, 0, 80",
            "-1, 80, -1, 80",
            "10, 80, 10, 80"
    })
    void UpdateQualityForSulfurasShouldNeverChangeItem(
            int initialSellIn,
            int initialQuality,
            int expectedSellIn,
            int expectedQuality
    ) {
        Item item = new Item(SULFURAS_NAME, initialSellIn, initialQuality);

        ItemType.SULFURAS.updateQuality(item);

        assertThat(item.sellIn).isEqualTo(expectedSellIn);
        assertThat(item.quality).isEqualTo(expectedQuality);
    }

    @ParameterizedTest(name = "[{index}] sellIn={0}, quality={1}")
    @CsvSource({
            "3, 6, 2, 4",
            "1, 1, 0, 0",
            "0, 6, -1, 2",
            "0, 1, -1, 0",
            "-1, 3, -2, 0"
    })
    void UpdateQualityForConjuredShouldDecreaseQualityAndRespectExpirationAndMinQuality(
            int initialSellIn,
            int initialQuality,
            int expectedSellIn,
            int expectedQuality
    ) {
        Item item = new Item(CONJURED_PREFIX + "Mana Cake", initialSellIn, initialQuality);

        ItemType.CONJURED.updateQuality(item);

        assertThat(item.sellIn).isEqualTo(expectedSellIn);
        assertThat(item.quality).isEqualTo(expectedQuality);
    }

    @ParameterizedTest(name = "[{index}] sellIn={0}, quality={1}")
    @CsvSource({
            "5, 7, 4, 6",
            "0, 7, -1, 5",
            "0, 1, -1, 0",
            "-1, 0, -2, 0",
            "10, 50, 9, 49"
    })
    void UpdateQualityForRegularShouldDecreaseQualityAndRespectExpirationAndMinQuality(
            int initialSellIn,
            int initialQuality,
            int expectedSellIn,
            int expectedQuality
    ) {
        Item item = new Item("Elixir of the Mongoose", initialSellIn, initialQuality);

        ItemType.REGULAR.updateQuality(item);

        assertThat(item.sellIn).isEqualTo(expectedSellIn);
        assertThat(item.quality).isEqualTo(expectedQuality);
    }

    @Test
    void SanityCheckEnumConstantsShouldBePresent() {
        assertThat(ItemType.values())
                .containsExactly(
                        ItemType.AGED_BRIE,
                        ItemType.BACKSTAGE_PASSES,
                        ItemType.SULFURAS,
                        ItemType.CONJURED,
                        ItemType.REGULAR
                );
    }
}
