package sample.idt.tabletgrid.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import sample.idt.tabletgrid.domain.gridsettings.ValidateGridSettingsUseCase
import sample.idt.tabletgrid.ui.gridsettings.GridSettingsViewModel
import sample.idt.tabletgrid.ui.gridviewer.GridViewerViewModel

val appModule = module {
    singleOf(::ValidateGridSettingsUseCase)
    viewModelOf(::GridSettingsViewModel)
    viewModelOf(::GridViewerViewModel)
}
