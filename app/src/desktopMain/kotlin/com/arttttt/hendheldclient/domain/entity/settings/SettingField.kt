package com.arttttt.hendheldclient.domain.entity.settings

sealed interface SettingField<T> {
    val id: String
    val value: T
    val hint: String
    val tags: List<String>
    val title: String

    data class DisplayField(
        override val id: String,
        override val value: String?,
        override val hint: String,
        override val tags: List<String>,
        override val title: String,
    ) : SettingField<String?>


    data class ActionField(
        override val id: String,
        override val hint: String,
        override val tags: List<String>,
        override val title: String,
    ) : SettingField<Unit> {
        override val value: Unit = Unit
    }

    data class BooleanField(
        override val id: String,
        override val value: Boolean,
        override val hint: String,
        override val tags: List<String>,
        override val title: String,
    ) : SettingField<Boolean>

    data class IntField(
        override val id: String,
        override val value: Int,
        override val hint: String,
        override val tags: List<String>,
        override val title: String,
        val min: Int,
        val max: Int,
        val step: Int?,
    ) : SettingField<Int>
}