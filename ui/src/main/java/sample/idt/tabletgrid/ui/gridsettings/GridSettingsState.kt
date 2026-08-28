package sample.idt.tabletgrid.ui.gridsettings

import sample.idt.tabletgrid.domain.gridsettings.GridSettingsValidationError

data class GridSettingsState(
    val rowsText: String,
    val columnsText: String,
    val rowsError: GridSettingsValidationError?,
    val columnsError: GridSettingsValidationError?,
) {
    val createEnabled: Boolean
        get() = rowsError == null &&
            columnsError == null &&
            rowsText.isNotBlank() &&
            columnsText.isNotBlank()
}
