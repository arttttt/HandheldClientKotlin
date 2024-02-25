package com.arttttt.hendheldclient.domain.entity.settings

sealed interface SettingField2<T> {
    val key: FieldKey
    val id: String
    val value: T
    val hint: String
    val tags: List<String>
    val title: String

    data class SectionField(
        override val key: FieldKey,
        override val id: String,
        override val title: String,
        override val tags: List<String>,
        override val hint: String,
        override val value: Map<String, SettingField2<*>>
    ) : SettingField2<Map<String, SettingField2<*>>>

    data class DisplayField(
        override val key: FieldKey,
        override val id: String,
        override val value: String?,
        override val hint: String,
        override val tags: List<String>,
        override val title: String,
    ) : SettingField2<String?>


    data class ActionField(
        override val key: FieldKey,
        override val value: Boolean?,
        override val id: String,
        override val hint: String,
        override val tags: List<String>,
        override val title: String,
    ) : SettingField2<Boolean?>

    data class BooleanField(
        override val key: FieldKey,
        override val id: String,
        override val value: Boolean,
        override val hint: String,
        override val tags: List<String>,
        override val title: String,
    ) : SettingField2<Boolean>

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
    ) : SettingField2<String>
}