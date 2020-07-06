package org.smartregister.chw.hiv.presenter

import com.nerdstone.neatformcore.domain.model.NFormViewData
import io.mockk.spyk
import io.mockk.verifyAll
import org.json.JSONObject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.smartregister.chw.hiv.TestHivApp
import org.smartregister.chw.hiv.contract.BaseRegisterFormsContract
import org.smartregister.chw.hiv.domain.HivMemberObject
import org.smartregister.chw.hiv.model.BaseHivRegisterFragmentModel
import org.smartregister.chw.hiv.util.Constants
import org.smartregister.chw.hiv.util.DBConstants
import org.smartregister.commonregistry.CommonPersonObjectClient

/**
 * Test class for testing various methods in BaseRegisterFormsPresenterTest
 */
@RunWith(RobolectricTestRunner::class)
@Config(application = TestHivApp::class)
class BaseRegisterFormsPresenterTest {

    private val registerFormsView: BaseRegisterFormsContract.View = spyk()
    private val registerFormsInteractor: BaseRegisterFormsContract.Interactor = spyk()
    private val sampleBaseEntityId = "5a5mple-b35eent"
    private val baseRegisterFormsPresenter: BaseRegisterFormsContract.Presenter =
        spyk(
            BaseRegisterFormsPresenter(
                sampleBaseEntityId, registerFormsView,
                registerFormsInteractor
            ),
            recordPrivateCalls = true
        )
    private lateinit var hivMemberObject: HivMemberObject

    @Before
    fun `Just before tests`() {
        val columnNames = BaseHivRegisterFragmentModel()
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
    fun `Should return view`() {
        Assert.assertNotNull(baseRegisterFormsPresenter.getView())
    }

    @Test
    fun `Should return main condition with the base entity id`() {
        Assert.assertTrue(
            baseRegisterFormsPresenter.getMainCondition() == "${Constants.Tables.FAMILY_MEMBER}.${DBConstants.Key.BASE_ENTITY_ID}  = '$sampleBaseEntityId'"
        )
    }

    @Test
    fun `Should return ec_family_member as the main table`() {
        Assert.assertTrue(baseRegisterFormsPresenter.getMainTable() == Constants.Tables.FAMILY_MEMBER)
    }

    @Test
    fun `Should set profile with data  `() {
        baseRegisterFormsPresenter.fillClientData(hivMemberObject)
        verifyAll { registerFormsView.setProfileViewWithData() }
    }

    @Test
    fun `Should update the member object`() {
        baseRegisterFormsPresenter.initializeMemberObject(hivMemberObject)
        Assert.assertNotNull((baseRegisterFormsPresenter as BaseRegisterFormsPresenter).hivMemberObject)
    }

    @Test
    fun `Should call save registration method of interactor`() {
        val valuesHashMap = hashMapOf<String, NFormViewData>()
        val jsonFormObject = JSONObject()
        baseRegisterFormsPresenter.saveForm(valuesHashMap, jsonFormObject)
        verifyAll {
            registerFormsInteractor.saveRegistration(
                sampleBaseEntityId, valuesHashMap, jsonFormObject,
                baseRegisterFormsPresenter as BaseRegisterFormsPresenter
            )
        }
    }
}