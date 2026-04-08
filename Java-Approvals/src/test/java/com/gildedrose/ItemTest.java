package com.gildedrose;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class ItemTest {

    private static final String NORMAL_ITEM_NAME = "Normal Item";

    /* -------------------------------------------------
       Constructor
       ------------------------------------------------- */

    @ParameterizedTest
    @CsvSource({
        "Normal Item, 10, 20",
        "Normal Item, 0, 0",
        "Normal Item, -5, 7",
        "Aged Brie, 3, 49"
    })
    void constructorSetsFieldsExactlyAsGiven(
        String name, int sellIn, int quality) {

        Item item = new Item(name, sellIn, quality);

        assertThat(item.remainingDays()).isEqualTo(sellIn);
        assertThat(item.getQuality()).isEqualTo(quality);
        assertThat(item.toString())
            .isEqualTo(name + ", " + sellIn + ", " + quality);
    }


    @Test
    void constructorThrowsNullPointerExceptionForNullName() {
        assertThatThrownBy(() -> new Item(null, 5, 10))
            .isInstanceOf(NullPointerException.class);
    }

    /* -------------------------------------------------
   constructor – quality bounds
   ------------------------------------------------- */

    @ParameterizedTest
    @CsvSource({
        "Normal Item, 5, -10, 0",
        "Normal Item, 5, -1, 0",
        "Normal Item, 5, 51, 50",
        "Normal Item, 5, 999, 50",
        "Aged Brie, 5, 60, 50"
    })
    void constructorClampsQualityToValidRange(
        String name,
        int sellIn,
        int inputQuality,
        int expectedQuality) {

        Item item = new Item(name, sellIn, inputQuality);

        assertThat(item.getQuality()).isEqualTo(expectedQuality);
    }

    /* -------------------------------------------------
   constructor – Sulfuras quality
   ------------------------------------------------- */

    @ParameterizedTest
    @CsvSource({
        "-10",
        "0",
        "10",
        "50",
        "79",
        "80",
        "81",
        "999"
    })
    void sulfurasHasQualityEightyRegardlessOfConstructorInput(int inputQuality) {

        Item item = new Item(ItemType.SULFURAS.itemName(), 10, inputQuality);

        assertThat(item.getQuality()).isEqualTo(80);
    }


    /* -------------------------------------------------
       isExpired
       ------------------------------------------------- */

    @ParameterizedTest
    @CsvSource({
        "1, false",
        "0, false",
        "-1, true",
        "-100, true"
    })
    void isExpiredDependsOnlyOnRemainingDays(
        int remainingDays, boolean expectedExpired) {

        Item item = new Item(NORMAL_ITEM_NAME, remainingDays, 10);

        assertThat(item.isExpired()).isEqualTo(expectedExpired);
    }

    /* -------------------------------------------------
       decreaseRemainingDays
       ------------------------------------------------- */

    @ParameterizedTest
    @CsvSource({
        "10, 9",
        "0, -1",
        "-5, -6"
    })
    void decreaseRemainingDaysAlwaysDecrementsByOne(
        int start, int expected) {

        Item item = new Item(NORMAL_ITEM_NAME, start, 10);

        item.decreaseRemainingDays();

        assertThat(item.remainingDays()).isEqualTo(expected);
    }

    /* -------------------------------------------------
       increaseQuality
       ------------------------------------------------- */

    @ParameterizedTest
    @CsvSource({
        "0, 1",
        "25, 26",
        "49, 50",
        "50, 50"
    })
    void increaseQualityRespectsUpperBound(
        int startQuality, int expectedQuality) {

        Item item = new Item(ItemType.AGED_BRIE.itemName(), 5, startQuality);

        item.increaseQuality();

        assertThat(item.getQuality()).isEqualTo(expectedQuality);
    }

    /* -------------------------------------------------
       decreaseQuality
       ------------------------------------------------- */

    @ParameterizedTest
    @CsvSource({
        "50, 49",
        "1, 0",
        "0, 0"
    })
    void decreaseQualityRespectsLowerBound(
        int startQuality, int expectedQuality) {

        Item item = new Item(NORMAL_ITEM_NAME, 5, startQuality);

        item.decreaseQuality();

        assertThat(item.getQuality()).isEqualTo(expectedQuality);
    }

    /* -------------------------------------------------
       invalidate
       ------------------------------------------------- */

    @ParameterizedTest
    @CsvSource({ "0", "1", "42", "50" })
    void invalidateSetsQualityToZeroForAnyInput(int startQuality) {

        Item item = new Item(NORMAL_ITEM_NAME, 5, startQuality);

        item.invalidate();

        assertThat(item.getQuality()).isZero();
    }

    /* -------------------------------------------------
       update – normal items
       ------------------------------------------------- */

    @ParameterizedTest
    @CsvSource({
        "10, 20, 9, 19",
        "0, 20, -1, 18",
        "-1, 1, -2, 0"
    })
    void updateNormalItemsFollowStandardRules(
        int sellIn,
        int quality,
        int expectedSellIn,
        int expectedQuality) {

        Item item = new Item(NORMAL_ITEM_NAME, sellIn, quality);

        item.update();

        assertThat(item.remainingDays()).isEqualTo(expectedSellIn);
        assertThat(item.getQuality()).isEqualTo(expectedQuality);
    }

    /* -------------------------------------------------
       update – Aged Brie
       ------------------------------------------------- */

    @ParameterizedTest
    @CsvSource({
        "10, 20, 9, 21",
        "0, 20, -1, 22",
        "-1, 49, -2, 50"
    })
    void updateAgedBrieIncreasesQuality(
        int sellIn,
        int quality,
        int expectedSellIn,
        int expectedQuality) {

        Item item = new Item(ItemType.AGED_BRIE.itemName(), sellIn, quality);

        item.update();

        assertThat(item.remainingDays()).isEqualTo(expectedSellIn);
        assertThat(item.getQuality()).isEqualTo(expectedQuality);
    }

    /* -------------------------------------------------
       update – Backstage passes
       ------------------------------------------------- */

    static Stream<Arguments> backstagePassUpdateCases() {
        return Stream.of(
            // normal increases
            Arguments.of(15, 20, 14, 21),
            Arguments.of(10, 20, 9, 22),
            Arguments.of(5, 20, 4, 23),

            // upper-bound protection
            Arguments.of(10, 49, 9, 50),
            Arguments.of(6, 49, 5, 50),
            Arguments.of(5, 48, 4, 50),

            // after concert
            Arguments.of(0, 20, -1, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("backstagePassUpdateCases")
    void updateBackstagePassesFollowConcertRulesAndRespectUpperBound(
        int sellIn,
        int quality,
        int expectedSellIn,
        int expectedQuality) {

        Item item = new Item(ItemType.BACKSTAGE_PASSES.itemName(), sellIn, quality);

        item.update();

        assertThat(item.remainingDays()).isEqualTo(expectedSellIn);
        assertThat(item.getQuality()).isEqualTo(expectedQuality);
    }

    /* -------------------------------------------------
       update – Sulfuras
       ------------------------------------------------- */

    @Test
    void updateSulfurasNeverChangesState() {
        Item item = new Item(ItemType.SULFURAS.itemName(), 0, 80);

        item.update();

        assertThat(item.remainingDays()).isEqualTo(0);
        assertThat(item.getQuality()).isEqualTo(80);
    }


    @Test
    void increaseQualityByDoesNotGoBelowZero() {
        Item item = new Item("Normal Item", 5, 0);

        item.increaseQualityBy(-10);

        assertThat(item.getQuality()).isZero();
    }

    @Test
    void increaseQualityByDoesNotExceedMaximum() {
        Item item = new Item("Aged Brie", 5, 49);

        item.increaseQualityBy(10);

        assertThat(item.getQuality()).isEqualTo(50);
    }
}
