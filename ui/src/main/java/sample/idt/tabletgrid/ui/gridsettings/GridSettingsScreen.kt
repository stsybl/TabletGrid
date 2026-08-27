package sample.idt.tabletgrid.ui.gridsettings

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sample.idt.tabletgrid.domain.gridsettings.GridSettingsLimits
import sample.idt.tabletgrid.domain.gridsettings.GridSettingsValidationError
import sample.idt.tabletgrid.ui.R
import sample.idt.tabletgrid.ui.components.GridSizeTextField
import sample.idt.tabletgrid.ui.theme.TabletGridTheme

@Composable
fun GridSettingsScreen(
    state: GridSettingsState,
    onEvent: (GridSettingsEvent) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            val contentPadding = when {
                maxWidth >= EXPANDED_WIDTH_MIN -> 48.dp
                maxWidth >= MEDIUM_WIDTH_MIN -> 24.dp
                else -> 16.dp
            }

            if (maxWidth >= EXPANDED_WIDTH_MIN) {
                Row(
                    modifier = Modifier
                        .padding(contentPadding)
                        .widthIn(max = 920.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(48.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    GridSettingsDescription(
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Start,
                        horizontalAlignment = Alignment.Start,
                    )
                    GridSettingsForm(
                        state = state,
                        onEvent = onEvent,
                        modifier = Modifier.weight(1f),
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .padding(contentPadding)
                        .widthIn(max = 480.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    GridSettingsDescription(
                        textAlign = TextAlign.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    )
                    GridSettingsForm(
                        state = state,
                        onEvent = onEvent,
                    )
                }
            }
        }
    }
}

@Composable
private fun GridSettingsDescription(
    modifier: Modifier = Modifier,
    textAlign: TextAlign,
    horizontalAlignment: Alignment.Horizontal,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = horizontalAlignment,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.grid_settings_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = textAlign,
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.grid_settings_description),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = textAlign,
        )
    }
}

@Composable
private fun GridSettingsForm(
    state: GridSettingsState,
    onEvent: (GridSettingsEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val rowsFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        rowsFocusRequester.requestFocus()
    }

    Column(
        modifier = modifier
            .widthIn(max = 420.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        GridSizeTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(rowsFocusRequester),
            value = state.rowsText,
            label = stringResource(R.string.grid_settings_rows_label),
            maxLength = GridSettingsLimits.MAX_ROWS.toString().length,
            errorText = errorToText(state.rowsError),
            helperText = stringResource(
                R.string.grid_settings_rows_helper,
                GridSettingsLimits.MIN_SIZE,
                GridSettingsLimits.MAX_ROWS,
            ),
            imeAction = ImeAction.Next,
            onKeyboardAction = KeyboardActionHandler {
                focusManager.moveFocus(FocusDirection.Down)
            },
            onValueChange = { value ->
                onEvent(GridSettingsEvent.RowsChanged(value))
            },
        )

        GridSizeTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.columnsText,
            label = stringResource(R.string.grid_settings_columns_label),
            maxLength = GridSettingsLimits.MAX_COLUMNS.toString().length,
            errorText = errorToText(state.columnsError),
            helperText = stringResource(
                R.string.grid_settings_columns_helper,
                GridSettingsLimits.MIN_SIZE,
                GridSettingsLimits.MAX_COLUMNS,
            ),
            imeAction = ImeAction.Done,
            onKeyboardAction = KeyboardActionHandler {
                if (state.createEnabled) {
                    onEvent(GridSettingsEvent.CreateGridClicked)
                }
            },
            onValueChange = { value ->
                onEvent(GridSettingsEvent.ColumnsChanged(value))
            },
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = state.createEnabled,
            onClick = {
                onEvent(GridSettingsEvent.CreateGridClicked)
            },
        ) {
            Text(text = stringResource(R.string.grid_settings_create_button))
        }
    }
}

@Composable
private fun errorToText(error: GridSettingsValidationError?): String? {
    return when (error) {
        null, GridSettingsValidationError.Empty -> null
        GridSettingsValidationError.InvalidNumber -> {
            stringResource(R.string.grid_settings_error_invalid_number)
        }
        is GridSettingsValidationError.BelowMinimum -> {
            stringResource(R.string.grid_settings_error_below_minimum, error.minValue)
        }
        is GridSettingsValidationError.AboveMaximum -> {
            stringResource(R.string.grid_settings_error_above_maximum, error.maxValue)
        }
    }
}

private val MEDIUM_WIDTH_MIN = 600.dp
private val EXPANDED_WIDTH_MIN = 840.dp

@Preview(
    showBackground = true,
    widthDp = 800,
    heightDp = 600,
)
@Composable
private fun GridSettingsScreenLightPreview() {
    TabletGridTheme(darkTheme = false) {
        GridSettingsScreen(
            state = GridSettingsState(
                rowsText = "1000",
                columnsText = "6",
                rowsError = null,
                columnsError = null,
            ),
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
private fun GridSettingsScreenDarkPreview() {
    TabletGridTheme(darkTheme = true) {
        GridSettingsScreen(
            state = GridSettingsState(
                rowsText = "1001",
                columnsText = "7",
                rowsError = GridSettingsValidationError.AboveMaximum(maxValue = 1000),
                columnsError = GridSettingsValidationError.AboveMaximum(maxValue = 6),
            ),
            onEvent = {},
        )
    }
}
