package br.com.baycap.simplifiedfidic.validate

import br.com.baycap.simplifiedfidic.dto.FidicInstructionDto
import br.com.baycap.simplifiedfidic.model.FidicOperation
import org.springframework.stereotype.Service

@Service
class StaticSimplifiedFidicValidation {
    fun validateDistributeCascade(fidicInstructions: List<FidicInstructionDto>) {
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

    fun FidicInstructionDto.validateFidicInstruction() {
        if (this.type == FidicOperation.UNKNOWN) {
            throw IllegalArgumentException("Invalid Fidic operation type")
        }

        if (this.type in listOf(FidicOperation.REGISTER_DEFAULT, FidicOperation.RECEIVE_CASH)) {
            if (this.amountE2 == null || this.amountE2 <= 0) {
                throw IllegalArgumentException("Amount must be greater than zero")
            }
        }

        if (this.type == FidicOperation.DISTRIBUTE_CASH) {
            if (this.amountE2 != null) {
                throw IllegalArgumentException("Amount should be null for DISTRIBUTE_CASH operation")
            }
        }
    }

    companion object {
        const val MAX_DISTRIBUTION_COUNT = 1
    }
}
