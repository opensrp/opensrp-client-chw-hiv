package org.smartregister.chw.hiv.presenter

import io.mockk.spyk
import io.mockk.verifySequence
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.smartregister.chw.hiv.TestReferralApp
import org.smartregister.chw.hiv.contract.BaseHivRegisterFragmentContract
import org.smartregister.chw.hiv.model.BaseHivRegisterFragmentModel
import org.smartregister.chw.hiv.util.Constants
import org.smartregister.chw.hiv.util.DBConstants

@RunWith(RobolectricTestRunner::class)
@Config(application = TestReferralApp::class)
class BaseHivRegisterFragmentPresenterTest {

    private val hivRegisterFragmentView: BaseHivRegisterFragmentContract.View = spyk()

    private val hivRegisterFragmentPresenter: BaseHivRegisterFragmentContract.Presenter =
        spyk(
            BaseHivRegisterFragmentPresenter(
                hivRegisterFragmentView, BaseHivRegisterFragmentModel(), null
            ),
            recordPrivateCalls = true
        )


    @Test
    fun `Should initialize the queries on the view`() {
        hivRegisterFragmentPresenter.initializeQueries("ec_hiv.hiv_status")
        val visibleColumns =
            (hivRegisterFragmentPresenter as BaseHivRegisterFragmentPresenter).visibleColumns
        verifySequence {
            hivRegisterFragmentView.initializeQueryParams(
                "ec_hiv",
                "SELECT COUNT(*) FROM ec_hiv WHERE ec_hiv.hiv_status ",
                "Select ec_hiv.id as _id , ec_hiv.relationalid FROM ec_hiv WHERE ec_hiv.hiv_status "
            )
            hivRegisterFragmentView.initializeAdapter(visibleColumns)
            hivRegisterFragmentView.countExecute()
            hivRegisterFragmentView.filterandSortInInitializeQueries()
        }
    }

    @Test
    fun `Main condition should be initialize by empty string`() {
        assertTrue(hivRegisterFragmentPresenter.getMainCondition().isEmpty())
    }

    @Test
    fun `Should return the right table name`() {
        assertTrue(hivRegisterFragmentPresenter.getMainTable() == Constants.Tables.HIV)
    }

    @Test
    fun `Should return the due filter query`() {
        assertEquals(
            hivRegisterFragmentPresenter.getDueFilterCondition(),
            "ec_hiv.hiv_status = '${Constants.HivStatus.UNKNOWN}'"
        )
    }

    @Test
    fun `Should return default sort query`() {
        assertEquals(
            hivRegisterFragmentPresenter.getDefaultSortQuery(),
            Constants.Tables.HIV + "." + DBConstants.Key.HIV_REGISTRATION_DATE + " DESC "
        )
    }

    @Test
    fun `View should not be null`() {
        assertNotNull(hivRegisterFragmentPresenter.getView())
    }
}