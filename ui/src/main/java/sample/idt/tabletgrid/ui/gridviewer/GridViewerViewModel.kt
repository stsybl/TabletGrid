package sample.idt.tabletgrid.ui.gridviewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import sample.idt.tabletgrid.domain.gridviewer.LoadGridDataUseCase

class GridViewerViewModel(
    private val loadGridDataUseCase: LoadGridDataUseCase,
    private val defaultDispatcher: CoroutineDispatcher,
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
            is GridViewerEvent.CellDoubleClicked -> processCellDoubleClick(event.id)
            is GridViewerEvent.CellEditSaved -> processCellEditSave(id = event.id, text = event.text)
            GridViewerEvent.CellEditCancelled -> processCellEditCancel()
            GridViewerEvent.BackClicked -> processBackClick()
        }
    }

    private fun processCellClick(id: Int) {
        updatePreview { currentState ->
            val updatedCells = currentState.cells.updateCellByIndexedId(id) { cell ->
                cell.copy(selected = !cell.selected)
            } ?: return@updatePreview currentState

            currentState.copy(cells = updatedCells)
        }
    }

    private fun processCellDoubleClick(id: Int) {
        updatePreview { currentState ->
            val cell = currentState.cells.cellByIndexedId(id) ?: return@updatePreview currentState
            val editor = CellEditorUiModel(
                cellId = cell.id,
                text = cell.text,
            )
            if (currentState.editor == editor) currentState else currentState.copy(editor = editor)
        }
    }

    private fun processCellEditSave(
        id: Int,
        text: String,
    ) {
        updatePreview { currentState ->
            currentState.editor?.takeIf { editor -> editor.cellId == id }
                ?: return@updatePreview currentState
            val cell = currentState.cells.cellByIndexedId(id)
                ?: return@updatePreview currentState
            val updatedCells = if (cell.text == text) {
                currentState.cells
            } else {
                currentState.cells.replacingAt(id, cell.copy(text = text))
            }

            currentState.copy(
                cells = updatedCells,
                editor = null,
            )
        }
    }

    private fun processCellEditCancel() {
        updatePreview { currentState ->
            if (currentState.editor == null) currentState else currentState.copy(editor = null)
        }
    }

    private fun processBackClick() {
        actionChannel.trySend(GridViewerAction.NavigateBack)
    }

    private fun loadGrid(
        rowCount: Int,
        columnCount: Int,
    ) {
        viewModelScope.launch {
            val domainCells = loadGridDataUseCase.invoke(
                rowCount = rowCount,
                columnCount = columnCount,
            )
            val cells = withContext(defaultDispatcher) {
                domainCells.map { cell ->
                    CellUiModel(
                        id = cell.id,
                        text = cell.text,
                        selected = false,
                    )
                }
                    .toPersistentList()
            }
            _state.value = GridViewerState.Preview(
                rowCount = rowCount,
                columnCount = columnCount,
                cells = cells,
            )
        }
    }

    private inline fun updatePreview(
        transform: (GridViewerState.Preview) -> GridViewerState.Preview,
    ) {
        _state.update { currentState ->
            val preview = currentState as? GridViewerState.Preview ?: return@update currentState
            transform(preview)
        }
    }
}

private fun PersistentList<CellUiModel>.cellByIndexedId(id: Int): CellUiModel? =
    getOrNull(id)?.takeIf { cell -> cell.id == id }

private inline fun PersistentList<CellUiModel>.updateCellByIndexedId(
    id: Int,
    transform: (CellUiModel) -> CellUiModel,
): PersistentList<CellUiModel>? {
    val cell = cellByIndexedId(id) ?: return null
    return replacingAt(id, transform(cell))
}
