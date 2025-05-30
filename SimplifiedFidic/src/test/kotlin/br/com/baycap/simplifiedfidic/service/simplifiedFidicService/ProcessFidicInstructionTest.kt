package br.com.baycap.simplifiedfidic.service.simplifiedFidicService

import br.com.baycap.simplifiedfidic.model.FidicOperation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class ProcessFidicInstructionTest : SmplifiedFidicServiceBaseTest() {
    @Test
    fun `should throw exception if amountE2 is null for RECEIVE_CASH`() {
        val exception =
            assertThrows(Exception::class.java) {
                impl.processFidicInstruction(FidicOperation.RECEIVE_CASH, null)
            }
        assertEquals("Amount cannot be null for REGISTER_DEFAULT operation", exception.message)
    }

    @Test
    fun `should throw exception if amountE2 is null for REGISTER_DEFAULT`() {
        val exception =
            assertThrows(Exception::class.java) {
                impl.processFidicInstruction(FidicOperation.REGISTER_DEFAULT, null)
            }
        assertEquals("Amount cannot be null for REGISTER_DEFAULT operation", exception.message)
    }

    @Test
    fun `should process RECEIVE_CASH with valid amount`() {
        val result = impl.processFidicInstruction(FidicOperation.RECEIVE_CASH, 100L)
        assertNull(result)
        assertEquals(100L, impl.fundCashE2)
    }

    @Test
    fun `should process REGISTER_DEFAULT with valid amount`() {
        val result = impl.processFidicInstruction(FidicOperation.REGISTER_DEFAULT, 50L)
        assertNull(result)
        assertEquals(50L, impl.accumulatedLossToAbsorbE2)
    }

    @Test
    fun `should process DISTRIBUTE_CASH and return expected result`() {
        impl.fundCashE2 = 200000000
        impl.accumulatedLossToAbsorbE2 = 10000000

        val result = impl.processFidicInstruction(FidicOperation.DISTRIBUTE_CASH, null)
        assertNotNull(result)
        assertEquals(FidicOperation.DISTRIBUTE_CASH, result?.type)
        assertEquals(190000000L, result?.totalDistributedE2)
        assertEquals(10000000L, result?.distribution?.lossAbsorbedE2)
        assertEquals(100000000L, result?.distribution?.seniorPrincipalE2)
        assertEquals(12000000L, result?.distribution?.seniorYieldE2)
        assertEquals(7500000L, result?.distribution?.mezzanineYieldE2)
        assertEquals(70500000L, result?.distribution?.juniorE2)
        assertEquals(0L, impl.fundCashE2)
    }

    @Test
    fun `should process DISTRIBUTE_CASH when accumulatedLossToAbsorbE2 is greater than fundCashE2`() {
        impl.fundCashE2 = 5000000
        impl.accumulatedLossToAbsorbE2 = 10000000

        val result = impl.processFidicInstruction(FidicOperation.DISTRIBUTE_CASH, null)
        assertNotNull(result)
        assertEquals(FidicOperation.DISTRIBUTE_CASH, result?.type)
        assertEquals(0L, result?.totalDistributedE2)
        assertEquals(5000000L, result?.distribution?.lossAbsorbedE2)
        assertEquals(0L, result?.distribution?.seniorPrincipalE2)
        assertEquals(0L, result?.distribution?.seniorYieldE2)
        assertEquals(0L, result?.distribution?.mezzanineYieldE2)
        assertEquals(0L, result?.distribution?.juniorE2)
        assertEquals(0L, impl.fundCashE2)
    }

    @Test
    fun `should throw exception for unsupported operation`() {
        val exception =
            assertThrows(Exception::class.java) {
                impl.processFidicInstruction(FidicOperation.UNKNOWN, 100L)
            }
        assertEquals("Operation type UNKNOWN not supported", exception.message)
    }
}
