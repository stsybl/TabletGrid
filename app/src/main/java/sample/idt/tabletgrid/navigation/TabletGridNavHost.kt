package sample.idt.tabletgrid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import sample.idt.tabletgrid.ui.gridsettings.GridSettingsRoute

@Composable
fun TabletGridNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = TabletGridDestination.GridSettings,
        modifier = modifier,
    ) {
        composable(route = TabletGridDestination.GridSettings) {
            GridSettingsRoute(
                onOpenGrid = { _, _ -> },
            )
        }
    }
}
