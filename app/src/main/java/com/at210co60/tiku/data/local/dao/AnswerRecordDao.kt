package com.at210co60.tiku.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.at210co60.tiku.data.local.entity.AnswerRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnswerRecordDao {
    @Query("SELECT * FROM answer_records WHERE questionBankId = :bankId ORDER BY answeredAt DESC")
    fun getRecordsByBank(bankId: Long): Flow<List<AnswerRecordEntity>>

    @Query("SELECT * FROM answer_records WHERE isCorrect = 0 ORDER BY answeredAt DESC")
    fun getWrongRecords(): Flow<List<AnswerRecordEntity>>

    @Query("SELECT * FROM answer_records WHERE questionId = :questionId ORDER BY answeredAt DESC")
    fun getRecordsByQuestion(questionId: Long): Flow<List<AnswerRecordEntity>>

    @Query("SELECT COUNT(*) FROM answer_records WHERE questionBankId = :bankId AND isCorrect = 1")
    suspend fun getCorrectCountByBank(bankId: Long): Int

    @Query("SELECT COUNT(*) FROM answer_records WHERE questionBankId = :bankId AND isCorrect = 0")
    suspend fun getWrongCountByBank(bankId: Long): Int

    @Query("SELECT COUNT(*) FROM answer_records WHERE questionBankId = :bankId")
    suspend fun getAnsweredCountByBank(bankId: Long): Int

    @Query("SELECT COUNT(DISTINCT questionId) FROM answer_records WHERE questionBankId = :bankId AND isCorrect = 0")
    suspend fun getWrongQuestionCountByBank(bankId: Long): Int

    @Query("SELECT COUNT(DISTINCT questionId) FROM answer_records WHERE questionBankId = :bankId AND isCorrect = 0")
    fun getWrongQuestionCountByBankFlow(bankId: Long): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: AnswerRecordEntity): Long

    @Query("DELETE FROM answer_records WHERE questionId = :questionId")
    suspend fun deleteByQuestionId(questionId: Long)

    @Query("DELETE FROM answer_records WHERE questionBankId = :bankId")
    suspend fun deleteByBankId(bankId: Long)

    @Query("DELETE FROM answer_records")
    suspend fun deleteAll()
}
