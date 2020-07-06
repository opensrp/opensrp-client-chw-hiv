package org.smartregister.chw.hiv.presenter

import io.mockk.spyk
import io.mockk.verifySequence
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.smartregister.chw.hiv.TestHivApp
import org.smartregister.chw.hiv.contract.BaseHivRegisterFragmentContract
import org.smartregister.chw.hiv.model.BaseHivRegisterFragmentModel
import org.smartregister.chw.hiv.util.Constants
import org.smartregister.chw.hiv.util.DBConstants

/**
 * Test class for testing various methods in BaseHivRegisterFragmentPresenter
 */
@RunWith(RobolectricTestRunner::class)
@Config(application = TestHivApp::class)
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
        hivRegisterFragmentPresenter.initializeQueries("ec_hiv_register.hiv_status = 'UNKNOWN'")
        val visibleColumns =
            (hivRegisterFragmentPresenter as BaseHivRegisterFragmentPresenter).visibleColumns
        verifySequence {
            hivRegisterFragmentView.initializeQueryParams(
                "ec_hiv_register",
                "SELECT COUNT(*) FROM ec_hiv_register WHERE ec_hiv_register.hiv_status = 'UNKNOWN' ",
                "Select ec_hiv_register.id as _id , ec_hiv_register.relationalid FROM ec_hiv_register WHERE ec_hiv_register.hiv_status = 'UNKNOWN' "
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
            "ec_hiv_register.client_hiv_status_during_registration = '${Constants.HivStatus.UNKNOWN}'",
            hivRegisterFragmentPresenter.getDueFilterCondition()
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