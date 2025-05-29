package br.com.baycap.simplifiedfidic.service.impl

import br.com.baycap.simplifiedfidic.dto.FidicDistributionDto
import br.com.baycap.simplifiedfidic.dto.SimplifiedFidicResultDto
import br.com.baycap.simplifiedfidic.model.FidicInstruction
import br.com.baycap.simplifiedfidic.model.FidicOperation
import br.com.baycap.simplifiedfidic.service.SimplifiedFidicService
import org.springframework.stereotype.Service

@Service
class SimplifiedFidicServiceImpl() : SimplifiedFidicService {
    // TODO: If this is as simple as the exercise shows, we can internalize on
    // processFidicInstruction, otherwise, store at Database for certain FIDIC ID
    var fundCashE2: Long = 0
    var accumulatedLossToAbsorbE2: Long = 0

    override fun distributeFidicCascade(fidicInstructions: List<FidicInstruction>): SimplifiedFidicResultDto? {
        fundCashE2 = 0
        accumulatedLossToAbsorbE2 = 0

        var simplifiedFidic: SimplifiedFidicResultDto? = null
        fidicInstructions.forEach { instruction ->
            val result =
                processFidicInstruction(
                    type = instruction.type,
                    amountE2 = instruction.amountE2,
                )

            result?.let {
                simplifiedFidic = it
            }
        }

        return simplifiedFidic
    }

    override fun processFidicInstruction(
        type: FidicOperation,
        amountE2: Long?,
    ): SimplifiedFidicResultDto? {
        return when (type) {
            FidicOperation.RECEIVE_CASH -> {
                if (amountE2 == null) {
                    throw Exception("Amount cannot be null for REGISTER_DEFAULT operation")
                }

                fundCashE2 =
                    registerReceiveCash(
                        fundCashE2 = fundCashE2,
                        amountE2 = amountE2,
                    )
                null
            }
            FidicOperation.REGISTER_DEFAULT -> {
                if (amountE2 == null) {
                    throw Exception("Amount cannot be null for REGISTER_DEFAULT operation")
                }

                accumulatedLossToAbsorbE2 =
                    registerLossToAbsorb(
                        accumulatedLossToAbsorbE2 = accumulatedLossToAbsorbE2,
                        amountE2 = amountE2,
                    )
                null
            }
            FidicOperation.DISTRIBUTE_CASH -> {
                val distributedFundsResponse =
                    distributeFunds(
                        fundCashE2 = fundCashE2,
                        accumulatedLossToAbsorbE2 = accumulatedLossToAbsorbE2,
                    )
                fundCashE2 = 0
                accumulatedLossToAbsorbE2 -= distributedFundsResponse.distribution.lossAbsorbedE2
                distributedFundsResponse
            }
            else -> throw Exception("Operation type $type not supported")
        }
    }

    private fun distributeFunds(
        fundCashE2: Long,
        accumulatedLossToAbsorbE2: Long,
    ): SimplifiedFidicResultDto {
        val results =
            getMapDistribution(
                fundCashE2 = fundCashE2,
                accumulatedLossToAbsorbE2 = accumulatedLossToAbsorbE2,
            )

        return SimplifiedFidicResultDto(
            type = FidicOperation.DISTRIBUTE_CASH,
            totalDistributedE2 = fundCashE2,
            distribution =
                FidicDistributionDto(
                    lossAbsorbedE2 = results["lossAbsorbed"] ?: 0L,
                    seniorPrincipalE2 = results["seniorPrincipalE2"] ?: 0L,
                    seniorYieldE2 = results["seniorYieldE2"] ?: 0L,
                    mezzanineYieldE2 = results["mezzanineYieldE2"] ?: 0L,
                    juniorE2 = results["juniorE2"] ?: 0L,
                ),
        )
    }

    private fun getMapDistribution(
        fundCashE2: Long,
        accumulatedLossToAbsorbE2: Long,
    ): Map<String, Long> {
        var mutableFundCashE2 = fundCashE2
        val distributionOrder =
            listOf(
                Pair("lossAbsorbed", Pair(0L, accumulatedLossToAbsorbE2)),
                Pair("seniorPrincipalE2", Pair(0L, PRINCIPAL_SENIOR_PAYMENT_AMOUNTE2)),
                Pair("seniorYieldE2", Pair(0L, TARGET_SENIOR_YIELD)),
                Pair("mezzanineYieldE2", Pair(0L, TARGET_MEZANINO_YIELD)),
                Pair("juniorE2", Pair(0L, Long.MAX_VALUE)),
            )
        val results = mutableMapOf<String, Long>()

        distributionOrder.forEach {
            val (name, minMaxVal) = it
            results[name] = mutableFundCashE2.coerceAtMost(minMaxVal.second)
            mutableFundCashE2 -= minMaxVal.second
            if (mutableFundCashE2 <= 0) {
                return results
            }
        }

        return results
    }

    private fun registerReceiveCash(
        fundCashE2: Long,
        amountE2: Long,
    ): Long {
        return fundCashE2 + amountE2
    }

    private fun registerLossToAbsorb(
        accumulatedLossToAbsorbE2: Long,
        amountE2: Long,
    ): Long {
        return accumulatedLossToAbsorbE2 + amountE2
    }

    companion object { // TODO: Retrieve through some configuration route
        const val PRINCIPAL_SENIOR_PAYMENT_AMOUNTE2: Long = 100000000
        const val TARGET_SENIOR_YIELD: Long = 12000000
        const val TARGET_MEZANINO_YIELD: Long = 7500000
    }
}
