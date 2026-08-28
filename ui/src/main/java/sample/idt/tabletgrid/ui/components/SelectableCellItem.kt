package sample.idt.tabletgrid.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun SelectableCellItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    onDoubleClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = GridCellHeight)
            .combinedClickable(
                role = Role.Checkbox,
                onClick = onClick,
                onDoubleClick = onDoubleClick,
            ),
        color = if (selected) {
            MaterialTheme.colorScheme.tertiaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerLow
        },
        contentColor = if (selected) {
            MaterialTheme.colorScheme.onTertiaryContainer
        } else {
            MaterialTheme.colorScheme.onSurface
        },
        shape = MaterialTheme.shapes.small,
        tonalElevation = CELL_TONAL_ELEVATION,
        border = BorderStroke(
            width = CELL_BORDER_WIDTH,
            color = if (selected) {
                MaterialTheme.colorScheme.tertiary
            } else {
                MaterialTheme.colorScheme.outlineVariant
            },
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = CELL_HORIZONTAL_PADDING),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

private val CELL_HORIZONTAL_PADDING = 16.dp
private val CELL_BORDER_WIDTH = 1.dp
private val CELL_TONAL_ELEVATION = 1.dp
