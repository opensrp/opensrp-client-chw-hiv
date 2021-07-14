package org.smartregister.chw.hiv.activity

import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import org.smartregister.chw.hiv.R
import org.smartregister.chw.hiv.contract.BaseHivRegisterContract
import org.smartregister.chw.hiv.fragment.BaseHivIndexContactsRegisterFragment
import org.smartregister.chw.hiv.util.Constants
import org.smartregister.helper.BottomNavigationHelper

/**
 * Created by cozej4 on 2021-06-21.
 *
 * @cozej4 https://github.com/cozej4
 */

/***
 * This class is for displaying register for all the hiv index contacts
 * that were elicited by positive HIV clients, that should be followed up for HIV testing
 * it implements [BaseHivRegisterContract.View]
 */
open class BaseHivIndexContactsRegisterActivity : BaseHivRegisterActivity(),
    BaseHivRegisterContract.View {

    override fun getViewIdentifiers(): List<String> =
        listOf(Constants.Configuration.HIV_INDEX_REGISTER)

    /**
     * Override this to subscribe to bottom navigation
     */
    override fun registerBottomNavigation() {
        bottomNavigationHelper = BottomNavigationHelper()
        bottomNavigationView =
            findViewById(org.smartregister.R.id.bottom_navigation)
        bottomNavigationView?.also {
            it.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
            it.inflateMenu(menuResource)
            it.menu.removeItem(org.smartregister.R.id.action_clients)
            it.menu.removeItem(R.id.action_register)
            it.menu.removeItem(org.smartregister.R.id.action_search)
            it.menu.removeItem(org.smartregister.R.id.action_library)
            it.menu.removeItem(R.id.action_received_referrals)
            bottomNavigationHelper.disableShiftMode(it)
            it.setOnNavigationItemSelectedListener(getBottomNavigation(this))
        }
    }

    override fun getRegisterFragment() = BaseHivIndexContactsRegisterFragment()

    override fun getOtherFragments() = arrayOf(Fragment())

    override fun presenter() = presenter as BaseHivRegisterContract.Presenter
}