package br.com.baycap.simplifiedfidic.validate.staticSimplifiedFidicValidation

import br.com.baycap.simplifiedfidic.dto.FidicInstructionDto
import br.com.baycap.simplifiedfidic.model.FidicOperation
import br.com.baycap.simplifiedfidic.validate.StaticSimplifiedFidicValidation
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class ValidateDistributeCascadeTest {
    private val validator = StaticSimplifiedFidicValidation()

    @Test
    fun `should throw exception if instructions list is empty`() {
        val instructions = emptyList<FidicInstructionDto>()
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                validator.validateDistributeCascade(instructions)
            }
        assertEquals("Fidic instructions cannot be empty", exception.message)
    }

    @Test
    fun `should throw exception if more than one DISTRIBUTE_CASH operation`() {
        val instructions =
            listOf(
                FidicInstructionDto(FidicOperation.RECEIVE_CASH, 100L),
                FidicInstructionDto(FidicOperation.DISTRIBUTE_CASH, null),
                FidicInstructionDto(FidicOperation.DISTRIBUTE_CASH, null),
            )
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                validator.validateDistributeCascade(instructions)
            }
        assertEquals("Only one DISTRIBUTE_CASH operation is allowed", exception.message)
    }

    @Test
    fun `should throw exception if any instruction is invalid`() {
        val instructions =
            listOf(
                FidicInstructionDto(FidicOperation.RECEIVE_CASH, 0L),
                FidicInstructionDto(FidicOperation.DISTRIBUTE_CASH, null),
            )
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                validator.validateDistributeCascade(instructions)
            }
        assertEquals("Amount must be greater than zero", exception.message)
    }

    @Test
    fun `should not throw exception for valid instructions with one DISTRIBUTE_CASH`() {
        val instructions =
            listOf(
                FidicInstructionDto(FidicOperation.RECEIVE_CASH, 100L),
                FidicInstructionDto(FidicOperation.REGISTER_DEFAULT, 50L),
                FidicInstructionDto(FidicOperation.DISTRIBUTE_CASH, null),
            )
        assertDoesNotThrow {
            validator.validateDistributeCascade(instructions)
        }
    }

    @Test
    fun `should not throw exception for valid instructions without DISTRIBUTE_CASH`() {
        val instructions =
            listOf(
                FidicInstructionDto(FidicOperation.RECEIVE_CASH, 100L),
                FidicInstructionDto(FidicOperation.REGISTER_DEFAULT, 50L),
            )
        assertDoesNotThrow {
            validator.validateDistributeCascade(instructions)
        }
    }
}
