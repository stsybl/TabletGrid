package sample.idt.tabletgrid

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import sample.idt.tabletgrid.di.appModule

class TabletGridApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TabletGridApplication)
            modules(appModule)
        }
    }
}
