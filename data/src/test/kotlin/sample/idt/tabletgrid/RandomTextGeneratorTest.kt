package sample.idt.tabletgrid

import org.junit.Assert.assertEquals
import org.junit.Test
import sample.idt.tablegrid.data.RandomTextGenerator
import kotlin.random.Random

class RandomTextGeneratorTest {

    @Test
    fun `generate returns one word when minimum words count is selected`() {
        val random = FakeRandom(
            values = listOf(
                1, // words count
                0, // apple
            ),
        )
        val randomTextGenerator = RandomTextGenerator(random)

        val result = randomTextGenerator.generate()

        assertEquals("apple", result)
    }

    @Test
    fun `generate returns five words when maximum words count is selected`() {
        val random = FakeRandom(
            values = listOf(
                5, // words count
                0,
                1,
                2,
                3,
                4,
            ),
        )
        val randomTextGenerator = RandomTextGenerator(random)

        val result = randomTextGenerator.generate()

        assertEquals(
            "apple table green water house",
            result,
        )
    }

    @Test
    fun `generate returns expected words for selected random values`() {
        val random = FakeRandom(
            values = listOf(
                3,  // words count
                10, // dream
                20, // beach
                30, // forest
            ),
        )
        val randomTextGenerator = RandomTextGenerator(random)

        val result = randomTextGenerator.generate()

        assertEquals(
            "dream beach forest",
            result,
        )
    }

    @Test
    fun `generate allows repeated words`() {
        val random = FakeRandom(
            values = listOf(
                3, // words count
                0,
                0,
                0,
            ),
        )
        val randomTextGenerator = RandomTextGenerator(random)

        val result = randomTextGenerator.generate()

        assertEquals(
            "apple apple apple",
            result,
        )
    }

    @Test
    fun `generate separates words with single spaces`() {
        val random = FakeRandom(
            values = listOf(
                3,
                0,
                1,
                2,
            ),
        )
        val randomTextGenerator = RandomTextGenerator(random)

        val result = randomTextGenerator.generate()

        assertEquals(
            "apple table green",
            result,
        )
    }

    private class FakeRandom(
        values: List<Int>,
    ) : Random() {

        private val values = values.iterator()

        override fun nextInt(
            from: Int,
            until: Int,
        ): Int {
            return values.next()
        }

        override fun nextBits(bitCount: Int): Int {
            error("nextBits should not be called")
        }
    }
}