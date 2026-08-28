package sample.idt.tabletgrid.domain.gridviewer

class LoadGridDataUseCase(
    private val gridRepository: GridRepository,
) {
    suspend fun invoke(
        rowCount: Int,
        columnCount: Int,
    ): List<Cell> {
        return gridRepository.loadGrid(
            rowCount = rowCount,
            columnCount = columnCount,
        )
    }
}
