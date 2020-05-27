package org.smartregister.chw.hiv.presenter

import com.nerdstone.neatformcore.domain.model.NFormViewData
import io.mockk.spyk
import io.mockk.verifyAll
import org.json.JSONObject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.smartregister.chw.hiv.contract.BaseHivFollowupContract
import org.smartregister.chw.hiv.domain.HivMemberObject
import org.smartregister.chw.hiv.model.BaseHivCommunityFollowupModel
import org.smartregister.chw.hiv.model.BaseRegisterFormModel
import org.smartregister.chw.hiv.util.Constants
import org.smartregister.commonregistry.CommonPersonObjectClient

class BaseHivCommunityFollowupPresenterTest {

    private val hivFollowupReferralView: BaseHivFollowupContract.View = spyk()
    private val hivFollowupReferralInteractor: BaseHivFollowupContract.Interactor = spyk()
    private val sampleBaseEntityId = "5a5mple-b35eent"
    private val hivFollowupReferralPresenter: BaseHivFollowupContract.Presenter =
        spyk(
            BaseHivCommunityFollowupPresenter(
                hivFollowupReferralView,
                BaseHivCommunityFollowupModel::class.java,
                hivFollowupReferralInteractor
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
        hivFollowupReferralPresenter.initializeMemberObject(hivMemberObject)
        hivFollowupReferralPresenter.saveForm(valuesHashMap, jsonFormObject)
        verifyAll {
            hivFollowupReferralInteractor.saveFollowup(
                sampleBaseEntityId, valuesHashMap, jsonFormObject,
                hivFollowupReferralPresenter as BaseHivCommunityFollowupPresenter
            )
        }
    }

    @Test
    fun `Should return view`() {
        Assert.assertNotNull(hivFollowupReferralPresenter.getView())
    }

    @Test
    fun `Should call set profile view data`() {
        hivFollowupReferralPresenter.fillProfileData(spyk(hivMemberObject))
        verifyAll { hivFollowupReferralView.setProfileViewWithData() }
    }

    @Test
    fun initializeMemberObject() {
        hivFollowupReferralPresenter.initializeMemberObject(hivMemberObject)
        Assert.assertNotNull((hivFollowupReferralPresenter as BaseHivCommunityFollowupPresenter).hivMemberObject)
    }
}