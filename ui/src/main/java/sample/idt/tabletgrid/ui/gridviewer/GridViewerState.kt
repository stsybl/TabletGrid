package sample.idt.tabletgrid.ui.gridviewer

sealed interface GridViewerState {

    data object Loading : GridViewerState

    data class Preview(
        val rowCount: Int,
        val columnCount: Int,
        val cells: List<CellUiModel>,
    ) : GridViewerState
}
