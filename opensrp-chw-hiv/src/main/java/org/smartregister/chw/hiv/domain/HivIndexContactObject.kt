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


    var hivStatus: String? = client?.columnmaps?.get(DBConstants.Key.HIV_STATUS)
    var testResults: String? = client?.columnmaps?.get(DBConstants.Key.TEST_RESULTS)
    var hivClientId: String? = client?.columnmaps?.get(DBConstants.Key.HIV_CLIENT_ID)
    var relationship: String? = client?.columnmaps?.get(DBConstants.Key.RELATIONSHIP)

    var hivTestEligibility: Boolean? = client?.columnmaps?.get(DBConstants.Key.HIV_TEST_ELIGIBILITY) == "true"
    var referToChw: Boolean? = client?.columnmaps?.get(DBConstants.Key.REFER_TO_CHW) == "yes"
    var followedUpByChw: Boolean? = client?.columnmaps?.get(DBConstants.Key.FOLLOWED_UP_BY_CHW) == "true"
    var enrolledToClinic: Boolean? = client?.columnmaps?.get(DBConstants.Key.ENROLLED_TO_CLINIC) == "yes"
    var hasStartedMediation: Boolean? = client?.columnmaps?.get(DBConstants.Key.HAS_STARTED_MEDICATION) == "yes"
    var hasTheContactClientBeenTested: String? = client?.columnmaps?.get(DBConstants.Key.HAS_THE_CONTACT_CLIENT_BEEN_TESTED)

    var contactClientNotificationMethod: String? = client?.columnmaps?.get(DBConstants.Key.HOW_TO_NOTIFY_CONTACT_CLIENT)
    var comments: String? = client?.columnmaps?.get(DBConstants.Key.COMMENTS)

    var hivIndexRegistrationDate: Date? = null
    var isClosed: Boolean? = client?.columnmaps?.get(DBConstants.Key.IS_CLOSED) == "1"
}