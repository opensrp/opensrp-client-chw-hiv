package org.smartregister.chw.hiv.domain

import org.smartregister.chw.hiv.util.DBConstants
import org.smartregister.commonregistry.CommonPersonObjectClient
import java.io.Serializable
import java.util.*


/**
 * This class wraps [client] into a new entity class used to pass data between activities,
 * it implements [Serializable]
 */
data class HivIndexContactObject(val client: CommonPersonObjectClient?) : Serializable {

    var firstName: String? = client?.columnmaps?.get(DBConstants.Key.FIRST_NAME) ?: ""
    var middleName: String? = client?.columnmaps?.get(DBConstants.Key.MIDDLE_NAME) ?: ""
    var lastName: String? = client?.columnmaps?.get(DBConstants.Key.LAST_NAME) ?: ""
    var address: String? = client?.columnmaps?.get(DBConstants.Key.VILLAGE_TOWN) ?: ""
    var gender: String? = client?.columnmaps?.get(DBConstants.Key.GENDER) ?: ""
    var uniqueId: String? = client?.columnmaps?.get(DBConstants.Key.UNIQUE_ID)
    var dob: String? = client?.columnmaps?.get(DBConstants.Key.DOB) ?: ""
    var relationalid: String? = client?.columnmaps?.get(DBConstants.Key.RELATIONAL_ID)
    var baseEntityId: String? = client?.columnmaps?.get(DBConstants.Key.BASE_ENTITY_ID)
    var relationalId: String? = client?.columnmaps?.get(DBConstants.Key.RELATIONAL_ID)
    var familyHead: String? = client?.columnmaps?.get(DBConstants.Key.FAMILY_HEAD)
    var familyBaseEntityId: String? = client?.columnmaps?.get(DBConstants.Key.RELATIONAL_ID)
    var familyName: String? = client?.columnmaps?.get(DBConstants.Key.FAMILY_NAME)
    var phoneNumber: String? = client?.columnmaps?.get(DBConstants.Key.PHONE_NUMBER)
    var familyHeadPhoneNumber: String? = client?.columnmaps?.get(DBConstants.Key.FAMILY_HEAD_PHONE_NUMBER)
    var otherPhoneNumber: String? = client?.columnmaps?.get(DBConstants.Key.OTHER_PHONE_NUMBER)
    var ctcNumber: String? = client?.columnmaps?.get(DBConstants.Key.CTC_NUMBER)


    var clientHivStatusAfterTesting: String? = client?.columnmaps?.get(DBConstants.Key.CLIENT_HIV_STATUS_AFTER_TESTING)
    var hivClientId: String? = client?.columnmaps?.get(DBConstants.Key.HIV_CLIENT_ID)

    var readinessToTestForHiv: Boolean? = client?.columnmaps?.get(DBConstants.Key.READINESS_TO_TEST_FOR_HIV) == "1"
    var hasJoinedHomeBasedServices: Boolean? = client?.columnmaps?.get(DBConstants.Key.HAS_JOINED_HOME_BASED_SERVICES) == "1"
    var hasStartedMediation: Boolean? = client?.columnmaps?.get(DBConstants.Key.HAS_STARTED_MEDICATION) == "1"
    var hasClientBeenTestedForHiv: Boolean? = client?.columnmaps?.get(DBConstants.Key.HAS_THE_CLIENT_BEEN_TESTED_FOR_HIV) == "1"

    var gbvAnalysis: String? = client?.columnmaps?.get(DBConstants.Key.GBV_ANALYSIS)
    var notificationMethod: String? = client?.columnmaps?.get(DBConstants.Key.NOTIFICATION_METHOD)
    var comments: String? = client?.columnmaps?.get(DBConstants.Key.COMMENTS)

    var hivIndexRegistrationDate: Date? = null
    var isClosed: Boolean? = client?.columnmaps?.get(DBConstants.Key.IS_CLOSED) == "1"
}