package sample.idt.tabletgrid.ui.gridviewer

sealed interface GridViewerEvent {
    data class CellClicked(val id: Int) : GridViewerEvent
    data class CellDoubleClicked(val id: Int) : GridViewerEvent
    data class CellEditSaved(val id: Int, val text: String) : GridViewerEvent
    data object CellEditCancelled : GridViewerEvent
    data object BackClicked : GridViewerEvent
}
