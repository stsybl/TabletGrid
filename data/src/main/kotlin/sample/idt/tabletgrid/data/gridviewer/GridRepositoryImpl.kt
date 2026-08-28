package sample.idt.tabletgrid.data.gridviewer

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import sample.idt.tablegrid.data.RandomTextGenerator
import sample.idt.tabletgrid.domain.gridviewer.Cell
import sample.idt.tabletgrid.domain.gridviewer.GridRepository
import kotlin.time.Duration.Companion.milliseconds

class GridRepositoryImpl(
    private val randomTextGenerator: RandomTextGenerator,
    private val defaultDispatcher: CoroutineDispatcher,
) : GridRepository {

    override suspend fun loadGrid(
        rowCount: Int,
        columnCount: Int,
    ): List<Cell> = withContext(defaultDispatcher) {
        delay(GRID_LOADING_DELAY_MILLIS.milliseconds)

        List(rowCount * columnCount) { index ->
            Cell(
                id = index,
                text = randomTextGenerator.generate(),
            )
        }
    }

    private companion object {
        const val GRID_LOADING_DELAY_MILLIS = 2_000L
    }
}
