package br.com.baycap.simplifiedfidic.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class FidicOperation(val id: Int) {
    UNKNOWN(0),
    RECEIVE_CASH(1),
    REGISTER_DEFAULT(2),
    DISTRIBUTE_CASH(3),
    ;

    @JsonValue
    fun toJson(): String = name.uppercase()

    companion object {
        @JsonCreator
        @JvmStatic
        fun fromJson(value: String): FidicOperation {
            return FidicOperation.entries.find { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Invalid FidicOperation value: $value")
        }
    }
}
