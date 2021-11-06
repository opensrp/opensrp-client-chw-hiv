package org.smartregister.chw.hiv.util

import android.content.Context
import com.nerdstone.neatformcore.domain.model.NFormViewData
import org.apache.commons.lang3.StringUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.smartregister.chw.hiv.HivLibrary
import org.smartregister.chw.hiv.util.Constants.Tables
import org.smartregister.clientandeventmodel.Event
import org.smartregister.clientandeventmodel.Obs
import org.smartregister.domain.tag.FormTag
import org.smartregister.repository.AllSharedPreferences
import org.smartregister.util.FormUtils
import org.smartregister.util.JsonFormUtils
import java.util.*

const val METADATA = "metadata"

/**
 * Utility functions for handling JSON forms, extends [JsonFormUtils]
 */
object JsonFormUtils : JsonFormUtils() {
    /**
     * Creates a event using the [valuesHashMap] and [jsonForm] then returns the event,
     * [encounterType] is the name of the form
     */
    @JvmStatic
    @Throws(Exception::class)
    fun processJsonForm(
        hivLibrary: HivLibrary, entityId: String?,
        valuesHashMap: HashMap<String, NFormViewData>, jsonForm: JSONObject?, encounterType: String
    ): Event {
        val bindType: String? = when (encounterType) {
            Constants.EventType.REGISTRATION -> {
                Tables.HIV
            }
            Constants.EventType.FOLLOW_UP_VISIT -> {
                Tables.HIV_FOLLOW_UP
            }
            Constants.EventType.HIV_OUTCOME -> {
                Tables.HIV_OUTCOME
            }
            Constants.EventType.HIV_COMMUNITY_FOLLOWUP -> {
                Tables.HIV_COMMUNITY_FOLLOWUP
            }
            Constants.EventType.HIV_INDEX_CONTACT_TESTING_FOLLOWUP -> {
                Tables.HIV_INDEX_HF
            }
            else -> null
        }
        return createEvent(
            JSONArray(), getJSONObject(jsonForm, METADATA),
            formTag(hivLibrary), entityId, encounterType, bindType
        ).also { it.obs = getObs(valuesHashMap) }
    }

    private fun formTag(hivLibrary: HivLibrary): FormTag =
        FormTag().also {
            it.providerId = hivLibrary.context.allSharedPreferences().fetchRegisteredANM()
            it.appVersion = hivLibrary.appVersion
            it.databaseVersion = hivLibrary.databaseVersion
        }


    /**
     * Applies attributes to [event], also including app and database version from [hivLibrary]
     */
    fun tagEvent(hivLibrary: HivLibrary, event: Event) {
        event.apply {
            with(hivLibrary.context.allSharedPreferences()) {
                providerId = fetchRegisteredANM()
                locationId = locationId(this)
                childLocationId = fetchCurrentLocality()
                team = fetchDefaultTeam(providerId)
                teamId = fetchDefaultTeamId(providerId)
                clientApplicationVersion = hivLibrary.appVersion
                clientDatabaseVersion = hivLibrary.databaseVersion
            }
        }
    }

    private fun locationId(allSharedPreferences: AllSharedPreferences): String {
        val providerId = allSharedPreferences.fetchRegisteredANM()
        return allSharedPreferences.fetchUserLocalityId(providerId).also {
            if (StringUtils.isBlank(it)) {
                return allSharedPreferences.fetchDefaultLocalityId(providerId)
            }
        }
    }

    /**
     * Adds [entityId],[currentLocationId] metadata to the [jsonObject]
     */
    @JvmStatic
    @Throws(JSONException::class)
    fun addFormMetadata(jsonObject: JSONObject, entityId: String?, currentLocationId: String?) {
        jsonObject.getJSONObject(METADATA)
            .put(ENCOUNTER_LOCATION, currentLocationId)
            .put(ENTITY_ID, entityId)
    }

    /**
     * Obtains a json form named [formName] using the provided [context]
     */
    @JvmStatic
    @Throws(Exception::class)
    fun getFormAsJson(formName: String?, context: Context): JSONObject =
        FormUtils.getInstance(context).getFormJson(formName)

    fun getObs(detailsHashMap: HashMap<String, NFormViewData>): List<Obs> {
        val obs = ArrayList<Obs>()
        detailsHashMap.keys.forEach { key ->
            detailsHashMap[key]?.also { viewData ->
                val ob = Obs()
                ob.formSubmissionField = key
                viewData.metadata?.also {
                    if (it.containsKey(OPENMRS_ENTITY))
                        ob.fieldType = it[OPENMRS_ENTITY].toString()
                    if (it.containsKey(OPENMRS_ENTITY_ID))
                        ob.fieldCode = it[OPENMRS_ENTITY_ID].toString()
                    if (it.containsKey(OPENMRS_ENTITY_PARENT))
                        ob.parentCode = it[OPENMRS_ENTITY_PARENT].toString()
                }
                when (viewData.value) {
                    is HashMap<*, *> -> {
                        val humanReadableValues = ArrayList<Any?>()
                        addHumanReadableValues(
                            ob, humanReadableValues, viewData.value as HashMap<*, *>?
                        )
                        ob.humanReadableValues = humanReadableValues
                    }
                    is NFormViewData -> {
                        val humanReadableValues = ArrayList<Any?>()
                        saveValues(viewData.value as NFormViewData?, ob, humanReadableValues)
                        ob.humanReadableValues = humanReadableValues
                    }
                    else -> {
                        ob.value = viewData.value
                        ob.humanReadableValues = ArrayList()
                    }
                }
                obs.add(ob)
            }
        }
        return obs
    }

    private fun addHumanReadableValues(
        obs: Obs, humanReadableValues: MutableList<Any?>, valuesHashMap: HashMap<*, *>?
    ) {
        valuesHashMap!!.keys.forEach { key ->
            valuesHashMap[key]?.also {
                when (it) {
                    is NFormViewData -> {
                        saveValues(it, obs, humanReadableValues)
                    }
                    else -> {
                        obs.value = valuesHashMap[key]
                    }
                }
            }
        }
    }

    private fun saveValues(
        optionsNFormViewData: NFormViewData?, obs: Obs, humanReadableValues: MutableList<Any?>
    ) {
        optionsNFormViewData?.also { viewData ->
            viewData.metadata?.also {
                if (it.containsKey(OPENMRS_ENTITY_ID)) {
                    obs.value = it[OPENMRS_ENTITY_ID]
                    humanReadableValues.add(viewData.value)
                } else {
                    obs.value = optionsNFormViewData.value
                }
            }
        }
    }
}