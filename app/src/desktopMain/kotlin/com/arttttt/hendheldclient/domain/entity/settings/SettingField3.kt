package com.arttttt.hendheldclient.domain.entity.settings

sealed interface SettingField3 {

    data class RootField(
        val key: FieldKey,
        val items: Map<String, SettingField3>
    ) : SettingField3

    sealed interface Fields<T> : SettingField3 {
        val key: FieldKey
        val value: T
        val hint: String
        val tags: List<String>
        val title: String

        data class SectionField(
            override val key: FieldKey,
            override val title: String,
            override val tags: List<String>,
            override val hint: String,
            override val value: Map<String, SettingField3>
        ) : Fields<Map<String, SettingField3>>

        data class DisplayField(
            override val key: FieldKey,
            override val value: String?,
            override val hint: String,
            override val tags: List<String>,
            override val title: String,
        ) : Fields<String?>


        data class ActionField(
            override val key: FieldKey,
            override val value: Boolean?,
            override val hint: String,
            override val tags: List<String>,
            override val title: String,
        ) : Fields<Boolean?>

        data class BooleanField(
            override val key: FieldKey,
            override val value: Boolean,
            override val hint: String,
            override val tags: List<String>,
            override val title: String,
        ) : Fields<Boolean>

        data class IntInputField(
            override val key: FieldKey,
            override val value: String,
            override val hint: String,
            override val tags: List<String>,
            override val title: String,
            val min: Int,
            val max: Int,
            val step: Int?,
        ) : Fields<String>

        data class DiscreteField(
            override val key: FieldKey,
            override val value: Int,
            override val hint: String,
            override val tags: List<String>,
            override val title: String,
            val values: Set<Int>,
        ) : Fields<Int>

        data class MultipleField(
            override val key: FieldKey,
            override val value: String,
            override val hint: String,
            override val tags: List<String>,
            override val title: String,
            val values: Map<String, String>,
        ) : Fields<String>

        data class Mode(
            override val key: FieldKey,
            override val value: String,
            override val hint: String,
            override val tags: List<String>,
            override val title: String,
            val modes: Map<String, SettingField3>,
        ) : Fields<String> {

            val mode: SettingField3
                get() {
                    return modes.getValue(value)
                }
        }
    }
}