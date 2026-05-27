package com.at210co60.tiku.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.at210co60.tiku.data.local.entity.QuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Query("SELECT * FROM questions ORDER BY id ASC")
    fun getAllQuestions(): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions ORDER BY RANDOM() LIMIT :limit")
    fun getRandomQuestions(limit: Int): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE id = :id")
    suspend fun getQuestionById(id: Long): QuestionEntity?

    @Query("SELECT COUNT(*) FROM questions")
    suspend fun getQuestionCount(): Int

    @Query("SELECT * FROM questions WHERE questionBankId = :bankId ORDER BY id ASC")
    fun getQuestionsByBank(bankId: Long): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE questionBankId = :bankId ORDER BY RANDOM() LIMIT :limit")
    fun getRandomQuestionsByBank(bankId: Long, limit: Int): Flow<List<QuestionEntity>>

    @Query("SELECT COUNT(*) FROM questions WHERE questionBankId = :bankId")
    suspend fun getQuestionCountByBank(bankId: Long): Int

    @Query("SELECT COUNT(*) FROM questions WHERE questionBankId = :bankId")
    fun getQuestionCountByBankFlow(bankId: Long): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: QuestionEntity)

    @Query("DELETE FROM questions")
    suspend fun deleteAllQuestions()
}
