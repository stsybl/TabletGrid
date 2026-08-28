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
    fun `generate returns two words when maximum words count is selected`() {
        val random = FakeRandom(
            values = listOf(
                2, // words count
                0,
                1,
            ),
        )
        val randomTextGenerator = RandomTextGenerator(random)

        val result = randomTextGenerator.generate()

        assertEquals(
            "apple table",
            result,
        )
    }

    @Test
    fun `generate returns expected words for selected random values`() {
        val random = FakeRandom(
            values = listOf(
                2,  // words count
                10, // dream
                20, // beach
            ),
        )
        val randomTextGenerator = RandomTextGenerator(random)

        val result = randomTextGenerator.generate()

        assertEquals(
            "dream beach",
            result,
        )
    }

    @Test
    fun `generate allows repeated words`() {
        val random = FakeRandom(
            values = listOf(
                2, // words count
                0,
                0,
            ),
        )
        val randomTextGenerator = RandomTextGenerator(random)

        val result = randomTextGenerator.generate()

        assertEquals(
            "apple apple",
            result,
        )
    }

    @Test
    fun `generate separates words with single spaces`() {
        val random = FakeRandom(
            values = listOf(
                2,
                0,
                1,
            ),
        )
        val randomTextGenerator = RandomTextGenerator(random)

        val result = randomTextGenerator.generate()

        assertEquals(
            "apple table",
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