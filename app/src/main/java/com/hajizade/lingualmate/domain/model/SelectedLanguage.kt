package com.hajizade.lingualmate.domain.model


data class SelectedLanguage(
    val languageName: String,
    val level: ProficiencyLevel = ProficiencyLevel.BEGINNER
)