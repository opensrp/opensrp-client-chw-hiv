package org.smartregister.chw.hiv.dao

import android.database.Cursor
import org.smartregister.chw.hiv.domain.HivIndexObject
import org.smartregister.chw.hiv.util.DBConstants
import org.smartregister.dao.AbstractDao
import org.smartregister.dao.AbstractDao.DataMap
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

object HivIndexDao : AbstractDao() {
    private val dataMap =
        DataMap { cursor: Cursor? ->
            val memberObject = HivIndexObject(null)
            memberObject.firstName = getCursorValue(cursor, DBConstants.Key.FIRST_NAME, "")
            memberObject.middleName = getCursorValue(cursor, DBConstants.Key.MIDDLE_NAME, "")
            memberObject.lastName = getCursorValue(cursor, DBConstants.Key.LAST_NAME, "")
            memberObject.address = getCursorValue(cursor, DBConstants.Key.VILLAGE_TOWN, "")
            memberObject.gender = getCursorValue(cursor, DBConstants.Key.GENDER)!!
            memberObject.uniqueId = getCursorValue(cursor, DBConstants.Key.UNIQUE_ID, "")
            memberObject.dob = getCursorValue(cursor, DBConstants.Key.DOB)!!
            memberObject.familyBaseEntityId =
                getCursorValue(cursor, DBConstants.Key.FAMILY_BASE_ENTITY_ID, "")
            memberObject.relationalId =
                getCursorValue(cursor, DBConstants.Key.FAMILY_BASE_ENTITY_ID, "")
            memberObject.otherPhoneNumber =
                getCursorValue(cursor, DBConstants.Key.OTHER_PHONE_NUMBER)
            memberObject.hivClientId =
                getCursorValue(cursor, DBConstants.Key.HIV_CLIENT_ID)

            memberObject.familyName = getCursorValue(cursor, DBConstants.Key.FAMILY_NAME, "")
            memberObject.phoneNumber = getCursorValue(cursor, DBConstants.Key.PHONE_NUMBER, "")
            memberObject.baseEntityId =
                getCursorValue(cursor, DBConstants.Key.BASE_ENTITY_ID, "")
            memberObject.familyHead = getCursorValue(cursor, DBConstants.Key.FAMILY_HEAD, "")
            memberObject.familyHeadPhoneNumber =
                getCursorValue(cursor, DBConstants.Key.FAMILY_HEAD_PHONE_NUMBER, "")
            memberObject.ctcNumber =
                getCursorValue(cursor, DBConstants.Key.CTC_NUMBER, "")

            memberObject.gbvAnalysis =
                getCursorValue(cursor, DBConstants.Key.CBHS_NUMBER, "")

            memberObject.notificationMethod =
                getCursorValue(cursor, DBConstants.Key.NOTIFICATION_METHOD, "")

            memberObject.clientHivStatusAfterTesting =
                getCursorValue(cursor, DBConstants.Key.CLIENT_HIV_STATUS_AFTER_TESTING, "")


            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            sdf.timeZone = TimeZone.getTimeZone("+03:00")

            memberObject.hivIndexRegistrationDate = sdf.parse(getCursorValue(
                cursor,
                DBConstants.Key.HIV_INDEX_REGISTRATION_DATE, ""
            ))

            memberObject.comments =
                getCursorValue(cursor, DBConstants.Key.COMMENTS, "")
            memberObject.readinessToTestForHiv =
                getCursorIntValue(cursor, DBConstants.Key.READINESS_TO_TEST_FOR_HIV, 0) == 1
            memberObject.hasJoinedHomeBasedServices =
                getCursorIntValue(cursor, DBConstants.Key.HAS_JOINED_HOME_BASED_SERVICES, 0) == 1
            memberObject.hasStartedMediation =
                getCursorIntValue(cursor, DBConstants.Key.HAS_STARTED_MEDICATION, 0) == 1
            memberObject.hasClientBeenTestedForHiv =
                getCursorIntValue(
                    cursor,
                    DBConstants.Key.HAS_THE_CLIENT_BEEN_TESTED_FOR_HIV,
                    0
                ) == 1
            memberObject.isClosed =
                getCursorIntValue(cursor, DBConstants.Key.IS_CLOSED, 0) == 1
            var familyHeadName =
                (getCursorValue(cursor, "family_head_first_name", "") + " "
                        + getCursorValue(cursor, "family_head_middle_name", ""))
            familyHeadName =
                (familyHeadName.trim { it <= ' ' } + " " + getCursorValue(
                    cursor, "family_head_last_name", ""
                )).trim { it <= ' ' }
            memberObject.familyHead = familyHeadName

            memberObject
        }

    @JvmStatic
    fun isRegisteredIndex(baseEntityID: String?): Boolean {
        val sql =
            """select count(ec_hiv_index.base_entity_id) count
               from ec_hiv_index
               where base_entity_id = '${baseEntityID}'
               and ec_hiv_index.is_closed = 0
            """

        val dataMap = DataMap { cursor: Cursor? -> getCursorIntValue(cursor, "count") }
        val res = readData(sql, dataMap)
        return if (res == null || res.size != 1) false else res[0]!! > 0
    }

    @JvmStatic
    fun getMember(baseEntityID: String): HivIndexObject? {
        val sql =
            """select m.base_entity_id , m.unique_id , m.relational_id as family_base_entity_id , m.dob , m.first_name , 
                    m.middle_name , m.last_name , m.gender , m.phone_number , m.other_phone_number , m.entity_type, m.has_primary_caregiver, m.has_primary_caregiver, m.primary_caregiver_name,
                    f.first_name family_name ,f.primary_caregiver , f.family_head , f.village_town ,
                    fh.first_name family_head_first_name , fh.middle_name family_head_middle_name , 
                    fh.last_name family_head_last_name, fh.phone_number family_head_phone_number, 
                    pcg.first_name pcg_first_name , pcg.last_name pcg_last_name , pcg.middle_name pcg_middle_name , 
                    pcg.phone_number  pcg_phone_number , mr.*

                from ec_family_member m 
                    inner join ec_family f on m.relational_id = f.base_entity_id 
                    left join ec_hiv_index mr on mr.base_entity_id = m.base_entity_id 
                    left join ec_family_member fh on fh.base_entity_id = f.family_head 
                    left join ec_family_member pcg on pcg.base_entity_id = f.primary_caregiver 
                    where m.base_entity_id ='${baseEntityID}' """
        val sqlHf =
            """select m.base_entity_id , m.unique_id , m.relational_id as family_base_entity_id , m.dob , m.first_name , 
                    m.middle_name , m.last_name , m.gender , m.phone_number , m.other_phone_number , m.entity_type,
                    f.first_name family_name ,f.primary_caregiver , f.family_head , f.village_town ,
                    fh.first_name family_head_first_name , fh.middle_name family_head_middle_name , 
                    fh.last_name family_head_last_name, fh.phone_number family_head_phone_number, 
                    pcg.first_name pcg_first_name , pcg.last_name pcg_last_name , pcg.middle_name pcg_middle_name , 
                    pcg.phone_number  pcg_phone_number , mr.* 
                    from ec_family_member m 
                    inner join ec_family f on m.relational_id = f.base_entity_id 
                    left join ec_hiv_index mr on mr.base_entity_id = m.base_entity_id 
                    left join ec_family_member fh on fh.base_entity_id = f.family_head 
                    left join ec_family_member pcg on pcg.base_entity_id = f.primary_caregiver 
                    where m.base_entity_id ='${baseEntityID}' """

        var res = readData(sql, dataMap)
        if (res == null)
            res = readData(sqlHf, dataMap)
        return if (res == null || res.size == 0) null else res[0]
    }

    @JvmStatic
    fun getHivClientIndexes(baseEntityID: String): List<HivIndexObject>? {
        val sql =
            """select m.base_entity_id , m.unique_id , m.relational_id as family_base_entity_id , m.dob , m.first_name , 
                    m.middle_name , m.last_name , m.gender , m.phone_number , m.other_phone_number , m.entity_type, m.has_primary_caregiver, m.has_primary_caregiver, m.primary_caregiver_name,
                    f.first_name family_name ,f.primary_caregiver , f.family_head , f.village_town ,
                    fh.first_name family_head_first_name , fh.middle_name family_head_middle_name , 
                    fh.last_name family_head_last_name, fh.phone_number family_head_phone_number, 
                    pcg.first_name pcg_first_name , pcg.last_name pcg_last_name , pcg.middle_name pcg_middle_name , 
                    pcg.phone_number  pcg_phone_number , mr.*

                from ec_family_member m 
                    inner join ec_family f on m.relational_id = f.base_entity_id 
                    inner join ec_hiv_index mr on mr.base_entity_id = m.base_entity_id 
                    left join ec_family_member fh on fh.base_entity_id = f.family_head 
                    left join ec_family_member pcg on pcg.base_entity_id = f.primary_caregiver 
                    where mr.hiv_client_id ='${baseEntityID}' """
        val sqlHf =
            """select m.base_entity_id , m.unique_id , m.relational_id as family_base_entity_id , m.dob , m.first_name , 
                    m.middle_name , m.last_name , m.gender , m.phone_number , m.other_phone_number , m.entity_type,
                    f.first_name family_name ,f.primary_caregiver , f.family_head , f.village_town ,
                    fh.first_name family_head_first_name , fh.middle_name family_head_middle_name , 
                    fh.last_name family_head_last_name, fh.phone_number family_head_phone_number, 
                    pcg.first_name pcg_first_name , pcg.last_name pcg_last_name , pcg.middle_name pcg_middle_name , 
                    pcg.phone_number  pcg_phone_number , mr.* 
                    from ec_family_member m 
                    inner join ec_family f on m.relational_id = f.base_entity_id 
                    inner join ec_hiv_index mr on mr.base_entity_id = m.base_entity_id 
                    left join ec_family_member fh on fh.base_entity_id = f.family_head 
                    left join ec_family_member pcg on pcg.base_entity_id = f.primary_caregiver 
                    where mr.hiv_client_id ='${baseEntityID}' """

        var res = readData(sql, dataMap)
        if (res == null)
            res = readData(sqlHf, dataMap)
        return if (res == null || res.size == 0) null else res
    }

}