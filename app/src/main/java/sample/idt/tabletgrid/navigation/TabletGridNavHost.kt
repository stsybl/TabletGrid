package sample.idt.tabletgrid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import sample.idt.tabletgrid.ui.gridsettings.GridSettingsRoute
import sample.idt.tabletgrid.ui.gridviewer.GridViewerRoute

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
        composable<TabletGridDestination.GridSettings> {
            GridSettingsRoute(
                onOpenGrid = { rows, columns ->
                    navController.navigate(
                        TabletGridDestination.GridViewer(
                            rowCount = rows,
                            columnCount = columns,
                        )
                    )
                },
            )
        }
        composable<TabletGridDestination.GridViewer> { backStackEntry ->
            val destination = backStackEntry.toRoute<TabletGridDestination.GridViewer>()

            GridViewerRoute(
                rowCount = destination.rowCount,
                columnCount = destination.columnCount,
                onBack = navController::navigateUp,
            )
        }
    }
}
