package sample.idt.tabletgrid.domain.gridsettings

class ValidateGridSettingsUseCase {

    fun invoke(
        rows: String,
        columns: String,
    ): GridSettingsValidationResult {
        return GridSettingsValidationResult(
            rows = validate(value = rows, maxValue = GridSettingsLimits.MAX_ROWS),
            columns = validate(value = columns, maxValue = GridSettingsLimits.MAX_COLUMNS),
        )
    }

    private fun validate(
        value: String,
        maxValue: Int,
    ): GridSizeValidationResult {
        if (value.isBlank()) {
            return GridSizeValidationResult(
                value = null,
                error = GridSettingsValidationError.Empty,
            )
        }

        val parsedValue = value.toIntOrNull()
            ?: return GridSizeValidationResult(
                value = null,
                error = GridSettingsValidationError.InvalidNumber,
            )

        val error = when {
            parsedValue < GridSettingsLimits.MIN_SIZE -> {
                GridSettingsValidationError.BelowMinimum(GridSettingsLimits.MIN_SIZE)
            }
            parsedValue > maxValue -> {
                GridSettingsValidationError.AboveMaximum(maxValue)
            }
            else -> null
        }

        return GridSizeValidationResult(
            value = parsedValue,
            error = error,
        )
    }
}
