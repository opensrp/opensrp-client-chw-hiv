package org.smartregister.chw.hiv.presenter

import com.nerdstone.neatformcore.domain.model.NFormViewData
import io.mockk.spyk
import io.mockk.verifyAll
import org.json.JSONObject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.smartregister.chw.hiv.contract.BaseHivCommunityFollowupContract
import org.smartregister.chw.hiv.domain.HivMemberObject
import org.smartregister.chw.hiv.model.BaseHivCommunityFollowupModel
import org.smartregister.chw.hiv.model.BaseRegisterFormModel
import org.smartregister.chw.hiv.util.Constants
import org.smartregister.commonregistry.CommonPersonObjectClient

class BaseHivCommunityFollowupPresenterTest {

    private val hivCommunityFollowupReferralView: BaseHivCommunityFollowupContract.View = spyk()
    private val hivCommunityFollowupReferralInteractor: BaseHivCommunityFollowupContract.Interactor = spyk()
    private val sampleBaseEntityId = "5a5mple-b35eent"
    private val hivCommunityFollowupReferralPresenter: BaseHivCommunityFollowupContract.Presenter =
        spyk(
            BaseHivCommunityFollowupPresenter(
                hivCommunityFollowupReferralView,
                BaseHivCommunityFollowupModel::class.java,
                hivCommunityFollowupReferralInteractor
            ),
            recordPrivateCalls = true
        )
    private lateinit var hivMemberObject: HivMemberObject

    @Before
    fun `Before test`() {
        val columnNames = BaseRegisterFormModel()
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
    fun `Should call save followup method of interactor`() {
        val valuesHashMap = hashMapOf<String, NFormViewData>()
        val jsonFormObject = JSONObject()
        hivCommunityFollowupReferralPresenter.initializeMemberObject(hivMemberObject)
        hivCommunityFollowupReferralPresenter.saveForm(valuesHashMap, jsonFormObject)
        verifyAll {
            hivCommunityFollowupReferralInteractor.saveFollowup(
                sampleBaseEntityId, valuesHashMap, jsonFormObject,
                hivCommunityFollowupReferralPresenter as BaseHivCommunityFollowupPresenter
            )
        }
    }

    @Test
    fun `Should return view`() {
        Assert.assertNotNull(hivCommunityFollowupReferralPresenter.getView())
    }

    @Test
    fun `Should call set profile view data`() {
        hivCommunityFollowupReferralPresenter.fillProfileData(spyk(hivMemberObject))
        verifyAll { hivCommunityFollowupReferralView.setProfileViewWithData() }
    }

    @Test
    fun initializeMemberObject() {
        hivCommunityFollowupReferralPresenter.initializeMemberObject(hivMemberObject)
        Assert.assertNotNull((hivCommunityFollowupReferralPresenter as BaseHivCommunityFollowupPresenter).hivMemberObject)
    }
}