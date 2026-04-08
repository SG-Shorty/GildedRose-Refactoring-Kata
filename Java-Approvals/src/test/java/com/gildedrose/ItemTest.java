package com.gildedrose;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class ItemTest {

    private static final String NORMAL = "Normal Item";
    private static final String AGED_BRIE = "Aged Brie";
    private static final String BACKSTAGE =
        "Backstage passes to a TAFKAL80ETC concert";
    private static final String SULFURAS =
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
    void constructor_sets_fields_exactly_as_given(
        String name, int sellIn, int quality) {

        Item item = new Item(name, sellIn, quality);

        assertThat(item.remainingDays()).isEqualTo(sellIn);
        assertThat(item.toString())
            .isEqualTo(name + ", " + sellIn + ", " + quality);
    }

    @Test
    void constructor_throws_null_pointer_exception_for_null_name() {
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
    void isExpired_depends_only_on_remaining_days(
        int remainingDays, boolean expectedExpired) {

        Item item = new Item(NORMAL, remainingDays, 10);

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
    void decreaseRemainingDays_always_decrements_by_one(
        int start, int expected) {

        Item item = new Item(NORMAL, start, 10);

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
    void increaseQuality_respects_upper_bound(
        int startQuality, int expectedQuality) {

        Item item = new Item(AGED_BRIE, 5, startQuality);

        item.increaseQuality();

        assertThat(item.toString())
            .endsWith(", " + expectedQuality);
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
    void decreaseQuality_respects_lower_bound(
        int startQuality, int expectedQuality) {

        Item item = new Item(NORMAL, 5, startQuality);

        item.decreaseQuality();

        assertThat(item.toString())
            .endsWith(", " + expectedQuality);
    }

    /* -------------------------------------------------
       invalidate
       ------------------------------------------------- */

    @ParameterizedTest
    @CsvSource({ "0", "1", "42", "50" })
    void invalidate_sets_quality_to_zero_for_any_input(int startQuality) {

        Item item = new Item(NORMAL, 5, startQuality);

        item.invalidate();

        assertThat(item.toString())
            .endsWith(", 0");
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
    void update_normal_items_follow_standard_rules(
        int sellIn,
        int quality,
        int expectedSellIn,
        int expectedQuality) {

        Item item = new Item(NORMAL, sellIn, quality);

        item.update();

        assertThat(item.toString())
            .isEqualTo(NORMAL + ", " +
                expectedSellIn + ", " + expectedQuality);
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
    void update_aged_brie_increases_quality(
        int sellIn,
        int quality,
        int expectedSellIn,
        int expectedQuality) {

        Item item = new Item(AGED_BRIE, sellIn, quality);

        item.update();

        assertThat(item.toString())
            .isEqualTo(AGED_BRIE + ", " +
                expectedSellIn + ", " + expectedQuality);
    }

    /* -------------------------------------------------
       update – Backstage passes
       ------------------------------------------------- */

    static Stream<Arguments> backstagePassUpdateCases() {
        return Stream.of(
            Arguments.of(15, 20, 14, 21),
            Arguments.of(10, 20, 9, 22),
            Arguments.of(5, 20, 4, 23),
            Arguments.of(0, 20, -1, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("backstagePassUpdateCases")
    void update_backstage_passes_follow_concert_rules(
        int sellIn,
        int quality,
        int expectedSellIn,
        int expectedQuality) {

        Item item = new Item(BACKSTAGE, sellIn, quality);

        item.update();

        assertThat(item.toString())
            .isEqualTo(
                BACKSTAGE + ", " +
                    expectedSellIn + ", " + expectedQuality
            );
    }

    /* -------------------------------------------------
       update – Sulfuras
       ------------------------------------------------- */

    @Test
    void update_sulfuras_never_changes_state() {
        Item item = new Item(SULFURAS, 0, 80);

        item.update();

        assertThat(item.toString())
            .isEqualTo(SULFURAS + ", 0, 80");
    }
}
