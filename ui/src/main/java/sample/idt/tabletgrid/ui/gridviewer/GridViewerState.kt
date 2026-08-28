package sample.idt.tabletgrid.ui.gridviewer

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.PersistentList

@Immutable
sealed interface GridViewerState {

    data object Loading : GridViewerState

    data class Preview(
        val rowCount: Int,
        val columnCount: Int,
        val cells: PersistentList<CellUiModel>,
        val editor: CellEditorUiModel? = null,
    ) : GridViewerState
}
