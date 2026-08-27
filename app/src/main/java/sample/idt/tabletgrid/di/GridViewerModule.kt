package sample.idt.tabletgrid.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import sample.idt.tablegrid.data.RandomTextGenerator
import sample.idt.tabletgrid.data.gridviewer.GridRepositoryImpl
import sample.idt.tabletgrid.domain.gridviewer.GridRepository
import sample.idt.tabletgrid.domain.gridviewer.LoadGridDataUseCase
import sample.idt.tabletgrid.ui.gridviewer.GridViewerViewModel

val gridViewerModule = module {
    viewModelOf(::GridViewerViewModel)

    single { RandomTextGenerator() }

    single<GridRepository> {
        GridRepositoryImpl(
            randomTextGenerator = get(),
            ioDispatcher = get(ioDispatcherQualifier),
        )
    }
    singleOf(::LoadGridDataUseCase)
}
