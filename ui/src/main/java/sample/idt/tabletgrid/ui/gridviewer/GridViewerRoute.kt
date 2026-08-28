package sample.idt.tabletgrid.ui.gridviewer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parameterArrayOf

@Composable
fun GridViewerRoute(
    rowCount: Int,
    columnCount: Int,
    onBack: () -> Unit,
) {
    val viewModel = koinViewModel<GridViewerViewModel>(
        parameters = { parameterArrayOf(rowCount, columnCount) },
    )
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.action.collect { action ->
            when (action) {
                GridViewerAction.NavigateBack -> onBack()
            }
        }
    }

    GridViewerScreen(
        state = state,
        onEvent = viewModel::onEvent,
    )
}
