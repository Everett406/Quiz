package com.at210co60.tiku.data.local.dao

import androidx.room.Embedded
import com.at210co60.tiku.data.local.entity.AnswerRecordEntity

class WrongRecordWithQuestionEntity {
    @Embedded
    lateinit var record: AnswerRecordEntity

    var questionTitle: String = ""
    var questionType: String = ""
    var questionOptions: String = ""
    var questionAnswers: String = ""
    var questionExplanation: String = ""
}
