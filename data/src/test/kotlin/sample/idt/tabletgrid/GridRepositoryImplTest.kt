package sample.idt.tabletgrid

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import sample.idt.tablegrid.data.RandomTextGenerator
import sample.idt.tabletgrid.data.gridviewer.GridRepositoryImpl
import sample.idt.tabletgrid.domain.gridviewer.Cell
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
class GridRepositoryImplTest {

    @Test
    fun `loads one or two generated words for every grid cell after delay`() = runTest {
        val randomTextGenerator = RandomTextGenerator(
            random = FakeRandom(
                values = listOf(
                    1, 0,
                    2, 1, 2,
                    1, 3,
                    2, 4, 5,
                ),
            ),
        )
        val repository = GridRepositoryImpl(
            randomTextGenerator = randomTextGenerator,
            ioDispatcher = StandardTestDispatcher(testScheduler),
        )

        val result = async {
            repository.loadGrid(
                rowCount = 2,
                columnCount = 2,
            )
        }

        assertFalse(result.isCompleted)
        advanceUntilIdle()

        assertEquals(2_000L, testScheduler.currentTime)
        assertEquals(
            listOf(
                Cell(id = 0, text = "apple"),
                Cell(id = 1, text = "table green"),
                Cell(id = 2, text = "water"),
                Cell(id = 3, text = "house cloud"),
            ),
            result.await(),
        )
    }

    private class FakeRandom(
        values: List<Int>,
    ) : Random() {

        private val values = values.iterator()

        override fun nextInt(
            from: Int,
            until: Int,
        ): Int = values.next()

        override fun nextBits(bitCount: Int): Int {
            error("nextBits should not be called")
        }
    }
}
