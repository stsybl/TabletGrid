package sample.idt.tabletgrid.ui.gridviewer

sealed interface GridViewerEvent {
    data class CellClicked(val id: Int) : GridViewerEvent
    data object BackClicked : GridViewerEvent
}
