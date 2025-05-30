package br.com.baycap.simplifiedfidic.service.simplifiedFidicService

import br.com.baycap.simplifiedfidic.model.FidicInstruction
import br.com.baycap.simplifiedfidic.model.FidicOperation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class DistributeFidicCascadeTest : SmplifiedFidicServiceBaseTest() {
    // Document TestCases
    @Test
    fun `should process cascade with 500000 receive, 10000 default, distribute`() {
        val instructions =
            listOf(
                FidicInstruction(FidicOperation.RECEIVE_CASH, 50000000),
                FidicInstruction(FidicOperation.REGISTER_DEFAULT, 1000000),
                FidicInstruction(FidicOperation.DISTRIBUTE_CASH, null),
            )
        val result = impl.distributeFidicCascade(instructions)
        assertNotNull(result)
        assertEquals(FidicOperation.DISTRIBUTE_CASH, result?.type)
        assertEquals(49000000L, result?.totalDistributedE2)
        assertEquals(1000000L, result?.distribution?.lossAbsorbedE2)
        assertEquals(49000000L, result?.distribution?.seniorPrincipalE2)
        assertEquals(0L, result?.distribution?.seniorYieldE2)
        assertEquals(0L, result?.distribution?.mezzanineYieldE2)
        assertEquals(0L, result?.distribution?.juniorE2)
    }

    @Test
    fun `should process cascade with 500000 receive, distribute`() {
        val instructions =
            listOf(
                FidicInstruction(FidicOperation.RECEIVE_CASH, 50000000),
                FidicInstruction(FidicOperation.DISTRIBUTE_CASH, null),
            )
        val result = impl.distributeFidicCascade(instructions)
        assertNotNull(result)
        assertEquals(FidicOperation.DISTRIBUTE_CASH, result?.type)
        assertEquals(50000000L, result?.totalDistributedE2)
        assertEquals(0L, result?.distribution?.lossAbsorbedE2)
        assertEquals(50000000L, result?.distribution?.seniorPrincipalE2)
        assertEquals(0L, result?.distribution?.seniorYieldE2)
        assertEquals(0L, result?.distribution?.mezzanineYieldE2)
        assertEquals(0L, result?.distribution?.juniorE2)
    }

    @Test
    fun `should process cascade with 1100000 receive, 10000 default, distribute`() {
        val instructions =
            listOf(
                FidicInstruction(FidicOperation.RECEIVE_CASH, 110000000),
                FidicInstruction(FidicOperation.REGISTER_DEFAULT, 1000000),
                FidicInstruction(FidicOperation.DISTRIBUTE_CASH, null),
            )
        val result = impl.distributeFidicCascade(instructions)
        assertNotNull(result)
        assertEquals(FidicOperation.DISTRIBUTE_CASH, result?.type)
        assertEquals(109000000L, result?.totalDistributedE2)
        assertEquals(1000000L, result?.distribution?.lossAbsorbedE2)
        assertEquals(100000000L, result?.distribution?.seniorPrincipalE2)
        assertEquals(9000000L, result?.distribution?.seniorYieldE2)
        assertEquals(0L, result?.distribution?.mezzanineYieldE2)
        assertEquals(0L, result?.distribution?.juniorE2)
    }

    @Test
    fun `should process cascade with 1200000 receive, distribute`() {
        val instructions =
            listOf(
                FidicInstruction(FidicOperation.RECEIVE_CASH, 120000000),
                FidicInstruction(FidicOperation.DISTRIBUTE_CASH, null),
            )
        val result = impl.distributeFidicCascade(instructions)
        assertNotNull(result)
        assertEquals(FidicOperation.DISTRIBUTE_CASH, result?.type)
        assertEquals(120000000L, result?.totalDistributedE2)
        assertEquals(0L, result?.distribution?.lossAbsorbedE2)
        assertEquals(100000000L, result?.distribution?.seniorPrincipalE2)
        assertEquals(12000000L, result?.distribution?.seniorYieldE2)
        assertEquals(7500000L, result?.distribution?.mezzanineYieldE2)
        assertEquals(500000L, result?.distribution?.juniorE2)
    }

    // End Document TestCases
    @Test
    fun `should return null if instruction list is empty`() {
        val result = impl.distributeFidicCascade(emptyList())
        assertNull(result)
    }

    @Test
    fun `should process valid instructions and return expected result`() {
        val instructions =
            listOf(
                FidicInstruction(FidicOperation.RECEIVE_CASH, 200000000),
                FidicInstruction(FidicOperation.REGISTER_DEFAULT, 10000000),
                FidicInstruction(FidicOperation.DISTRIBUTE_CASH, null),
            )
        val result = impl.distributeFidicCascade(instructions)
        assertNotNull(result)
        assertEquals(FidicOperation.DISTRIBUTE_CASH, result?.type)
        assertEquals(190000000L, result?.totalDistributedE2)
        assertEquals(10000000L, result?.distribution?.lossAbsorbedE2)
        assertEquals(100000000L, result?.distribution?.seniorPrincipalE2)
        assertEquals(12000000L, result?.distribution?.seniorYieldE2)
        assertEquals(7500000L, result?.distribution?.mezzanineYieldE2)
        assertEquals(70500000L, result?.distribution?.juniorE2)
    }

    @Test
    fun `should process DISTRIBUTE_CASH when accumulatedLossToAbsorbE2 is greater than fundCashE2`() {
        val instructions =
            listOf(
                FidicInstruction(FidicOperation.RECEIVE_CASH, 5000000),
                FidicInstruction(FidicOperation.REGISTER_DEFAULT, 10000000),
                FidicInstruction(FidicOperation.DISTRIBUTE_CASH, null),
            )
        val result = impl.distributeFidicCascade(instructions)
        assertNotNull(result)
        assertEquals(FidicOperation.DISTRIBUTE_CASH, result?.type)
        assertEquals(0L, result?.totalDistributedE2)
        assertEquals(5000000L, result?.distribution?.lossAbsorbedE2)
        assertEquals(0L, result?.distribution?.seniorPrincipalE2)
        assertEquals(0L, result?.distribution?.seniorYieldE2)
        assertEquals(0L, result?.distribution?.mezzanineYieldE2)
        assertEquals(0L, result?.distribution?.juniorE2)
    }
}
