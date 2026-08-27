package sample.idt.tabletgrid.ui.gridviewer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parameterArrayOf

@Composable
fun GridViewerRoute(
    rowCount: Int,
    columnCount: Int,
) {
    val viewModel = koinViewModel<GridViewerViewModel>(
        parameters = { parameterArrayOf(rowCount, columnCount) },
    )
    val state by viewModel.state.collectAsState()

    GridViewerScreen(
        state = state,
        onEvent = viewModel::onEvent,
    )
}
