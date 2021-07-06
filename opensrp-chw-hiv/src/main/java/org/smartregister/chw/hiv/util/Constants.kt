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
        const val HIV_INDEX_REGISTER = "hiv_index_register"
    }

    object EventType {
        const val REGISTRATION = "HIV Registration"
        const val HIV_OUTCOME = "HIV Outcome"
        const val HIV_COMMUNITY_FOLLOWUP = "HIV Community Followup"
        const val HIV_INDEX_CONTACT_COMMUNITY_FOLLOWUP = "HIV Index Contact Community Followup Referral"
        const val HIV_INDEX_CONTACT_TESTING_FOLLOWUP = "HIV Index Contact Testing Followup"
        const val HIV_COMMUNITY_FOLLOWUP_FEEDBACK = "HIV Community Followup Feedback"
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
        const val HIV_INDEX = "ec_hiv_index"
        const val HIV_FOLLOW_UP = "ec_hiv_follow_up_visit"
        const val HIV_OUTCOME = "ec_hiv_outcome"
        const val HIV_COMMUNITY_FOLLOWUP = "ec_hiv_community_followup"
        const val HIV_COMMUNITY_FEEDBACK = "ec_hiv_community_feedback"
        const val FAMILY_MEMBER = "ec_family_member"
    }

    object ActivityPayload {
        const val BASE_ENTITY_ID = "BASE_ENTITY_ID"
        const val HIV_MEMBER_OBJECT = "HIV_MEMBER_OBJECT"
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

    object FamilyMemberEntityType {
        const val EC_INDEPENDENT_CLIENT = "ec_independent_client"
    }
}

object DBConstants {
    object Key {
        const val ID = "id"
        const val FIRST_NAME = "first_name"
        const val MIDDLE_NAME = "middle_name"
        const val LAST_NAME = "last_name"
        const val BASE_ENTITY_ID = "base_entity_id"
        const val ENTITY_ID = "entity_id"
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
        const val FAMILY_MEMBER_ENTITY_TYPE = "entity_type"
        const val CHW_FOLLOWUP_DATE = "chw_followup_date"
        const val HIV_COMMUNITY_FOLLOWUP_VISIT_DATE = "hiv_community_followup_visit_date"
        const val REASONS_FOR_ISSUING_COMMUNITY_REFERRAL = "reasons_for_issuing_community_referral"
        const val HIV_COMMUNITY_REFERRAL_DATE = "hiv_community_referral_date"
        const val LAST_INTERACTED_WITH = "last_interacted_with"
        const val LAST_FACILITY_VISIT_DATE = "last_client_visit_date"
        const val COMMENTS = "comment"
        const val COMMUNITY_REFERRAL_FORM_ID = "community_referral_form_id"
        const val CHW_NAME = "chw_name"
        const val HIV_CLIENT_ID = "hiv_client_id"
        const val HIV_TEST_ELIGIBILITY = "hiv_test_eligibility"
        const val RELATIONSHIP = "relationship"
        const val HAS_STARTED_MEDICATION = "has_started_medication"
        const val HOW_TO_NOTIFY_CONTACT_CLIENT = "how_to_notify_the_contact_client"
        const val HAS_THE_CONTACT_CLIENT_BEEN_TESTED = "has_the_contact_client_been_tested"
        const val ENROLLED_TO_CLINIC = "enrolled_to_clinic"
        const val REFER_TO_CHW = "refer_to_chw"
        const val FOLLOWED_UP_BY_CHW = "client_followed_up_by_chw"
        const val HIV_INDEX_REGISTRATION_DATE = "hiv_index_registration_date"
        const val TEST_RESULTS = "test_results"
        const val PLACE_WHERE_TEST_WAS_CONDUCTED = "place_where_test_was_conducted"
    }
}


