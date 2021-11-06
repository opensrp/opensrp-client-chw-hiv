package org.smartregister.chw.hiv.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import org.json.JSONObject
import org.smartregister.chw.hiv.R
import org.smartregister.chw.hiv.contract.BaseHivRegisterContract
import org.smartregister.chw.hiv.fragment.BaseHivCommunityFollowupRegisterFragment
import org.smartregister.chw.hiv.fragment.BaseHivRegisterFragment
import org.smartregister.chw.hiv.listener.HivBottomNavigationListener
import org.smartregister.chw.hiv.model.BaseHivRegisterModel
import org.smartregister.chw.hiv.presenter.BaseHivRegisterPresenter
import org.smartregister.chw.hiv.util.Constants
import org.smartregister.helper.BottomNavigationHelper
import org.smartregister.listener.BottomNavigationListener
import org.smartregister.view.activity.BaseRegisterActivity

/**
 * Created by cozej4 on 2020-05-13.
 *
 * @cozej4 https://github.com/cozej4
 */

/***
 * This class is for displaying register for all the hiv clients
 * it implements [BaseHivRegisterContract.View]
 */
open class BaseHivRegisterActivity : BaseRegisterActivity(),
    BaseHivRegisterContract.View {

    protected var baseEntityId: String? = null
    protected var formName: String? = null
    protected var formAction: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(this.intent) {
            baseEntityId = getStringExtra(Constants.ActivityPayload.BASE_ENTITY_ID)
            formAction = getStringExtra(Constants.ActivityPayload.ACTION)
            formName = getStringExtra(Constants.ActivityPayload.HIV_REGISTRATION_FORM_NAME)
            onStartActivityWithAction()
        }
    }

    /**
     * Process a payload when an activity is started with an action
     */
    protected open fun onStartActivityWithAction() {
        if (formName != null && formAction != null) {
            startFormActivity(formName, baseEntityId, formAction)
        }
    }

    override fun startRegistration() = Unit

    override fun startFormActivity(formName: String?, entityId: String?, metaData: String?) = Unit

    override fun startFormActivity(jsonForm: JSONObject) {
        //Implement
    }

    override fun onActivityResultExtended(requestCode: Int, resultCode: Int, data: Intent?) = Unit

    override fun getViewIdentifiers(): List<String> = listOf(Constants.Configuration.HIV_REGISTER)

    /**
     * Override this to subscribe to bottom navigation
     */
    override fun registerBottomNavigation() {
        bottomNavigationHelper = BottomNavigationHelper()
        bottomNavigationView =
            findViewById(org.smartregister.R.id.bottom_navigation)
        bottomNavigationView?.also {
            it.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
            it.menu.removeItem(org.smartregister.R.id.action_clients)
            it.menu.removeItem(R.id.action_register)
            it.menu.removeItem(org.smartregister.R.id.action_search)
            it.menu.removeItem(org.smartregister.R.id.action_library)
            it.inflateMenu(menuResource)
            bottomNavigationHelper.disableShiftMode(it)
            it.setOnNavigationItemSelectedListener(getBottomNavigation(this))
        }
    }

    @get:MenuRes
    val menuResource
        get() = R.menu.bottom_nav_hiv_menu

    protected open fun getBottomNavigation(activity: Activity?): BottomNavigationListener =
        HivBottomNavigationListener(activity!!)

    override fun initializePresenter() {
        presenter = BaseHivRegisterPresenter(this, BaseHivRegisterModel())
    }

    override fun getRegisterFragment() = BaseHivRegisterFragment()

    override fun getOtherFragments() = arrayOf(Fragment())

    override fun presenter() = presenter as BaseHivRegisterContract.Presenter
}