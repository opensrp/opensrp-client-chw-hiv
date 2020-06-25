package org.smartregister.chw.hiv.presenter

import io.mockk.spyk
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.smartregister.chw.hiv.TestHivApp
import org.smartregister.chw.hiv.contract.BaseHivRegisterContract
import org.smartregister.chw.hiv.model.BaseHivRegisterModel

@RunWith(RobolectricTestRunner::class)
@Config(application = TestHivApp::class)
class BaseHivRegisterPresenterTest {

    private val hivHistoryView: BaseHivRegisterContract.View = spyk()
    private val hivHistoryPresenter: BaseHivRegisterContract.Presenter =
        spyk(
            BaseHivRegisterPresenter(hivHistoryView, BaseHivRegisterModel()),
            recordPrivateCalls = true
        )

    @Test
    fun `Referral register view should not be null`() {
        assertNotNull(hivHistoryPresenter.getView())
    }
}