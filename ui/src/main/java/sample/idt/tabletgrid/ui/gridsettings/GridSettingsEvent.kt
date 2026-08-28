package sample.idt.tabletgrid.ui.gridsettings

sealed interface GridSettingsEvent {
    data class RowsChanged(val value: String) : GridSettingsEvent
    data class ColumnsChanged(val value: String) : GridSettingsEvent
    data object CreateGridClicked : GridSettingsEvent
}
