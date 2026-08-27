package sample.idt.tabletgrid.domain.gridviewer

interface GridRepository {

    suspend fun loadGrid(
        rowCount: Int,
        columnCount: Int,
    ): List<String>
}
