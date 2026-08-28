package sample.idt.tabletgrid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import sample.idt.tabletgrid.navigation.TabletGridNavHost
import sample.idt.tabletgrid.ui.theme.TabletGridTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TabletGridTheme {
                TabletGridNavHost()
            }
        }
    }
}