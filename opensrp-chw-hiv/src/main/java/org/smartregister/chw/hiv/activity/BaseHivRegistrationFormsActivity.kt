package org.smartregister.chw.hiv.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.nerdstone.neatandroidstepper.core.domain.StepperActions
import com.nerdstone.neatandroidstepper.core.model.StepperModel
import com.nerdstone.neatandroidstepper.core.model.StepperModel.IndicatorType
import com.nerdstone.neatandroidstepper.core.stepper.Step
import com.nerdstone.neatandroidstepper.core.stepper.StepVerificationState
import com.nerdstone.neatformcore.domain.builders.FormBuilder
import com.nerdstone.neatformcore.domain.model.JsonFormStepBuilderModel
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.form.json.JsonFormBuilder
import org.joda.time.DateTime
import org.joda.time.Period
import org.json.JSONException
import org.json.JSONObject
import org.koin.android.ext.android.inject
import org.smartregister.AllConstants
import org.smartregister.chw.hiv.HivLibrary
import org.smartregister.chw.hiv.R
import org.smartregister.chw.hiv.contract.BaseRegisterFormsContract
import org.smartregister.chw.hiv.dao.HivDao
import org.smartregister.chw.hiv.interactor.BaseRegisterFormsInteractor
import org.smartregister.chw.hiv.model.AbstractRegisterFormModel
import org.smartregister.chw.hiv.model.BaseRegisterFormModel
import org.smartregister.chw.hiv.presenter.BaseRegisterFormsPresenter
import org.smartregister.chw.hiv.util.Constants
import org.smartregister.chw.hiv.util.DBConstants
import org.smartregister.chw.hiv.util.JsonFormConstants
import org.smartregister.chw.hiv.util.JsonFormUtils.addFormMetadata
import org.smartregister.chw.hiv.util.JsonFormUtils.getFormAsJson
import org.smartregister.commonregistry.CommonPersonObjectClient
import timber.log.Timber
import java.util.*

/**
 * Created by cozej4 on 2020-05-13.
 *
 * @cozej4 https://github.com/cozej4
 */
/**
 * This is the activity for loading hiv registration and followup JSON forms. It implements [BaseRegisterFormsContract.View]
 * and [StepperActions] (which is from the neat form library) that provides callback methods from the
 * form builder. It exposes a method to receiving the data from the views and exiting the activity
 */
open class BaseHivRegistrationFormsActivity : AppCompatActivity(), BaseRegisterFormsContract.View,
    StepperActions {

    protected var presenter: BaseRegisterFormsContract.Presenter? = null
    protected var baseEntityId: String? = null
    protected var formName: String? = null
    private var viewModel: AbstractRegisterFormModel? = null
    private var formBuilder: FormBuilder? = null
    private var jsonForm: JSONObject? = null
    private var useDefaultNeatFormLayout: Boolean? = null
    val hivLibrary by inject<HivLibrary>()

    protected val locationID: String
        get() = org.smartregister.Context.getInstance().allSharedPreferences()
            .getPreference(AllConstants.CURRENT_LOCATION_ID)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(this.intent) {
            baseEntityId = getStringExtra(Constants.ActivityPayload.BASE_ENTITY_ID)
            action = getStringExtra(Constants.ActivityPayload.ACTION)
            formName = getStringExtra(Constants.ActivityPayload.HIV_REGISTRATION_FORM_NAME)
            useDefaultNeatFormLayout =
                getBooleanExtra(Constants.ActivityPayload.USE_DEFAULT_NEAT_FORM_LAYOUT, false)
            try {
                jsonForm = JSONObject(getStringExtra(Constants.ActivityPayload.JSON_FORM))
            } catch (e: JSONException) {
                Timber.e(e)
            }
        }

        presenter = presenter()
        viewModel =
            ViewModelProviders.of(this).get(presenter!!.getViewModel<AbstractRegisterFormModel>())
        setContentView(R.layout.activity_hiv_registration)
        updateMemberObject()

        with(presenter) {
            this?.initializeMemberObject(viewModel?.hivMemberObject!!)
            this?.fillClientData(viewModel?.hivMemberObject!!)
        }

        createViewsFromJson()
    }

    private fun createViewsFromJson() {
        val formJsonObject: JSONObject? = jsonForm ?: getFormAsJson(formName, this)
        try {
            formJsonObject?.also {
                addFormMetadata(it, baseEntityId, locationID)
                with(viewModel?.hivMemberObject!!) {
                    val age = Period(DateTime(this.age), DateTime()).years
                    it.put(
                        JsonFormConstants.FORM,
                        "${this.firstName} ${this.middleName} ${this.lastName}, $age"

                    )
                }

                val formLayout = findViewById<LinearLayout>(R.id.formLayout)
                val stepperModel = StepperModel.Builder()
                    .exitButtonDrawableResource(R.drawable.ic_arrow_back_white_24dp)
                    .indicatorType(IndicatorType.DOT_INDICATOR)
                    .toolbarColorResource(R.color.family_actionbar)
                    .build()


                val customLayouts = ArrayList<View>().also { list ->
                    list.add(layoutInflater.inflate(R.layout.hiv_registration_form_view, null))
                }

                formBuilder = JsonFormBuilder(it.toString(), this, formLayout)
                    .buildForm(
                        JsonFormStepBuilderModel.Builder(this, stepperModel).build(),
                        if (useDefaultNeatFormLayout!!) customLayouts else null
                    )
                formLayout.addView(formBuilder!!.neatStepperLayout)
            }

        } catch (ex: JSONException) {
            Timber.e(ex)
        }
    }

    override fun presenter() = BaseRegisterFormsPresenter(
        baseEntityId!!, this, BaseRegisterFormModel::class.java, BaseRegisterFormsInteractor()
    )


    override fun setProfileViewWithData() = Unit

    @Throws(Exception::class)
    private fun updateMemberObject() {
        with(presenter!!) {
            val query = viewModel!!.mainSelect(getMainTable(), getMainCondition())
            Timber.d("Query for the family member = %s", query)
            val commonRepository = hivLibrary.context.commonrepository(getMainTable())
            with(commonRepository.rawCustomQueryForAdapter(query)) {
                if (moveToFirst()) {
                    commonRepository.readAllcommonforCursorAdapter(this)
                        .also { commonPersonObject ->
                            CommonPersonObjectClient(
                                commonPersonObject.caseId, commonPersonObject.details, ""
                            ).apply {
                                this.columnmaps = commonPersonObject.columnmaps
                            }.also {
                                viewModel!!.hivMemberObject =
                                    commonPersonObject.columnmaps[DBConstants.Key.BASE_ENTITY_ID]?.let { it1 ->
                                        HivDao.getMember(it1)
                                    }
                            }
                        }
                }

            }
        }
    }

    override fun onButtonNextClick(step: Step) = Unit

    override fun onButtonPreviousClick(step: Step) = Unit

    override fun onCompleteStepper() {
        val formData = formBuilder!!.getFormData()
        if (formData.isNotEmpty()) {
            //Saving HIV registration Date
            formData[JsonFormConstants.HIV_REGISTRATION_DATE] = NFormViewData().apply {
                value = Calendar.getInstance().timeInMillis
            }
            presenter!!.saveForm(formData, jsonForm!!)
            Timber.i("Saved data = %s", Gson().toJson(formData))
            finish()
        }
    }

    override fun onExitStepper() {
        AlertDialog.Builder(this, R.style.AlertDialogTheme)
            .setTitle(getString(R.string.confirm_form_close))
            .setMessage(getString(R.string.confirm_form_close_explanation))
            .setNegativeButton(R.string.yes) { _: DialogInterface?, _: Int -> finish() }
            .setPositiveButton(R.string.no) { _: DialogInterface?, _: Int ->
                Timber.d("Do Nothing exit confirm dialog")
            }
            .create()
            .show()
    }

    override fun onStepComplete(step: Step) = Unit

    override fun onStepError(stepVerificationState: StepVerificationState) = Unit
}