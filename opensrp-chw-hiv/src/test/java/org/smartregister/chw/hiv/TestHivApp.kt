package org.smartregister.chw.hiv

import android.app.Application
import org.koin.core.context.stopKoin
import org.robolectric.TestLifecycleApplication
import org.smartregister.Context
import org.smartregister.CoreLibrary
import timber.log.Timber
import java.lang.reflect.Method

/**
 * TestApplication used across various tests
 */
class TestHivApp : Application(), TestLifecycleApplication {

    override fun onCreate() {
        super.onCreate()
        setTheme(R.style.CustomTestTheme)
        val context = Context.getInstance()
        context.updateApplicationContext(this)
        CoreLibrary.init(context)
        HivLibrary.testInit(this)
    }

    override fun beforeTest(method: Method?) {
        Timber.i("Testing method: ${method?.name}")
    }

    override fun prepareTest(test: Any?) {
        Timber.i("Prepared test ${test.toString()}")
    }

    override fun afterTest(method: Method?) {
        Timber.i("End of testing method: ${method?.name}")
        stopKoin()
    }
}