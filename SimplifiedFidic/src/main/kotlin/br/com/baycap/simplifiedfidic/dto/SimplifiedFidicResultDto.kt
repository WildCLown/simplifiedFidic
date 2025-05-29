package br.com.baycap.simplifiedfidic.dto

import br.com.baycap.simplifiedfidic.model.FidicOperation
import com.fasterxml.jackson.annotation.JsonProperty

data class SimplifiedFidicResultDto(
    @JsonProperty("type")
    val type: FidicOperation,
    @JsonProperty("total_distributed")
    val totalDistributedE2: Long,
    @JsonProperty("distribution")
    val distribution: FidicDistributionDto,
)
