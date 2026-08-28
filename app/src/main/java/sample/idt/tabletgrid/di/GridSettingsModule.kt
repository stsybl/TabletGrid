package sample.idt.tabletgrid.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import sample.idt.tabletgrid.domain.gridsettings.ValidateGridSettingsUseCase
import sample.idt.tabletgrid.ui.gridsettings.GridSettingsViewModel

val gridSettingsModule = module {
    viewModelOf(::GridSettingsViewModel)
    singleOf(::ValidateGridSettingsUseCase)
}
