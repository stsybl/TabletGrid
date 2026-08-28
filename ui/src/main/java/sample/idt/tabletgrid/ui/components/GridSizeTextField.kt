package sample.idt.tabletgrid.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.then
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun GridSizeTextField(
    value: String,
    label: String,
    maxLength: Int,
    errorText: String?,
    helperText: String,
    imeAction: ImeAction,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onKeyboardAction: KeyboardActionHandler? = null,
) {
    val textFieldState = rememberTextFieldState(initialText = value)

    LaunchedEffect(textFieldState, value) {
        if (textFieldState.text.toString() != value) {
            textFieldState.edit {
                replace(
                    start = 0,
                    end = length,
                    text = value,
                )
            }
        }
    }

    LaunchedEffect(textFieldState) {
        snapshotFlow { textFieldState.text.toString() }
            .distinctUntilChanged()
            .collect(onValueChange)
    }

    OutlinedTextField(
        modifier = modifier,
        state = textFieldState,
        label = {
            Text(text = label)
        },
        isError = errorText != null,
        supportingText = {
            Text(text = errorText ?: helperText)
        },
        inputTransformation = InputTransformation
            .maxLength(maxLength)
            .then {
                if (!asCharSequence().all(Char::isDigit)) {
                    revertAllChanges()
                }
            },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = imeAction,
        ),
        onKeyboardAction = onKeyboardAction,
        lineLimits = TextFieldLineLimits.SingleLine,
    )
}
