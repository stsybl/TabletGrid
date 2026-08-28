package sample.idt.tabletgrid.ui.gridviewer

import androidx.compose.runtime.Immutable

@Immutable
data class CellUiModel(
    val id: Int,
    val text: String,
    val selected: Boolean,
)
