package me.linhvo.focus.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformLatest
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.linhvo.focus.database.dao.Session
import me.linhvo.focus.database.dao.SessionDao
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Instant

@Singleton
class SessionRepository @Inject constructor(val sessionDao: SessionDao) {
    suspend fun getSessionById(id: Long): Session? = sessionDao.getSessionById(id)

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeSessionsByDate(requestDate: Instant): Flow<List<Session>> {
        val localDate = requestDate.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val allSessions = sessionDao.observeAllSessions()
        return allSessions.transformLatest { s ->
            val filtered = s.filter {
                it.stopTime?.toLocalDateTime(TimeZone.currentSystemDefault())?.date?.equals(localDate) ?: false
            }
            emit(filtered)
        }
    }

    suspend fun startSession(taskName: String, startTime: Instant): Long =
        sessionDao.addSession(Session(startTime = startTime, taskName = taskName))

    suspend fun updateSession(session: Session) = sessionDao.updateSession(session)

    suspend fun deleteSession(session: Session) = sessionDao.deleteSession(session)
}