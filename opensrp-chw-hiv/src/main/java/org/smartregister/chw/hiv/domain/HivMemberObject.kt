package org.smartregister.chw.hiv.domain

import java.io.Serializable
import java.util.*


/**
 * This class wraps [client] into a new entity class used to pass data between activities,
 * it implements [Serializable]
 */
class HivMemberObject : Serializable {

    var firstName: String? = null
    var middleName: String? = null
    var lastName: String? = null
    var address: String? = null
    var gender: String? = null
    var uniqueId: String? = null
    var age: String? = null
    var relationalid: String? = null
    var baseEntityId: String? = null
    var relationalId: String? = null
    var primaryCareGiver: String? = null
    var primaryCareGiverPhoneNumber: String? = null
    var familyHead: String? = null
    var familyBaseEntityId: String? = null
    var familyName: String? = null
    var phoneNumber: String? = null
    var familyHeadPhoneNumber: String? = null
    var otherPhoneNumber: String? = null
    var ctcNumber: String? = null
    var cbhsNumber: String? = null
    var clientHivStatusDuringRegistration: String? = null
    var clientHivStatusAfterTesting: String? = null
    var hivRegistrationDate: Date? = null
    var isClosed: Boolean? = null
}