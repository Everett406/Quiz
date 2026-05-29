package com.at210co60.tiku.data.repository

import com.at210co60.tiku.data.local.dao.AnswerRecordDao
import com.at210co60.tiku.data.local.dao.QuestionBankDao
import com.at210co60.tiku.data.local.dao.QuestionDao
import com.at210co60.tiku.data.local.entity.AnswerRecordEntity
import com.at210co60.tiku.data.local.entity.QuestionBankEntity
import com.at210co60.tiku.data.local.entity.QuestionEntity
import com.at210co60.tiku.data.model.AnswerRecord
import com.at210co60.tiku.data.model.BankStats
import com.at210co60.tiku.data.model.Question
import com.at210co60.tiku.data.model.QuestionBank
import com.at210co60.tiku.data.model.QuestionType
import com.at210co60.tiku.data.model.WrongRecordWithQuestion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true }

class QuestionRepository(
    private val questionDao: QuestionDao,
    private val questionBankDao: QuestionBankDao,
    private val answerRecordDao: AnswerRecordDao,
) {
    // QuestionBank operations
    fun getAllQuestionBanks(): Flow<List<QuestionBank>> =
        questionBankDao.getAllQuestionBanks().map { entities ->
            entities.map { entity ->
                QuestionBank(
                    id = entity.id,
                    name = entity.name,
                    description = entity.description,
                    questionCount = questionBankDao.getQuestionCountByBank(entity.id),
                    isDefault = entity.isDefault,
                )
            }
        }

    fun getAllQuestionBanksWithCount(): Flow<List<QuestionBank>> =
        questionBankDao.getAllQuestionBanks().map { entities ->
            entities.map { entity ->
                QuestionBank(
                    id = entity.id,
                    name = entity.name,
                    description = entity.description,
                    questionCount = questionBankDao.getQuestionCountByBank(entity.id),
                    isDefault = entity.isDefault,
                )
            }
        }

    suspend fun getQuestionBankById(id: Long): QuestionBank? =
        questionBankDao.getQuestionBankById(id)?.let { entity ->
            QuestionBank(
                id = entity.id,
                name = entity.name,
                description = entity.description,
                questionCount = questionBankDao.getQuestionCountByBank(entity.id),
                isDefault = entity.isDefault,
            )
        }

    suspend fun insertQuestionBank(questionBank: QuestionBank): Long =
        questionBankDao.insert(
            QuestionBankEntity(
                id = questionBank.id,
                name = questionBank.name,
                description = questionBank.description,
                isDefault = questionBank.isDefault,
            )
        )

    suspend fun deleteQuestionBank(id: Long) =
        questionBankDao.deleteById(id)

    // Question operations
    fun getQuestionsByBank(bankId: Long): Flow<List<Question>> =
        questionDao.getQuestionsByBank(bankId).map { entities ->
            entities.map { it.toDomain() }
        }

    fun getRandomQuestionsByBank(bankId: Long, limit: Int): Flow<List<Question>> =
        questionDao.getRandomQuestionsByBank(bankId, limit).map { entities ->
            entities.map { it.toDomain() }
        }

    fun getAllQuestions(): Flow<List<Question>> =
        questionDao.getAllQuestions().map { entities -> entities.map { it.toDomain() } }

    suspend fun insertQuestions(questions: List<Question>, bankId: Long) {
        questionDao.insertQuestions(questions.map { it.toEntity(bankId) })
    }

    suspend fun importFromJson(jsonString: String, bankId: Long) {
        val questions = json.decodeFromString<List<QuestionImportDto>>(jsonString)
        questionDao.insertQuestions(questions.map { it.toEntity(bankId) })
    }

    // Answer record operations
    suspend fun recordAnswer(
        questionId: Long,
        bankId: Long,
        userAnswer: String,
        isCorrect: Boolean,
        practiceMode: String,
    ) {
        answerRecordDao.insert(
            AnswerRecordEntity(
                questionId = questionId,
                questionBankId = bankId,
                userAnswer = userAnswer,
                isCorrect = isCorrect,
                practiceMode = practiceMode,
            )
        )
    }

    fun getBankStats(bankId: Long): Flow<BankStats> =
        combine(
            questionDao.getQuestionCountByBankFlow(bankId),
            answerRecordDao.getWrongQuestionCountByBankFlow(bankId),
        ) { total, wrongCount ->
            BankStats(
                bankId = bankId,
                totalQuestions = total,
                wrongAnswers = wrongCount,
            )
        }

    suspend fun getBankStatsSnapshot(bankId: Long): BankStats {
        val total = questionDao.getQuestionCountByBank(bankId)
        val answered = answerRecordDao.getAnsweredCountByBank(bankId)
        val correct = answerRecordDao.getCorrectCountByBank(bankId)
        val wrong = answerRecordDao.getWrongCountByBank(bankId)
        return BankStats(
            bankId = bankId,
            totalQuestions = total,
            answeredQuestions = answered,
            correctAnswers = correct,
            wrongAnswers = wrong,
        )
    }

    fun getWrongRecords(): Flow<List<AnswerRecord>> =
        answerRecordDao.getWrongRecords().map { entities ->
            entities.map { it.toDomain() }
        }

    fun getWrongRecordsWithQuestions(): Flow<List<WrongRecordWithQuestion>> =
        answerRecordDao.getWrongRecordsWithQuestions().map { entities ->
            entities.map { entity ->
                WrongRecordWithQuestion(
                    record = entity.record.toDomain(),
                    questionTitle = entity.questionTitle,
                    questionType = QuestionType.valueOf(entity.questionType),
                    questionOptions = json.decodeFromString(entity.questionOptions),
                    questionAnswers = json.decodeFromString(entity.questionAnswers),
                    questionExplanation = entity.questionExplanation,
                )
            }
        }

    suspend fun deleteWrongRecord(questionId: Long) {
        answerRecordDao.deleteByQuestionId(questionId)
    }

    suspend fun clearAllData() {
        answerRecordDao.deleteAll()
        questionDao.deleteAllQuestions()
        questionBankDao.deleteAll()
    }
}

private fun QuestionEntity.toDomain(): Question = Question(
    id = id,
    title = title,
    type = QuestionType.valueOf(type),
    options = json.decodeFromString<List<String>>(options),
    answers = json.decodeFromString<List<String>>(answers.ifEmpty { "[]" }),
    explanation = explanation,
    tags = json.decodeFromString<List<String>>(tags.ifEmpty { "[]" }),
)

private fun Question.toEntity(bankId: Long): QuestionEntity = QuestionEntity(
    id = if (id == 0L) 0 else id,
    questionBankId = bankId,
    title = title,
    type = type.name,
    options = json.encodeToString(options),
    answers = json.encodeToString(answers),
    explanation = explanation,
    tags = json.encodeToString(tags),
)

private fun AnswerRecordEntity.toDomain(): AnswerRecord = AnswerRecord(
    id = id,
    questionId = questionId,
    questionBankId = questionBankId,
    userAnswer = userAnswer,
    isCorrect = isCorrect,
    answeredAt = answeredAt,
    practiceMode = practiceMode,
)

@kotlinx.serialization.Serializable
private data class QuestionImportDto(
    val title: String,
    val type: String = "SINGLE_CHOICE",
    val options: List<String> = emptyList(),
    val answers: List<String>,
    val explanation: String = "",
    val tags: List<String> = emptyList(),
) {
    fun toEntity(bankId: Long): QuestionEntity = QuestionEntity(
        questionBankId = bankId,
        title = title,
        type = type,
        options = Json.encodeToString(options),
        answers = Json.encodeToString(answers),
        explanation = explanation,
        tags = Json.encodeToString(tags),
    )
}
