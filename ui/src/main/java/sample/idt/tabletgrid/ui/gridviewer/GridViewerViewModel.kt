package sample.idt.tabletgrid.ui.gridviewer

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class GridViewerViewModel(
    rowCount: Int,
    columnCount: Int,
) : ViewModel() {

    private val _state = MutableStateFlow(
        GridViewerState(
            rowCount = rowCount,
            columnCount = columnCount,
        )
    )
    val state: StateFlow<GridViewerState> = _state.asStateFlow()

    private val actionChannel = Channel<GridViewerAction>(Channel.BUFFERED)
    val action = actionChannel.receiveAsFlow()

    fun onEvent(event: GridViewerEvent) = Unit
}
