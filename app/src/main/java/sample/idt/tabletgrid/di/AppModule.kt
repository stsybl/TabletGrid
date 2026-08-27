package sample.idt.tabletgrid.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

val ioDispatcherQualifier = named("ioDispatcher")

val appModule = module {

    single<CoroutineDispatcher>(ioDispatcherQualifier) { Dispatchers.IO }

    includes(
        gridSettingsModule,
        gridViewerModule,
    )
}
