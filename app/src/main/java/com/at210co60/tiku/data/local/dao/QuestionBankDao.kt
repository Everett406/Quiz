package com.at210co60.tiku.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.at210co60.tiku.data.local.entity.QuestionBankEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionBankDao {
    @Query("SELECT * FROM question_banks ORDER BY createdAt DESC")
    fun getAllQuestionBanks(): Flow<List<QuestionBankEntity>>

    @Query("SELECT * FROM question_banks WHERE id = :id")
    suspend fun getQuestionBankById(id: Long): QuestionBankEntity?

    @Query("SELECT * FROM question_banks WHERE isDefault = 1 LIMIT 1")
    suspend fun getDefaultQuestionBank(): QuestionBankEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bank: QuestionBankEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(banks: List<QuestionBankEntity>)

    @Query("DELETE FROM question_banks WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM question_banks")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM questions WHERE questionBankId = :bankId")
    suspend fun getQuestionCountByBank(bankId: Long): Int
}
