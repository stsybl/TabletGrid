package sample.idt.tabletgrid.ui.gridviewer

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import sample.idt.tabletgrid.ui.theme.TabletGridTheme

@Composable
fun GridViewerScreen(
    state: GridViewerState,
    onEvent: (GridViewerEvent) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            when (state) {
                GridViewerState.Loading -> CircularProgressIndicator()
                is GridViewerState.Preview -> {

                }
            }
        }
    }
}

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
                rowCount = 2,
                columnCount = 2,
                cells = listOf("one", "two", "three", "four"),
            ),
            onEvent = {},
        )
    }
}
