package br.com.baycap.simplifiedfidic.validate

import br.com.baycap.simplifiedfidic.dto.FidicInstructionDto
import br.com.baycap.simplifiedfidic.model.FidicOperation

class StaticSimplifiedFidicValidation {
    fun validateDistributeCascate(fidicInstructions: List<FidicInstructionDto>) {
        if (fidicInstructions.isEmpty()) {
            throw IllegalArgumentException("Fidic instructions cannot be empty")
        }
        var distributionCount = 0
        fidicInstructions.forEach { instruction ->
            if (instruction.type == FidicOperation.DISTRIBUTE_CASH) {
                distributionCount++
            }

            instruction.validateFidicInstruction()
        }

        require(distributionCount <= MAX_DISTRIBUTION_COUNT) {
            "Only one DISTRIBUTE_CASH operation is allowed"
        }
    }

    companion object {
        const val MAX_DISTRIBUTION_COUNT = 1
    }
}
