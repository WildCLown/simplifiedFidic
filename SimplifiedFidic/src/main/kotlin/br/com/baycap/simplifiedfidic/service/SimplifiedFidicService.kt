package br.com.baycap.simplifiedfidic.service

import br.com.baycap.simplifiedfidic.dto.SimplifiedFidicResultDto
import br.com.baycap.simplifiedfidic.model.FidicInstruction
import br.com.baycap.simplifiedfidic.model.FidicOperation

interface SimplifiedFidicService {
    fun distributeFidicCascade(fidicInstructions: List<FidicInstruction>): SimplifiedFidicResultDto?

    fun processFidicInstruction(
        type: FidicOperation,
        amountE2: Long?,
    ): SimplifiedFidicResultDto?
}
