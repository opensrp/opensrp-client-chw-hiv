package org.smartregister.chw.hiv.presenter

import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.smartregister.chw.hiv.TestHivApp
import org.smartregister.chw.hiv.contract.BaseHivRegisterFragmentContract
import org.smartregister.chw.hiv.domain.HivMemberObject
import org.smartregister.chw.hiv.model.BaseHivCommunityFollowupModel
import org.smartregister.chw.hiv.util.Constants
import org.smartregister.commonregistry.CommonPersonObjectClient
import java.util.*

/**
 * Test class for testing various methods in BaseHIVCommunityFollowup Presenter
 */
@RunWith(RobolectricTestRunner::class)
@Config(application = TestHivApp::class)
class BaseHivCommunityFollowupPresenterTest {

    private val hivCommunityFollowupView: BaseHivRegisterFragmentContract.View = spyk()
    private val hivCommunityFollowupModel = BaseHivCommunityFollowupModel()
    private val sampleBaseEntityId = "5a5mple-b35eent"
    private val hivCommunityFollowupPresenter: BaseHivRegisterFragmentPresenter =
        spyk(
            BaseHivCommunityFollowupPresenter(
                hivCommunityFollowupView,
                hivCommunityFollowupModel,
                "null"
            ),
            recordPrivateCalls = true
        )
    private lateinit var hivMemberObject: HivMemberObject

    @Before
    fun `Before test`() {
        val columnNames = BaseHivCommunityFollowupModel()
            .mainColumns(Constants.Tables.FAMILY_MEMBER).map {
                it.replace("${Constants.Tables.FAMILY_MEMBER}.", "")
            }.toTypedArray()

        val columnValues = arrayListOf(
            "10689167-07ca-45a4-91c0-1406c9ceb881", sampleBaseEntityId, "first_name", "middle_name",
            "last_name", "unique_id", "male", "1985-01-01T03:00:00.000+03:00", null
        )
        val details = columnNames.zip(columnValues).toMap()
        hivMemberObject = HivMemberObject(
            CommonPersonObjectClient(sampleBaseEntityId, details, "Some Cool Name").apply {
                columnmaps = details
            }
        )
    }

    @Test
    fun `Should call initialize query parameters on the view`() {
        val condition = "is_closed = 0"
        val mainTable = hivCommunityFollowupPresenter.getMainTable();
        hivCommunityFollowupPresenter.initializeQueries(condition)
        verify {
            hivCommunityFollowupView.initializeQueryParams(
                mainTable,
                "SELECT COUNT(*) FROM ec_hiv_community_followup WHERE is_closed = 0 ",
                "Select ec_hiv_community_followup.id as _id , ec_hiv_community_followup.relationalid FROM ec_hiv_community_followup WHERE is_closed = 0 "
            )
            hivCommunityFollowupView.initializeAdapter(TreeSet())
            hivCommunityFollowupView.countExecute()
            hivCommunityFollowupView.filterandSortInInitializeQueries()
        }
    }

    @Test
    fun `Should return view`() {
        Assert.assertNotNull(hivCommunityFollowupPresenter.getView())
    }

    @Test
    fun `Should return sort query`() {
        Assert.assertNotNull(hivCommunityFollowupPresenter.getDefaultSortQuery())
    }

    @Test
    fun `Should return main table`() {
        Assert.assertEquals(
            Constants.Tables.HIV_COMMUNITY_FOLLOWUP,
            hivCommunityFollowupPresenter.getMainTable()
        )
    }
}