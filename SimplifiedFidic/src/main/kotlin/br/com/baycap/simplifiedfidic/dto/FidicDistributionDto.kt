package br.com.baycap.simplifiedfidic.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class FidicDistributionDto(
    @JsonProperty("loss_absorbed")
    val lossAbsorbedE2: Long,
    @JsonProperty("senior_principal")
    val seniorPrincipalE2: Long,
    @JsonProperty("senior_yield")
    val seniorYieldE2: Long,
    @JsonProperty("mezzanine_principal")
    val mezzanineYieldE2: Long,
    @JsonProperty("junior")
    val juniorE2: Long,
)
