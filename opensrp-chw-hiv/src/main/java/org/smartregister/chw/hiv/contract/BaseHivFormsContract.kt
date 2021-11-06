package org.smartregister.chw.hiv.contract

import android.content.Intent
import com.nerdstone.neatformcore.domain.model.NFormViewData
import org.apache.commons.lang3.tuple.Triple
import org.json.JSONObject
import org.koin.core.KoinComponent
import org.smartregister.chw.hiv.domain.HivMemberObject
import org.smartregister.domain.Location
import java.util.*

/**
 * Contract for BaseHivRegistrationForms
 */
interface BaseHivFormsContract {

    /**
     * defines methods to implement for the view, also extends [KoinComponent]
     */
    interface View : KoinComponent {

        /**
         *Used to get the instance for [Presenter]
         */
        fun presenter(): Presenter

        /**
         * passes data pertaining to the client's profile to the [View]
         */
        fun setProfileViewWithData()


        /**
         * passes data back to the source activity's on activity results
         */
        fun setDataToBePassedBackToCallingActivityAsResults(
            intent: Intent,
            jsonForm: JSONObject,
            formData: HashMap<String, NFormViewData>
        )
    }

    /**
     * Presenter contract for hiv registration forms functionality
     */
    interface Presenter {

        /**
         * retrieves the [View]
         */
        fun getView(): View?

        /**
         * returns where clause [String] passed on the query
         */
        fun getMainCondition(): String

        /**
         * Returns the name of the table used in local database as [String]
         */
        fun getMainTable(): String

        /**
         * copies [hivMemberObject] to the same property of the view model
         */
        fun fillClientData(hivMemberObject: HivMemberObject?)

        /**
         * sets the value of [hivMemberObject]
         */
        fun initializeMemberObject(hivMemberObject: HivMemberObject?)

        /**
         * Saves referral data retrieved from [valuesHashMap] and uses the [jsonObject] to create a
         * referral event
         */
        fun saveForm(valuesHashMap: HashMap<String, NFormViewData>, jsonObject: JSONObject)
    }

    /**
     * Implementation for BaseHivRegistrationForms Model
     */
    interface Model {

        /**
         * Returns the location id given [locationName]
         */
        fun getLocationId(locationName: String?): String?

        /**
         * Returns a generated query given [tableName] and [mainCondition]
         */
        fun mainSelect(tableName: String, mainCondition: String): String

        /**
         * Returns a list of [Location] for the health facilities
         */
        val healthFacilities: List<Location>?
    }

    /**
     * Interactor implementation
     */
    interface Interactor : KoinComponent {

        /**
         * Saves referral for the given [baseEntityId] using the values obtained from the views [valuesHashMap]
         * then creates an event for the form using the provided [jsonObject] and call the [callBack] method
         */
        fun saveRegistration(
            baseEntityId: String, valuesHashMap: HashMap<String, NFormViewData>,
            jsonObject: JSONObject, callBack: InteractorCallBack
        )
    }

    interface InteractorCallBack {

        fun onUniqueIdFetched(triple: Triple<String, String, String>, entityId: String)

        fun onNoUniqueId()

        fun onRegistrationSaved(saveSuccessful: Boolean, encounterType: String)
    }
}