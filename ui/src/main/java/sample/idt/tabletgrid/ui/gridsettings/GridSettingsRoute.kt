package sample.idt.tabletgrid.ui.gridsettings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun GridSettingsRoute(
    onOpenGrid: (rows: Int, columns: Int) -> Unit,
) {
    val viewModel = koinViewModel<GridSettingsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.action.collect { action ->
            when (action) {
                is GridSettingsAction.OpenGrid -> onOpenGrid(action.rows, action.columns)
            }
        }
    }

    GridSettingsScreen(
        state = state,
        onEvent = viewModel::onEvent,
    )
}
