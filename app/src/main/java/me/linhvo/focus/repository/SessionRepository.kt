package me.linhvo.focus.repository

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import me.linhvo.focus.database.dao.Session
import me.linhvo.focus.database.dao.SessionDao
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Instant

@Singleton
class SessionRepository @Inject constructor(val sessionDao: SessionDao) {

    suspend fun getSessionsByDate(requestDate: Instant): List<Session> {
        val localDate = requestDate.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val allSessions = sessionDao.getAllSessions()
        return allSessions.filter { s ->
            s.stopTime?.toLocalDateTime(TimeZone.currentSystemDefault())?.date?.equals(localDate) ?: false
        }
    }

    suspend fun startSession(taskName: String, startTime: Instant): Long =
        sessionDao.addSession(Session(startTime = startTime, taskName = taskName))

    suspend fun stopSession(session: Session) = sessionDao.updateSession(session)

    suspend fun deleteSession(session: Session) = sessionDao.deleteSession(session)
}