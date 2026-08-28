package sample.idt.tabletgrid.ui.gridviewer

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import sample.idt.tabletgrid.ui.R
import sample.idt.tabletgrid.ui.components.GridCellHeight
import sample.idt.tabletgrid.ui.components.SelectableCellItem
import sample.idt.tabletgrid.ui.theme.TabletGridTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GridViewerScreen(
    state: GridViewerState,
    onEvent: (GridViewerEvent) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.grid_viewer_title))
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onEvent(GridViewerEvent.BackClicked) },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.grid_viewer_back),
                        )
                    }
                },
            )
        },
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
        ) {
            when (state) {
                GridViewerState.Loading -> LoadingContent(
                    modifier = Modifier.align(Alignment.Center),
                )
                is GridViewerState.Preview -> GridPreview(
                    state = state,
                    onCellClick = { id ->
                        onEvent(GridViewerEvent.CellClicked(id))
                    },
                    onCellDoubleClick = { id ->
                        onEvent(GridViewerEvent.CellDoubleClicked(id))
                    },
                    onCellEditSave = { id, text ->
                        onEvent(GridViewerEvent.CellEditSaved(id = id, text = text))
                    },
                    onCellEditCancel = {
                        onEvent(GridViewerEvent.CellEditCancelled)
                    },
                )
            }
        }
    }
}

@Composable
private fun LoadingContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        CircularProgressIndicator()
        Text(
            text = stringResource(R.string.grid_viewer_loading),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun GridPreview(
    state: GridViewerState.Preview,
    onCellClick: (id: Int) -> Unit,
    onCellDoubleClick: (id: Int) -> Unit,
    onCellEditSave: (id: Int, text: String) -> Unit,
    onCellEditCancel: () -> Unit,
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SCREEN_HORIZONTAL_PADDING)
                .padding(top = 8.dp, bottom = 16.dp),
            text = stringResource(
                R.string.grid_viewer_description,
                state.rowCount,
                state.columnCount,
            ),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = SCREEN_HORIZONTAL_PADDING)
                .padding(bottom = TABLE_BOTTOM_PADDING),
        ) {
            val editor = state.editor
            if (editor != null && isLandscape) {
                val editorWidth = (maxWidth * EDITOR_PANE_WIDTH_FRACTION).coerceIn(
                    minimumValue = EDITOR_PANE_MIN_WIDTH,
                    maximumValue = EDITOR_PANE_MAX_WIDTH,
                )

                LandscapeGridLayout(
                    modifier = Modifier.fillMaxSize(),
                    state = state,
                    editor = editor,
                    editorWidth = editorWidth,
                    onCellClick = onCellClick,
                    onCellDoubleClick = onCellDoubleClick,
                    onCellEditSave = onCellEditSave,
                    onCellEditCancel = onCellEditCancel,
                )
            } else {
                PortraitGridLayout(
                    modifier = Modifier.fillMaxSize(),
                    state = state,
                    editor = editor,
                    onCellClick = onCellClick,
                    onCellDoubleClick = onCellDoubleClick,
                    onCellEditSave = onCellEditSave,
                    onCellEditCancel = onCellEditCancel,
                )
            }
        }
    }
}

@Composable
private fun LandscapeGridLayout(
    state: GridViewerState.Preview,
    editor: CellEditorUiModel,
    editorWidth: Dp,
    onCellClick: (id: Int) -> Unit,
    onCellDoubleClick: (id: Int) -> Unit,
    onCellEditSave: (id: Int, text: String) -> Unit,
    onCellEditCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(EDITOR_PANEL_SPACING),
    ) {
        GridTable(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            cells = state.cells,
            columnCount = state.columnCount,
            onCellClick = onCellClick,
            onCellDoubleClick = onCellDoubleClick,
        )
        ActiveCellEditor(
            modifier = Modifier
                .width(editorWidth)
                .fillMaxHeight(),
            editor = editor,
            cellCount = state.cells.size,
            columnCount = state.columnCount,
            onSave = onCellEditSave,
            onCancel = onCellEditCancel,
        )
    }
}

@Composable
private fun PortraitGridLayout(
    state: GridViewerState.Preview,
    editor: CellEditorUiModel?,
    onCellClick: (id: Int) -> Unit,
    onCellDoubleClick: (id: Int) -> Unit,
    onCellEditSave: (id: Int, text: String) -> Unit,
    onCellEditCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(EDITOR_PANEL_SPACING),
    ) {
        GridTable(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            cells = state.cells,
            columnCount = state.columnCount,
            onCellClick = onCellClick,
            onCellDoubleClick = onCellDoubleClick,
        )
        if (editor != null) {
            ActiveCellEditor(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding(),
                editor = editor,
                cellCount = state.cells.size,
                columnCount = state.columnCount,
                onSave = onCellEditSave,
                onCancel = onCellEditCancel,
            )
        }
    }
}

@Composable
private fun GridTable(
    cells: ImmutableList<CellUiModel>,
    columnCount: Int,
    onCellClick: (id: Int) -> Unit,
    onCellDoubleClick: (id: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter,
    ) {
        val horizontalInsets = TABLE_CONTENT_PADDING * 2 +
            CELL_SPACING * (columnCount - 1)
        val availableCellsWidth = (maxWidth - horizontalInsets).coerceAtLeast(0.dp)
        val cellWidth = (availableCellsWidth / columnCount).coerceIn(
            minimumValue = CELL_MIN_WIDTH,
            maximumValue = CELL_MAX_WIDTH,
        )
        val rowsCount = (cells.size + columnCount - 1) / columnCount
        val verticalInsets = TABLE_CONTENT_PADDING * 2 +
            CELL_SPACING * (rowsCount - 1).coerceAtLeast(0)
        val tableWidth = minOf(
            maxWidth,
            cellWidth * columnCount + horizontalInsets,
        )
        val tableHeight = minOf(
            maxHeight,
            GridCellHeight * rowsCount + verticalInsets,
        )

        Surface(
            modifier = Modifier
                .width(tableWidth)
                .height(tableHeight),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            tonalElevation = TABLE_TONAL_ELEVATION,
        ) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(columnCount),
                contentPadding = PaddingValues(TABLE_CONTENT_PADDING),
                horizontalArrangement = Arrangement.spacedBy(CELL_SPACING),
                verticalArrangement = Arrangement.spacedBy(CELL_SPACING),
            ) {
                items(
                    items = cells,
                    key = { cell -> cell.id },
                    contentType = { GRID_CELL_CONTENT_TYPE },
                ) { cell ->
                    SelectableCellItem(
                        modifier = Modifier.height(GridCellHeight),
                        text = cell.text,
                        selected = cell.selected,
                        onClick = { onCellClick(cell.id) },
                        onDoubleClick = { onCellDoubleClick(cell.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun ActiveCellEditor(
    editor: CellEditorUiModel,
    cellCount: Int,
    columnCount: Int,
    onSave: (id: Int, text: String) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cellIndex = editor.cellId.takeIf { index -> index in 0 until cellCount } ?: return

    CellEditorPanel(
        modifier = modifier,
        editor = editor,
        rowIndex = cellIndex / columnCount,
        columnIndex = cellIndex % columnCount,
        onSave = { text -> onSave(editor.cellId, text) },
        onCancel = onCancel,
    )
}

private const val GRID_CELL_CONTENT_TYPE = "grid_cell"
private const val EDITOR_PANE_WIDTH_FRACTION = 0.36f
private val CELL_MIN_WIDTH = 80.dp
private val CELL_MAX_WIDTH = 160.dp
private val CELL_SPACING = 8.dp
private val TABLE_CONTENT_PADDING = 8.dp
private val TABLE_TONAL_ELEVATION = 1.dp
private val TABLE_BOTTOM_PADDING = 16.dp
private val SCREEN_HORIZONTAL_PADDING = 16.dp
private val EDITOR_PANEL_SPACING = 16.dp
private val EDITOR_PANE_MIN_WIDTH = 280.dp
private val EDITOR_PANE_MAX_WIDTH = 360.dp

@Preview(
    showBackground = true,
    widthDp = 800,
    heightDp = 600,
)
@Composable
private fun GridViewerScreenLightPreview() {
    TabletGridTheme(darkTheme = false) {
        GridViewerScreen(
            state = GridViewerState.Loading,
            onEvent = {},
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 800,
    heightDp = 600,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun GridViewerScreenDarkPreview() {
    TabletGridTheme(darkTheme = true) {
        GridViewerScreen(
            state = previewGridState(),
            onEvent = {},
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 600,
    heightDp = 800,
)
@Composable
private fun GridViewerScreenPortraitEditorPreview() {
    TabletGridTheme(darkTheme = false) {
        GridViewerScreen(
            state = previewGridState(),
            onEvent = {},
        )
    }
}

private fun previewGridState() = GridViewerState.Preview(
    rowCount = 3,
    columnCount = 3,
    cells = persistentListOf(
        CellUiModel(id = 0, text = "one", selected = false),
        CellUiModel(id = 1, text = "two", selected = true),
        CellUiModel(id = 2, text = "three", selected = false),
        CellUiModel(id = 3, text = "four", selected = false),
        CellUiModel(id = 4, text = "five", selected = true),
        CellUiModel(id = 5, text = "six", selected = false),
        CellUiModel(id = 6, text = "seven", selected = false),
        CellUiModel(id = 7, text = "eight", selected = false),
        CellUiModel(id = 8, text = "nine", selected = false),
    ),
    editor = CellEditorUiModel(cellId = 4, text = "five"),
)
