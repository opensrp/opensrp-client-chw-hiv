package org.smartregister.chw.hiv.dao

import android.database.Cursor
import org.smartregister.chw.anc.domain.Visit
import org.smartregister.chw.hiv.domain.HivMemberObject
import org.smartregister.chw.hiv.util.DBConstants
import org.smartregister.commonregistry.CommonPersonObjectClient
import org.smartregister.dao.AbstractDao
import org.smartregister.dao.AbstractDao.DataMap

object HivDao : AbstractDao() {
    @JvmStatic
    fun isRegisteredForHiv(baseEntityID: String?): Boolean {
        val sql =
            """select count(ec_hiv_register.base_entity_id) count
               from ec_hiv_register
               where base_entity_id = '${baseEntityID}'
               and ec_hiv_register.is_closed = 0
            """

        val dataMap = DataMap { cursor: Cursor? -> getCursorIntValue(cursor, "count") }
        val res = readData(sql, dataMap)
        return if (res == null || res.size != 1) false else res[0]!! > 0
    }

    @JvmStatic
    fun getLatestVisit(baseEntityId: String, visitType: String): Visit? {
        val sql =
            """select visit_id, visit_type, parent_visit_id, visit_date from visits 
                WHERE 
                base_entity_id = '${baseEntityId}' and 
                visit_type = '${visitType}' 
                ORDER BY visit_date DESC LIMIT 1"""
        val visit = readData(sql, visitDataMap)
        return if (visit.size == 0) null else visit[0]
    }

    @JvmStatic
    fun getMember(baseEntityID: String): HivMemberObject? {
        val sql =
            """select m.base_entity_id , m.unique_id , m.relational_id , m.dob , m.first_name , 
                    m.middle_name , m.last_name , m.gender , m.phone_number , m.other_phone_number , 
                    f.first_name family_name ,f.primary_caregiver , f.family_head , f.village_town ,
                    fh.first_name family_head_first_name , fh.middle_name family_head_middle_name , 
                    fh.last_name family_head_last_name, fh.phone_number family_head_phone_number, 
                    pcg.first_name pcg_first_name , pcg.last_name pcg_last_name , pcg.middle_name pcg_middle_name , 
                    pcg.phone_number  pcg_phone_number , mr.* 
                    from ec_family_member m 
                    inner join ec_family f on m.relational_id = f.base_entity_id 
                    inner join ec_hiv_register mr on mr.base_entity_id = m.base_entity_id 
                    left join ec_family_member fh on fh.base_entity_id = f.family_head 
                    left join ec_family_member pcg on pcg.base_entity_id = f.primary_caregiver 
                    where m.base_entity_id ='${baseEntityID}' """
        val dataMap =
            DataMap { cursor: Cursor? ->
                val memberObject = HivMemberObject()
                memberObject.firstName = getCursorValue(cursor, DBConstants.Key.FIRST_NAME, "")
                memberObject.middleName = getCursorValue(cursor, DBConstants.Key.MIDDLE_NAME, "")
                memberObject.lastName = getCursorValue(cursor, DBConstants.Key.LAST_NAME, "")
                memberObject.address = getCursorValue(cursor, DBConstants.Key.VILLAGE_TOWN)!!
                memberObject.gender = getCursorValue(cursor, DBConstants.Key.GENDER)!!
                memberObject.uniqueId = getCursorValue(cursor, DBConstants.Key.UNIQUE_ID, "")
                memberObject.age = getCursorValue(cursor, DBConstants.Key.DOB)!!
                memberObject.familyBaseEntityId =
                    getCursorValue(cursor, DBConstants.Key.RELATIONAL_ID, "")
                memberObject.relationalId =
                    getCursorValue(cursor, DBConstants.Key.RELATIONAL_ID, "")
                memberObject.primaryCareGiver =
                    getCursorValue(cursor, DBConstants.Key.PRIMARY_CARE_GIVER)
                memberObject.familyName = getCursorValue(cursor, DBConstants.Key.FAMILY_NAME, "")
                memberObject.phoneNumber = getCursorValue(cursor, DBConstants.Key.PHONE_NUMBER, "")
                memberObject.baseEntityId =
                    getCursorValue(cursor, DBConstants.Key.BASE_ENTITY_ID, "")
                memberObject.familyHead = getCursorValue(cursor, DBConstants.Key.FAMILY_HEAD, "")
                memberObject.primaryCareGiverPhoneNumber =
                    getCursorValue(cursor, DBConstants.Key.PRIMARY_CARE_GIVER_PHONE_NUMBER, "")
                memberObject.familyHeadPhoneNumber =
                    getCursorValue(cursor, DBConstants.Key.FAMILY_HEAD_PHONE_NUMBER, "")
                memberObject.hivRegistrationDate =
                    getCursorValue(cursor, DBConstants.Key.HIV_REGISTRATION_DATE, "")
                var familyHeadName =
                    (getCursorValue(cursor, "family_head_first_name", "") + " "
                            + getCursorValue(cursor, "family_head_middle_name", ""))
                familyHeadName =
                    (familyHeadName.trim { it <= ' ' } + " " + getCursorValue(
                        cursor, "family_head_last_name", ""
                    )).trim { it <= ' ' }
                memberObject.familyHead = familyHeadName
                var familyPcgName =
                    (getCursorValue(cursor, "pcg_first_name", "") + " "
                            + getCursorValue(cursor, "pcg_middle_name", ""))
                familyPcgName =
                    (familyPcgName.trim { it <= ' ' } + " " + getCursorValue(
                        cursor,
                        "pcg_last_name",
                        ""
                    )).trim { it <= ' ' }
                memberObject.primaryCareGiver = familyPcgName
                memberObject
            }
        val res = readData(sql, dataMap)
        return if (res == null || res.size != 1) null else res[0]
    }

    @JvmStatic
    fun getLatestHivVisit(baseEntityId: String, entityType: String): Visit? {
        val sql =
            """SELECT visit_date, visit_id,visit_type, parent_visit_id
               FROM Visits v
               INNER JOIN ec_hiv_register hv on hv.base_entity_id = v.base_entity_id
               WHERE v.base_entity_id = '${baseEntityId}' COLLATE NOCASE
                    AND v.visit_type = '${entityType}' COLLATE NOCASE
                    AND strftime('%Y%d%m', (datetime(v.visit_date/1000, 'unixepoch')))  >= substr(hv.hiv_registration_date,7,4) || substr(hv.hiv_registration_date,4,2) || substr(hv.hiv_registration_date,1,2)
               "ORDER BY v.visit_date DESC"""
        val visit = readData(sql, visitDataMap)
        return if (visit.size == 0) {
            null
        } else visit[0]
    }

    private val visitDataMap: DataMap<Visit>
        get() = DataMap { c: Cursor? ->
            val visit = Visit()
            visit.visitId = getCursorValue(c, "visit_id")
            visit.parentVisitID = getCursorValue(c, "parent_visit_id")
            visit.visitType = getCursorValue(c, "visit_type")
            visit.date = getCursorValueAsDate(c, "visit_date")
            visit
        }
}