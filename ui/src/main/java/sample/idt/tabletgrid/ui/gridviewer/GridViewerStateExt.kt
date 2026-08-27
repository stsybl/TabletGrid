package sample.idt.tabletgrid.ui.gridviewer

fun GridViewerState.Preview.getItemByPosition(
    rowIndex: Int,
    columnIndex: Int,
): String {
    require(rowIndex in 0 until rowCount) {
        "rowIndex must be in 0 until $rowCount, but was $rowIndex"
    }
    require(columnIndex in 0 until columnCount) {
        "columnIndex must be in 0 until $columnCount, but was $columnIndex"
    }

    return cells[rowIndex * columnCount + columnIndex]
}
