package sample.idt.tabletgrid.navigation

import kotlinx.serialization.Serializable
import sample.idt.tabletgrid.domain.gridsettings.GridSettingsLimits

internal sealed interface TabletGridDestination {

    @Serializable
    data object GridSettings : TabletGridDestination

    @Serializable
    data class GridViewer(
        val rowCount: Int,
        val columnCount: Int,
    ) : TabletGridDestination {
        init {
            require(rowCount in GridSettingsLimits.MIN_SIZE..GridSettingsLimits.MAX_ROWS) {
                "rowCount must be in " +
                    "${GridSettingsLimits.MIN_SIZE}..${GridSettingsLimits.MAX_ROWS}, " +
                    "but was $rowCount"
            }
            require(columnCount in GridSettingsLimits.MIN_SIZE..GridSettingsLimits.MAX_COLUMNS) {
                "columnCount must be in " +
                    "${GridSettingsLimits.MIN_SIZE}..${GridSettingsLimits.MAX_COLUMNS}, " +
                    "but was $columnCount"
            }
        }
    }
}
