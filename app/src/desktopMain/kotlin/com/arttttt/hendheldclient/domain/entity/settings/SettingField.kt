package com.arttttt.hendheldclient.domain.entity.settings

sealed interface SettingField<T> {
    val key: FieldKey
    val id: String
    val value: T
    val hint: String
    val tags: List<String>
    val title: String

    data class DisplayField(
        override val key: FieldKey,
        override val id: String,
        override val value: String?,
        override val hint: String,
        override val tags: List<String>,
        override val title: String,
    ) : SettingField<String?>


    data class ActionField(
        override val key: FieldKey,
        override val value: Boolean?,
        override val id: String,
        override val hint: String,
        override val tags: List<String>,
        override val title: String,
    ) : SettingField<Boolean?>

    data class BooleanField(
        override val key: FieldKey,
        override val id: String,
        override val value: Boolean,
        override val hint: String,
        override val tags: List<String>,
        override val title: String,
    ) : SettingField<Boolean>

    data class IntInputField(
        override val key: FieldKey,
        override val id: String,
        override val value: String,
        override val hint: String,
        override val tags: List<String>,
        override val title: String,
        val min: Int,
        val max: Int,
        val step: Int?,
    ) : SettingField<String>
}