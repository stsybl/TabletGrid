package sample.idt.tabletgrid.ui.gridviewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import sample.idt.tabletgrid.domain.gridviewer.LoadGridDataUseCase

class GridViewerViewModel(
    private val loadGridDataUseCase: LoadGridDataUseCase,
    rowCount: Int,
    columnCount: Int,
) : ViewModel() {

    private val _state = MutableStateFlow<GridViewerState>(GridViewerState.Loading)
    val state: StateFlow<GridViewerState> = _state.asStateFlow()

    private val actionChannel = Channel<GridViewerAction>(Channel.BUFFERED)
    val action = actionChannel.receiveAsFlow()

    init {
        loadGrid(
            rowCount = rowCount,
            columnCount = columnCount,
        )
    }

    fun onEvent(event: GridViewerEvent) {
        when (event) {
            is GridViewerEvent.CellClicked -> processCellClick(event.id)
            GridViewerEvent.BackClicked -> processBackClick()
        }
    }

    private fun processCellClick(id: Int) {
        val currentState = state.value as? GridViewerState.Preview ?: return

        _state.value = currentState.copy(
            cells = currentState.cells.map { cell ->
                if (cell.id == id) {
                    cell.copy(selected = !cell.selected)
                } else {
                    cell
                }
            },
        )
    }

    private fun processBackClick() {
        actionChannel.trySend(GridViewerAction.NavigateBack)
    }

    private fun loadGrid(
        rowCount: Int,
        columnCount: Int,
    ) {
        viewModelScope.launch {
            val cells = loadGridDataUseCase.invoke(
                rowCount = rowCount,
                columnCount = columnCount,
            )
                .map { cell ->
                    CellUiModel(
                        id = cell.id,
                        text = cell.text,
                        selected = false,
                    )
                }
            _state.value = GridViewerState.Preview(
                rowCount = rowCount,
                columnCount = columnCount,
                cells = cells,
            )
        }
    }
}
