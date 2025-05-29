package br.com.baycap.simplifiedfidic.model

import br.com.baycap.simplifiedfidic.dto.FidicInstructionDto

data class FidicInstruction(
    val type: FidicOperation,
    val amountE2: Long?,
)

fun FidicInstructionDto.toModel(): FidicInstruction {
    return FidicInstruction(
        type = type,
        amountE2 = amountE2,
    )
}
