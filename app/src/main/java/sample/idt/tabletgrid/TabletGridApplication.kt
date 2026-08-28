package sample.idt.tabletgrid

import android.app.Application
import androidx.compose.ui.AndroidComposeUiFlags
import androidx.compose.ui.ExperimentalComposeUiApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import sample.idt.tabletgrid.di.appModule

class TabletGridApplication : Application() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate() {
        // Compose UI 1.12.0 may briefly close the IME while transferring focus between fields.
        // This scheduler is disabled by default in the upcoming 1.12.1 patch release.
        AndroidComposeUiFlags.isOutOfFrameSchedulerForTextInputEventsEnabled = false
        super.onCreate()
        startKoin {
            androidContext(this@TabletGridApplication)
            modules(appModule)
        }
    }
}
