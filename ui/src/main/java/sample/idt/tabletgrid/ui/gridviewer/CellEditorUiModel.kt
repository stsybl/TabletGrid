package sample.idt.tabletgrid.ui.gridviewer

import androidx.compose.runtime.Immutable

@Immutable
data class CellEditorUiModel(
    val cellId: Int,
    val text: String,
)
