package sample.idt.tabletgrid.ui.gridsettings

sealed interface GridSettingsAction {
    data class OpenGrid(val rows: Int, val columns: Int) : GridSettingsAction
}
