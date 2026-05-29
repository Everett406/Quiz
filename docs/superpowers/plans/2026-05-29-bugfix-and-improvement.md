# Bugfix & Improvement Implementation Plan

> **For agentic workers:** Execute tasks in order. Each task is self-contained. Build and test after every major change group.

**Goal:** Fix all identified bugs and UX issues in the Quiz app, bump version to v1.2.0, build APK, and verify.

**Architecture:** Keep existing MVVM + Repository pattern. Fix data model (multi-choice answer as List), improve UI interactions, enrich wrong-question book with full question content.

**Tech Stack:** Kotlin, Jetpack Compose, Room, DataStore, Gradle

---

## File Change Map

| File | Action | Reason |
|------|--------|--------|
| `data/model/Question.kt` | Modify | Change `answer: String` to `answers: List<String>` for multi-choice support |
| `data/local/entity/QuestionEntity.kt` | Modify | Change `answer: String` to `answers: String` (JSON array) |
| `data/repository/QuestionRepository.kt` | Modify | Update mapping, JSON import/export for answers list |
| `data/local/dao/AnswerRecordDao.kt` | Modify | Add `getWrongRecordsWithQuestions()` join query |
| `data/model/Models.kt` | Modify | Add `WrongRecordWithQuestion` data class |
| `viewmodel/QuizViewModel.kt` | Modify | Support multi-choice selection, fix correctness check |
| `ui/screen/quiz/QuizPracticeScreen.kt` | Modify | Multi-choice UI, manual next button, short answer reference answer |
| `ui/screen/wrong/WrongQuestionsScreen.kt` | Modify | Show full question content, fix delete, add bank filter |
| `ui/screen/home/HomeScreen.kt` | Modify | Fix button labels, remove hardcoded JSON |
| `ui/screen/settings/SettingsScreen.kt` | Modify | Add About section, Clear Data option |
| `ui/screen/detail/QuizDetailScreen.kt` | Modify | Fix stats calculation |
| `navigation/Screen.kt` | Modify | Add bankId param to WrongQuestions route |
| `TikuApp.kt` | Modify | Pass bankId to WrongQuestionsScreen |
| `app/build.gradle.kts` | Modify | Bump version to 1.2.0 |
| `README.md` | Modify | Add v1.2.0 changelog |
| `data/local/TikuDatabase.kt` | Modify | Bump database version, add migration |

---

## Task 1: Fix Multi-Choice Data Model

**Files:**
- Modify: `data/model/Question.kt`
- Modify: `data/local/entity/QuestionEntity.kt`
- Modify: `data/repository/QuestionRepository.kt`

- [ ] **Step 1: Update Question domain model**

```kotlin
// data/model/Question.kt
data class Question(
    val id: Long = 0,
    val title: String,
    val type: QuestionType,
    val options: List<String> = emptyList(),
    val answers: List<String>,        // Changed from single answer: String
    val explanation: String = "",
    val tags: List<String> = emptyList(),
)
```

- [ ] **Step 2: Update QuestionEntity**

```kotlin
// data/local/entity/QuestionEntity.kt
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val questionBankId: Long,
    val title: String,
    val type: String,
    val options: String,     // JSON array
    val answers: String,     // JSON array (changed from answer: String)
    val explanation: String = "",
    val tags: String = "",   // JSON array
)
```

- [ ] **Step 3: Update Repository mapping functions**

In `QuestionRepository.kt`, update:
- `QuestionEntity.toDomain()`: `answers = json.decodeFromString<List<String>>(answers)`
- `Question.toEntity()`: `answers = json.encodeToString(answers)`
- `QuestionImportDto`: change `answer: String` to `answers: List<String>`, update `toEntity()`

- [ ] **Step 4: Update sample JSON in HomeScreen** (will be done in Task 9)

---

## Task 2: Fix Multi-Choice UI

**Files:**
- Modify: `ui/screen/quiz/QuizPracticeScreen.kt`
- Modify: `viewmodel/QuizViewModel.kt`

- [ ] **Step 1: Change selectedAnswer to List<String> in ViewModel**

```kotlin
// QuizViewModel.kt
private val _selectedAnswers = MutableStateFlow<Set<String>>(emptySet())
val selectedAnswers: StateFlow<Set<String>> = _selectedAnswers.asStateFlow()
```

Update `selectAnswer()` to toggle for multi-choice:
```kotlin
fun selectAnswer(answer: String) {
    if (_isAnswered.value) return
    val currentQuestion = currentQuestion.value ?: return
    if (currentQuestion.type == QuestionType.MULTI_CHOICE) {
        _selectedAnswers.value = _selectedAnswers.value.toMutableSet().apply {
            if (contains(answer)) remove(answer) else add(answer)
        }
    } else {
        _selectedAnswers.value = setOf(answer)
        _isAnswered.value = true
        _totalAnswered.value++
        if (isCorrect()) _correctCount.value++
    }
}

fun confirmMultiChoiceAnswer() {
    if (_isAnswered.value) return
    _isAnswered.value = true
    _totalAnswered.value++
    if (isCorrect()) _correctCount.value++
}
```

Update `isCorrect()`:
```kotlin
fun isCorrect(): Boolean {
    val question = currentQuestion.value ?: return false
    val selected = _selectedAnswers.value
    if (selected.isEmpty()) return false
    return when (question.type) {
        QuestionType.MULTI_CHOICE -> {
            selected.size == question.answers.size && selected.containsAll(question.answers)
        }
        QuestionType.TRUE_FALSE -> {
            selected.firstOrNull()?.equals(question.answers.firstOrNull(), ignoreCase = true) ?: false
        }
        QuestionType.SHORT_ANSWER -> {
            selected.firstOrNull()?.equals(question.answers.firstOrNull(), ignoreCase = true) ?: false
        }
        else -> selected.firstOrNull() == question.answers.firstOrNull()
    }
}
```

Update `nextQuestion()` / `previousQuestion()` / `resetQuiz()` to clear `_selectedAnswers`.

- [ ] **Step 2: Update QuizPracticeScreen for multi-choice UI**

For `MULTI_CHOICE` type:
- Each option shows checkbox-like selection (can select multiple)
- Show "确认答案" button at bottom
- Only after confirming, show feedback and enable next

For `SINGLE_CHOICE` / `TRUE_FALSE`:
- Keep current behavior (select = answer immediately)

Update `OptionCard` to accept `isSelected: Boolean` from a Set.

---

## Task 3: Fix Short Answer Reference Display

**Files:**
- Modify: `ui/screen/quiz/QuizPracticeScreen.kt`

- [ ] **Step 1: Show correct answer for short answer after submission**

In the feedback card for short answer, always show:
```
参考答案：xxx
```

---

## Task 4: Fix Auto-Advance to Manual Confirmation

**Files:**
- Modify: `ui/screen/quiz/QuizPracticeScreen.kt`

- [ ] **Step 1: Remove auto-advance LaunchedEffect**

Remove:
```kotlin
LaunchedEffect(isAnswered) {
    if (isAnswered && !isLastQuestion && !quizCompleted) {
        kotlinx.coroutines.delay(1500)
        viewModel.nextQuestion()
    }
}
```

- [ ] **Step 2: Add explicit "下一题" button that is always visible after answering**

After answer feedback, show:
- "下一题" button (or "查看结果" for last question)
- Remove "即将自动进入下一题..." text

---

## Task 5: Fix Wrong Questions Screen - Show Full Content

**Files:**
- Modify: `data/local/dao/AnswerRecordDao.kt`
- Modify: `data/model/Models.kt`
- Modify: `data/repository/QuestionRepository.kt`
- Modify: `ui/screen/wrong/WrongQuestionsScreen.kt`

- [ ] **Step 1: Add join query in DAO**

```kotlin
// AnswerRecordDao.kt
@Query("""
    SELECT ar.*, q.title as questionTitle, q.type as questionType, q.options as questionOptions, 
           q.answers as questionAnswers, q.explanation as questionExplanation
    FROM answer_records ar
    INNER JOIN questions q ON ar.questionId = q.id
    WHERE ar.isCorrect = 0
    ORDER BY ar.answeredAt DESC
""")
fun getWrongRecordsWithQuestions(): Flow<List<WrongRecordWithQuestionEntity>>
```

- [ ] **Step 2: Add data class**

```kotlin
// data/model/Models.kt
data class WrongRecordWithQuestion(
    val record: AnswerRecord,
    val questionTitle: String,
    val questionType: QuestionType,
    val questionOptions: List<String>,
    val questionAnswers: List<String>,
    val questionExplanation: String,
)
```

- [ ] **Step 3: Add Repository method**

```kotlin
// QuestionRepository.kt
fun getWrongRecordsWithQuestions(): Flow<List<WrongRecordWithQuestion>> =
    answerRecordDao.getWrongRecordsWithQuestions().map { entities ->
        entities.map { entity ->
            WrongRecordWithQuestion(
                record = entity.toDomain(),
                questionTitle = entity.questionTitle,
                questionType = QuestionType.valueOf(entity.questionType),
                questionOptions = json.decodeFromString(entity.questionOptions),
                questionAnswers = json.decodeFromString(entity.questionAnswers),
                questionExplanation = entity.questionExplanation,
            )
        }
    }
```

- [ ] **Step 4: Redesign WrongQuestionsScreen**

Show:
- Question title (not "第 X 题")
- Question type badge
- User's wrong answer (red)
- Correct answer (green)
- Explanation (if any)
- Delete button (working)
- Expandable card

---

## Task 6: Fix Answer Record Deduplication

**Files:**
- Modify: `data/local/dao/AnswerRecordDao.kt`
- Modify: `data/repository/QuestionRepository.kt`

- [ ] **Step 1: Add upsert in DAO**

```kotlin
@Query("DELETE FROM answer_records WHERE questionId = :questionId")
suspend fun deleteByQuestionId(questionId: Long)

@Transaction
suspend fun upsert(record: AnswerRecordEntity) {
    deleteByQuestionId(record.questionId)
    insert(record)
}
```

- [ ] **Step 2: Update Repository to use upsert**

```kotlin
suspend fun recordAnswer(...) {
    answerRecordDao.upsert(AnswerRecordEntity(...))
}
```

---

## Task 7: Fix Wrong Questions Delete

**Files:**
- Modify: `ui/screen/wrong/WrongQuestionsScreen.kt`
- Modify: `data/repository/QuestionRepository.kt`
- Modify: `data/local/dao/AnswerRecordDao.kt`

- [ ] **Step 1: Add delete method**

Already have `deleteByQuestionId` in DAO. Add to Repository:
```kotlin
suspend fun deleteWrongRecord(questionId: Long) {
    answerRecordDao.deleteByQuestionId(questionId)
}
```

- [ ] **Step 2: Wire up in UI**

Pass `onDelete = { scope.launch { repository.deleteWrongRecord(record.questionId) } }`

---

## Task 8: Fix Home Screen Button Labels

**Files:**
- Modify: `ui/screen/home/HomeScreen.kt`

- [ ] **Step 1: Change "导入题库" to "加载示例"**
- [ ] **Step 2: Remove or disable "题库模板" button (no function yet)**
- [ ] **Step 3: Move sample JSON to a constant or string resource (optional, can keep inline for now)**

---

## Task 9: Add About & Clear Data to Settings

**Files:**
- Modify: `ui/screen/settings/SettingsScreen.kt`
- Modify: `data/repository/QuestionRepository.kt`
- Modify: `data/local/dao/*Dao.kt`

- [ ] **Step 1: Add clear all data function**

```kotlin
// QuestionRepository.kt
suspend fun clearAllData() {
    answerRecordDao.deleteAll()
    questionDao.deleteAllQuestions()
    questionBankDao.deleteAll()
}
```

- [ ] **Step 2: Add About section in SettingsScreen**

Show:
- App name: Tiku
- Version: v1.2.0
- Description

- [ ] **Step 3: Add Clear Data button with confirmation dialog**

---

## Task 10: Fix Quiz Detail Stats

**Files:**
- Modify: `ui/screen/detail/QuizDetailScreen.kt`
- Modify: `data/model/Models.kt`

- [ ] **Step 1: Fix correctRate calculation**

`correctRate` should be based on distinct questions answered, not total answer records.

---

## Task 11: Add Bank Filter to Wrong Questions

**Files:**
- Modify: `navigation/Screen.kt`
- Modify: `TikuApp.kt`
- Modify: `ui/screen/wrong/WrongQuestionsScreen.kt`

- [ ] **Step 1: Add optional bankId param to route**

```kotlin
data object WrongQuestions : Screen("wrong_questions?bankId={bankId}") {
    fun createRoute(bankId: Long = -1) = if (bankId >= 0) "wrong_questions?bankId=$bankId" else "wrong_questions"
}
```

- [ ] **Step 2: Update screen to accept bankId and filter**

---

## Task 12: Bump Version & Update README

**Files:**
- Modify: `app/build.gradle.kts`
- Modify: `README.md`

- [ ] **Step 1: Update versionCode and versionName to 1.2.0**
- [ ] **Step 2: Add v1.2.0 to README changelog**

---

## Task 13: Database Migration

**Files:**
- Modify: `data/local/TikuDatabase.kt`

- [ ] **Step 1: Bump version to 3**
- [ ] **Step 2: Add migration from 2 to 3**

```kotlin
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Rename answer to answers (now JSON array)
        db.execSQL("ALTER TABLE questions ADD COLUMN answers TEXT NOT NULL DEFAULT '[]'")
        db.execSQL("UPDATE questions SET answers = json_array(answer) WHERE answers = '[]'")
        // Note: dropping old column requires table recreation in SQLite
    }
}
```

Actually simpler: since this is pre-release and data is sample, we can use `fallbackToDestructiveMigration()` or recreate.

---

## Task 14: Build APK

- [ ] **Step 1: Run gradle build**

```bash
./gradlew assembleRelease
```

- [ ] **Step 2: Check for errors, fix if any**

---

## Task 15: Push to GitHub

- [ ] **Step 1: Commit all changes**
- [ ] **Step 2: Push to main**
- [ ] **Step 3: Wait for GitHub Actions build**
- [ ] **Step 4: Check build status via MCP**
