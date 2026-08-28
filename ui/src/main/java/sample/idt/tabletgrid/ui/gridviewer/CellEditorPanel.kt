package sample.idt.tabletgrid.ui.gridviewer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import sample.idt.tabletgrid.ui.R

@Composable
fun CellEditorPanel(
    editor: CellEditorUiModel,
    rowIndex: Int,
    columnIndex: Int,
    onSave: (text: String) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember(editor.cellId) { FocusRequester() }
    var fieldValue by rememberSaveable(
        editor.cellId,
        stateSaver = TextFieldValue.Saver,
    ) {
        mutableStateOf(
            TextFieldValue(
                text = editor.text,
                selection = TextRange(editor.text.length),
            )
        )
    }

    LaunchedEffect(editor.cellId) {
        focusRequester.requestFocus()
    }

    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        tonalElevation = EDITOR_TONAL_ELEVATION,
        border = BorderStroke(
            width = EDITOR_BORDER_WIDTH,
            color = MaterialTheme.colorScheme.outlineVariant,
        ),
    ) {
        Column(
            modifier = Modifier.padding(EDITOR_CONTENT_PADDING),
            verticalArrangement = Arrangement.spacedBy(EDITOR_CONTENT_SPACING),
        ) {
            Text(
                text = stringResource(R.string.grid_viewer_edit_cell),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = stringResource(
                    R.string.grid_viewer_edit_cell_position,
                    rowIndex + 1,
                    columnIndex + 1,
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = fieldValue,
                onValueChange = { value -> fieldValue = value },
                label = {
                    Text(text = stringResource(R.string.grid_viewer_edit_cell_label))
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onSave(fieldValue.text) }),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(onClick = onCancel) {
                    Text(text = stringResource(R.string.grid_viewer_cancel_cell_edit))
                }
                Spacer(modifier = Modifier.width(EDITOR_ACTION_SPACING))
                Button(onClick = { onSave(fieldValue.text) }) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.width(EDITOR_ACTION_ICON_SPACING))
                    Text(text = stringResource(R.string.grid_viewer_save_cell_edit))
                }
            }
        }
    }
}

private val EDITOR_CONTENT_PADDING = 20.dp
private val EDITOR_CONTENT_SPACING = 12.dp
private val EDITOR_ACTION_SPACING = 8.dp
private val EDITOR_ACTION_ICON_SPACING = 8.dp
private val EDITOR_BORDER_WIDTH = 1.dp
private val EDITOR_TONAL_ELEVATION = 2.dp
