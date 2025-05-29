package br.com.baycap.simplifiedfidic.dto

import br.com.baycap.simplifiedfidic.model.FidicOperation
import com.fasterxml.jackson.annotation.JsonProperty

data class FidicInstructionDto(
    @JsonProperty("type")
    val type: FidicOperation,
    @JsonProperty("amount")
    val amountE2: Long?,
)
