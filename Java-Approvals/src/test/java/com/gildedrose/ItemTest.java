package com.gildedrose;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class ItemParameterizedTest {

    private static final String NormalItem = "Normal Item";
    private static final String AgedBrie = "Aged Brie";
    private static final String BackstagePasses =
        "Backstage passes to a TAFKAL80ETC concert";
    private static final String Sulfuras =
        "Sulfuras, Hand of Ragnaros";

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
    }

    @Test
    void constructorThrowsNullPointerExceptionForNullName() {
        assertThatThrownBy(() -> new Item(null, 5, 10))
            .isInstanceOf(NullPointerException.class);
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

        Item item = new Item(NormalItem, remainingDays, 10);

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

        Item item = new Item(NormalItem, start, 10);

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

        Item item = new Item(AgedBrie, 5, startQuality);

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

        Item item = new Item(NormalItem, 5, startQuality);

        item.decreaseQuality();

        assertThat(item.getQuality()).isEqualTo(expectedQuality);
    }

    /* -------------------------------------------------
       invalidate
       ------------------------------------------------- */

    @ParameterizedTest
    @CsvSource({ "0", "1", "42", "50" })
    void invalidateSetsQualityToZeroForAnyInput(int startQuality) {

        Item item = new Item(NormalItem, 5, startQuality);

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

        Item item = new Item(NormalItem, sellIn, quality);

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

        Item item = new Item(AgedBrie, sellIn, quality);

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

        Item item = new Item(BackstagePasses, sellIn, quality);

        item.update();

        assertThat(item.remainingDays()).isEqualTo(expectedSellIn);
        assertThat(item.getQuality()).isEqualTo(expectedQuality);
    }

    /* -------------------------------------------------
       update – Sulfuras
       ------------------------------------------------- */

    @Test
    void updateSulfurasNeverChangesState() {
        Item item = new Item(Sulfuras, 0, 80);

        item.update();

        assertThat(item.remainingDays()).isEqualTo(0);
        assertThat(item.getQuality()).isEqualTo(80);
    }
}
