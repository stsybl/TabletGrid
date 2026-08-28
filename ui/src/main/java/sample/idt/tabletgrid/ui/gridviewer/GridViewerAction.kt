package sample.idt.tabletgrid.ui.gridviewer

sealed interface GridViewerAction {
    data object NavigateBack : GridViewerAction
}
