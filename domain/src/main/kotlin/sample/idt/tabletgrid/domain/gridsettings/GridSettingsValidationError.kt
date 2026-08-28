package sample.idt.tabletgrid.domain.gridsettings

sealed class GridSettingsValidationError {
    data object Empty : GridSettingsValidationError()
    data object InvalidNumber : GridSettingsValidationError()
    data class BelowMinimum(val minValue: Int) : GridSettingsValidationError()
    data class AboveMaximum(val maxValue: Int) : GridSettingsValidationError()
}
