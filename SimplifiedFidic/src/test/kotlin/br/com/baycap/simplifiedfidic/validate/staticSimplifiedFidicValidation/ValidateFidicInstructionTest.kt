package br.com.baycap.simplifiedfidic.validate.staticSimplifiedFidicValidation

import br.com.baycap.simplifiedfidic.dto.FidicInstructionDto
import br.com.baycap.simplifiedfidic.model.FidicOperation
import br.com.baycap.simplifiedfidic.validate.StaticSimplifiedFidicValidation
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class ValidateFidicInstructionTest {
    private val validator = StaticSimplifiedFidicValidation()

    @Test
    fun `should throw exception for UNKNOWN type`() {
        val dto = FidicInstructionDto(FidicOperation.UNKNOWN, 100L)
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                with(validator) { dto.validateFidicInstruction() }
            }
        assertEquals("Invalid Fidic operation type", exception.message)
    }

    @Test
    fun `should throw exception if amountE2 is null for REGISTER_DEFAULT`() {
        val dto = FidicInstructionDto(FidicOperation.REGISTER_DEFAULT, null)
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                with(validator) { dto.validateFidicInstruction() }
            }
        assertEquals("Amount must be greater than zero", exception.message)
    }

    @Test
    fun `should throw exception if amountE2 is less than or equal to zero for RECEIVE_CASH`() {
        val dto = FidicInstructionDto(FidicOperation.RECEIVE_CASH, 0L)
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                with(validator) { dto.validateFidicInstruction() }
            }
        assertEquals("Amount must be greater than zero", exception.message)
    }

    @Test
    fun `should throw exception if amountE2 is not null for DISTRIBUTE_CASH`() {
        val dto = FidicInstructionDto(FidicOperation.DISTRIBUTE_CASH, 10L)
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                with(validator) { dto.validateFidicInstruction() }
            }
        assertEquals("Amount should be null for DISTRIBUTE_CASH operation", exception.message)
    }

    @Test
    fun `happy path for REGISTER_DEFAULT`() {
        val dto = FidicInstructionDto(FidicOperation.REGISTER_DEFAULT, 100L)
        assertDoesNotThrow {
            with(validator) { dto.validateFidicInstruction() }
        }
    }

    @Test
    fun `happy path for RECEIVE_CASH`() {
        val dto = FidicInstructionDto(FidicOperation.RECEIVE_CASH, 50L)
        assertDoesNotThrow {
            with(validator) { dto.validateFidicInstruction() }
        }
    }

    @Test
    fun `happy path for DISTRIBUTE_CASH`() {
        val dto = FidicInstructionDto(FidicOperation.DISTRIBUTE_CASH, null)
        assertDoesNotThrow {
            with(validator) { dto.validateFidicInstruction() }
        }
    }
}
