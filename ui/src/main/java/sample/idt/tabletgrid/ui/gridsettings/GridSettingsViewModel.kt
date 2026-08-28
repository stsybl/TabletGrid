package sample.idt.tabletgrid.ui.gridsettings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import sample.idt.tabletgrid.domain.gridsettings.ValidateGridSettingsUseCase

class GridSettingsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val validateGridSettingsUseCase: ValidateGridSettingsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(
        createState(
            rows = savedStateHandle[ROWS_COUNT_KEY] ?: "",
            columns = savedStateHandle[COLUMNS_COUNT_KEY] ?: "",
        )
    )
    val state: StateFlow<GridSettingsState> = _state.asStateFlow()

    private val actionChannel = Channel<GridSettingsAction>(Channel.BUFFERED)
    val action = actionChannel.receiveAsFlow()

    fun onEvent(event: GridSettingsEvent) {
        when (event) {
            is GridSettingsEvent.RowsChanged -> processRowsChange(event.value)
            is GridSettingsEvent.ColumnsChanged -> processColumnsChange(event.value)
            GridSettingsEvent.CreateGridClicked -> processCreateGridClick()
        }
    }

    private fun processRowsChange(value: String) {
        updateGridSettings(rows = value)
    }

    private fun processColumnsChange(value: String) {
        updateGridSettings(columns = value)
    }

    private fun processCreateGridClick() {
        val currentState = state.value
        val validationResult = validateGridSettingsUseCase.invoke(
            rows = currentState.rowsText,
            columns = currentState.columnsText,
        )
        val rows = validationResult.rows.value
        val columns = validationResult.columns.value

        if (rows == null || columns == null || !validationResult.isValid) {
            _state.value = currentState.copy(
                rowsError = validationResult.rows.error,
                columnsError = validationResult.columns.error,
            )
            return
        }

        viewModelScope.launch {
            actionChannel.send(
                GridSettingsAction.OpenGrid(
                    rows = rows,
                    columns = columns,
                )
            )
        }
    }

    private fun updateGridSettings(
        rows: String = state.value.rowsText,
        columns: String = state.value.columnsText,
    ) {
        savedStateHandle[ROWS_COUNT_KEY] = rows
        savedStateHandle[COLUMNS_COUNT_KEY] = columns

        _state.value = createState(
            rows = rows,
            columns = columns,
        )
    }

    private fun createState(
        rows: String,
        columns: String,
    ): GridSettingsState {
        val validationResult = validateGridSettingsUseCase.invoke(
            rows = rows,
            columns = columns,
        )

        return GridSettingsState(
            rowsText = rows,
            columnsText = columns,
            rowsError = validationResult.rows.error,
            columnsError = validationResult.columns.error,
        )
    }

    private companion object {
        const val ROWS_COUNT_KEY = "grid_settings_rows_count"
        const val COLUMNS_COUNT_KEY = "grid_settings_columns_count"
    }
}
