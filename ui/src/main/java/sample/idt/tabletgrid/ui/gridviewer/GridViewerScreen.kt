package sample.idt.tabletgrid.ui.gridviewer

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sample.idt.tabletgrid.ui.R
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
) {
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
            contentAlignment = Alignment.TopCenter,
        ) {
            val horizontalInsets = TABLE_CONTENT_PADDING * 2 +
                CELL_SPACING * (state.columnCount - 1)
            val availableCellsWidth = (maxWidth - horizontalInsets).coerceAtLeast(0.dp)
            val cellWidth = (availableCellsWidth / state.columnCount).coerceIn(
                minimumValue = CELL_MIN_WIDTH,
                maximumValue = CELL_MAX_WIDTH,
            )
            val rowsCount = (state.cells.size + state.columnCount - 1) / state.columnCount
            val verticalInsets = TABLE_CONTENT_PADDING * 2 +
                CELL_SPACING * (rowsCount - 1).coerceAtLeast(0)
            val tableWidth = minOf(
                maxWidth,
                cellWidth * state.columnCount + horizontalInsets,
            )
            val tableHeight = minOf(
                maxHeight,
                TABLE_ROW_HEIGHT * rowsCount + verticalInsets,
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
                    columns = GridCells.Fixed(state.columnCount),
                    contentPadding = PaddingValues(TABLE_CONTENT_PADDING),
                    horizontalArrangement = Arrangement.spacedBy(CELL_SPACING),
                    verticalArrangement = Arrangement.spacedBy(CELL_SPACING),
                ) {
                    items(
                        items = state.cells,
                        key = { cell -> cell.id },
                        contentType = { GRID_CELL_CONTENT_TYPE },
                    ) { cell ->
                        SelectableCellItem(
                            modifier = Modifier.height(TABLE_ROW_HEIGHT),
                            text = cell.text,
                            selected = cell.selected,
                            onClick = { onCellClick(cell.id) },
                        )
                    }
                }
            }
        }
    }
}

private const val GRID_CELL_CONTENT_TYPE = "grid_cell"
private val CELL_MIN_WIDTH = 80.dp
private val CELL_MAX_WIDTH = 160.dp
private val CELL_SPACING = 8.dp
private val TABLE_ROW_HEIGHT = 56.dp
private val TABLE_CONTENT_PADDING = 8.dp
private val TABLE_TONAL_ELEVATION = 1.dp
private val TABLE_BOTTOM_PADDING = 16.dp
private val SCREEN_HORIZONTAL_PADDING = 16.dp

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
            state = GridViewerState.Preview(
                rowCount = 3,
                columnCount = 3,
                cells = listOf(
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
            ),
            onEvent = {},
        )
    }
}
