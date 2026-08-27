package sample.idt.tabletgrid

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sample.idt.tabletgrid.domain.gridsettings.GridSettingsLimits
import sample.idt.tabletgrid.domain.gridsettings.GridSettingsValidationError
import sample.idt.tabletgrid.domain.gridsettings.ValidateGridSettingsUseCase

class ValidateGridSettingsUseCaseTest {

    private val validateGridSettingsUseCase = ValidateGridSettingsUseCase()

    @Test
    fun `invoke returns parsed values without errors when rows and columns are valid`() {
        val result = validateGridSettingsUseCase.invoke(
            rows = "100",
            columns = "4",
        )

        assertEquals(100, result.rows.value)
        assertNull(result.rows.error)

        assertEquals(4, result.columns.value)
        assertNull(result.columns.error)
    }

    @Test
    fun `invoke returns valid result when rows and columns are equal to minimum size`() {
        val result = validateGridSettingsUseCase.invoke(
            rows = "1",
            columns = "1",
        )

        assertEquals(1, result.rows.value)
        assertNull(result.rows.error)

        assertEquals(1, result.columns.value)
        assertNull(result.columns.error)
    }

    @Test
    fun `invoke returns valid result when rows and columns are equal to maximum size`() {
        val result = validateGridSettingsUseCase.invoke(
            rows = "1000",
            columns = "6",
        )

        assertEquals(1000, result.rows.value)
        assertNull(result.rows.error)

        assertEquals(6, result.columns.value)
        assertNull(result.columns.error)
    }

    @Test
    fun `invoke returns empty error when rows are empty`() {
        val result = validateGridSettingsUseCase.invoke(
            rows = "",
            columns = "4",
        )

        assertNull(result.rows.value)
        assertEquals(
            GridSettingsValidationError.Empty,
            result.rows.error,
        )

        assertEquals(4, result.columns.value)
        assertNull(result.columns.error)
    }

    @Test
    fun `invoke returns empty error when columns contain only spaces`() {
        val result = validateGridSettingsUseCase.invoke(
            rows = "100",
            columns = "   ",
        )

        assertEquals(100, result.rows.value)
        assertNull(result.rows.error)

        assertNull(result.columns.value)
        assertEquals(
            GridSettingsValidationError.Empty,
            result.columns.error,
        )
    }

    @Test
    fun `invoke returns invalid number error when rows are not a number`() {
        val result = validateGridSettingsUseCase.invoke(
            rows = "abc",
            columns = "4",
        )

        assertNull(result.rows.value)
        assertEquals(
            GridSettingsValidationError.InvalidNumber,
            result.rows.error,
        )

        assertEquals(4, result.columns.value)
        assertNull(result.columns.error)
    }

    @Test
    fun `invoke returns invalid number error when columns are not a number`() {
        val result = validateGridSettingsUseCase.invoke(
            rows = "100",
            columns = "1.5",
        )

        assertEquals(100, result.rows.value)
        assertNull(result.rows.error)

        assertNull(result.columns.value)
        assertEquals(
            GridSettingsValidationError.InvalidNumber,
            result.columns.error,
        )
    }

    @Test
    fun `invoke returns invalid number error when value is greater than Int maximum`() {
        val result = validateGridSettingsUseCase.invoke(
            rows = "999999999999999999999",
            columns = "4",
        )

        assertNull(result.rows.value)
        assertEquals(
            GridSettingsValidationError.InvalidNumber,
            result.rows.error,
        )
    }

    @Test
    fun `invoke returns below minimum error when rows are zero`() {
        val result = validateGridSettingsUseCase.invoke(
            rows = "0",
            columns = "4",
        )

        assertEquals(0, result.rows.value)
        assertEquals(
            GridSettingsValidationError.BelowMinimum(
                GridSettingsLimits.MIN_SIZE,
            ),
            result.rows.error,
        )

        assertEquals(4, result.columns.value)
        assertNull(result.columns.error)
    }

    @Test
    fun `invoke returns below minimum error when columns are zero`() {
        val result = validateGridSettingsUseCase.invoke(
            rows = "100",
            columns = "0",
        )

        assertEquals(100, result.rows.value)
        assertNull(result.rows.error)

        assertEquals(0, result.columns.value)
        assertEquals(
            GridSettingsValidationError.BelowMinimum(
                GridSettingsLimits.MIN_SIZE,
            ),
            result.columns.error,
        )
    }

    @Test
    fun `invoke returns above maximum error when rows exceed maximum size`() {
        val result = validateGridSettingsUseCase.invoke(
            rows = "1001",
            columns = "4",
        )

        assertEquals(1001, result.rows.value)
        assertEquals(
            GridSettingsValidationError.AboveMaximum(
                GridSettingsLimits.MAX_ROWS,
            ),
            result.rows.error,
        )

        assertEquals(4, result.columns.value)
        assertNull(result.columns.error)
    }

    @Test
    fun `invoke returns above maximum error when columns exceed maximum size`() {
        val result = validateGridSettingsUseCase.invoke(
            rows = "100",
            columns = "7",
        )

        assertEquals(100, result.rows.value)
        assertNull(result.rows.error)

        assertEquals(7, result.columns.value)
        assertEquals(
            GridSettingsValidationError.AboveMaximum(
                GridSettingsLimits.MAX_COLUMNS,
            ),
            result.columns.error,
        )
    }

    @Test
    fun `invoke returns validation errors for both fields when both values are invalid`() {
        val result = validateGridSettingsUseCase.invoke(
            rows = "",
            columns = "abc",
        )

        assertNull(result.rows.value)
        assertEquals(
            GridSettingsValidationError.Empty,
            result.rows.error,
        )

        assertNull(result.columns.value)
        assertEquals(
            GridSettingsValidationError.InvalidNumber,
            result.columns.error,
        )
    }

    @Test
    fun `invoke applies different maximum limits to rows and columns`() {
        val result = validateGridSettingsUseCase.invoke(
            rows = "7",
            columns = "7",
        )

        assertEquals(7, result.rows.value)
        assertNull(result.rows.error)

        assertEquals(7, result.columns.value)
        assertEquals(
            GridSettingsValidationError.AboveMaximum(
                GridSettingsLimits.MAX_COLUMNS,
            ),
            result.columns.error,
        )
    }
}