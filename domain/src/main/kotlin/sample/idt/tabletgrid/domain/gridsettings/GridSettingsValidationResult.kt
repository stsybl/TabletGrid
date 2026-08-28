package sample.idt.tabletgrid.domain.gridsettings

data class GridSettingsValidationResult(
    val rows: GridSizeValidationResult,
    val columns: GridSizeValidationResult,
) {
    val isValid: Boolean
        get() = rows.error == null && columns.error == null
}

data class GridSizeValidationResult(
    val value: Int?,
    val error: GridSettingsValidationError?,
)
