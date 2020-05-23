package org.smartregister.chw.hiv.util

/**
 * Contains constants used through out the application
 */
object Constants {
    const val EN ="en"
    const val SW ="sw"
    const val REQUEST_CODE_GET_JSON = 2244
    const val ENCOUNTER_TYPE = "encounter_type"
    const val STEP_ONE = "step1"
    const val STEP_TWO = "step2"

    object Configuration {
        const val HIV_REGISTER = "hiv_register"
    }

    object ReferralMemberObject {
        const val MEMBER_OBJECT = "memberObject"
        const val COMMON_PERSON_OBJECT = "commonPersonObjectClient"
    }

    object JsonFormExtra {
        const val JSON = "json"
        const val ENCOUNTER_TYPE = "encounter_type"
    }

    object EventType {
        const val REGISTRATION = "HIV Registration"
        const val FOLLOW_UP_VISIT = "HIV Followup"
        const val REFERRAL_FOLLOW_UP_VISIT = "Followup Visit"
    }

    object HivStatus {
        const val POSITIVE = "positive"
        const val NEGATIVE = "negative"
        const val UNKNOWN = "unknown"
    }

    object Tables {
        const val HIV = "ec_hiv_register"
        const val HIV_FOLLOW_UP = "ec_hiv_follow_up"
        const val FAMILY_MEMBER = "ec_family_member"
    }

    object ActivityPayload {
        const val BASE_ENTITY_ID = "BASE_ENTITY_ID"
        const val MEMBER_OBJECT = "MEMBER_OBJECT"
        const val ACTION = "ACTION"
        const val HIV_REGISTRATION_FORM_NAME = "HIV_REGISTRATION_FORM_NAME"
        const val USE_DEFAULT_NEAT_FORM_LAYOUT = "use_default_neat_form_layout"
        const val JSON_FORM = "JSON_FORM"
        const val HIV_FOLLOWUP_FORM_NAME = "HIV_FOLLOWUP_FORM_NAME"
    }

    object ActivityPayloadType {
        const val REGISTRATION = "REGISTRATION"
        const val FOLLOW_UP_VISIT = "FOLLOW_UP_VISIT"
    }
}

object DBConstants {
    object Key {
        const val ID = "id"
        const val FIRST_NAME = "first_name"
        const val MIDDLE_NAME = "middle_name"
        const val LAST_NAME = "last_name"
        const val BASE_ENTITY_ID = "base_entity_id"
        const val FAMILY_BASE_ENTITY_ID = "family_base_entity_id"
        const val DOB = "dob"
        const val DOD = "dod"
        const val UNIQUE_ID = "unique_id"
        const val VILLAGE_TOWN = "village_town"
        const val DATE_REMOVED = "date_removed"
        const val GENDER = "gender"
        const val DETAILS = "details"
        const val RELATIONAL_ID = "relationalid"
        const val FAMILY_HEAD = "family_head"
        const val PRIMARY_CARE_GIVER = "primary_caregiver"
        const val PRIMARY_CARE_GIVER_PHONE_NUMBER = "pcg_phone_number"
        const val FAMILY_NAME = "family_name"
        const val PHONE_NUMBER = "phone_number"
        const val OTHER_PHONE_NUMBER = "other_phone_number"
        const val FAMILY_HEAD_PHONE_NUMBER = "family_head_phone_number"
        const val HIV_REGISTRATION_DATE = "hiv_registration_date"
        const val HIV_STATUS = "hiv_status"
        const val CTC_NUMBER = "ctc_number"
        const val CBHS_NUMBER = "cbhs_number"
        const val CLIENT_HIV_STATUS_DURING_REGISTRATION = "client_hiv_status_during_registration"
        const val CLIENT_HIV_STATUS_AFTER_TESTING = "client_hiv_status_after_testing"
        const val IS_CLOSED = "is_closed"
        const val CHW_FOLLOWUP_DATE = "chw_followup_date"
    }
}


