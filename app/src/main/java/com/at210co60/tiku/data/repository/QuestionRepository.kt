package com.at210co60.tiku.data.repository

import com.at210co60.tiku.data.local.dao.QuestionDao
import com.at210co60.tiku.data.local.entity.QuestionEntity
import com.at210co60.tiku.data.model.Question
import com.at210co60.tiku.data.model.QuestionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class QuestionRepository(private val questionDao: QuestionDao) {

    private val json = Json { ignoreUnknownKeys = true }

    fun getAllQuestions(): Flow<List<Question>> =
        questionDao.getAllQuestions().map { entities -> entities.map { it.toDomain() } }

    fun getRandomQuestions(limit: Int): Flow<List<Question>> =
        questionDao.getRandomQuestions(limit).map { entities -> entities.map { it.toDomain() } }

    suspend fun getQuestionCount(): Int = questionDao.getQuestionCount()

    suspend fun insertQuestions(questions: List<Question>) {
        questionDao.insertQuestions(questions.map { it.toEntity() })
    }

    suspend fun importFromJson(jsonString: String) {
        val questions = json.decodeFromString<List<QuestionImportDto>>(jsonString)
        questionDao.insertQuestions(questions.map { it.toEntity() })
    }

    suspend fun clearAndImport(jsonString: String) {
        questionDao.deleteAllQuestions()
        importFromJson(jsonString)
    }
}

private fun QuestionEntity.toDomain(): Question = Question(
    id = id,
    title = title,
    type = QuestionType.valueOf(type),
    options = json.decodeFromString<List<String>>(options),
    answer = answer,
    explanation = explanation,
    tags = json.decodeFromString<List<String>>(tags.ifEmpty { "[]" }),
)

private fun Question.toEntity(): QuestionEntity = QuestionEntity(
    id = if (id == 0L) 0 else id,
    title = title,
    type = type.name,
    options = json.encodeToString(options),
    answer = answer,
    explanation = explanation,
    tags = json.encodeToString(tags),
)

@kotlinx.serialization.Serializable
private data class QuestionImportDto(
    val title: String,
    val type: String = "SINGLE_CHOICE",
    val options: List<String> = emptyList(),
    val answer: String,
    val explanation: String = "",
    val tags: List<String> = emptyList(),
) {
    fun toEntity(): QuestionEntity = QuestionEntity(
        title = title,
        type = type,
        options = Json.encodeToString(options),
        answer = answer,
        explanation = explanation,
        tags = Json.encodeToString(tags),
    )
}
