package br.com.baycap.simplifiedfidic.controller

import br.com.baycap.simplifiedfidic.dto.FidicInstructionDto
import br.com.baycap.simplifiedfidic.dto.SimplifiedFidicResultDto
import br.com.baycap.simplifiedfidic.model.toModel
import br.com.baycap.simplifiedfidic.service.SimplifiedFidicService
import br.com.baycap.simplifiedfidic.validate.StaticSimplifiedFidicValidation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/simplifiedFidic")
class SimplifiedFidicController(
    val simplifiedFidicService: SimplifiedFidicService,
    val staticSimplifiedFidicValidation: StaticSimplifiedFidicValidation,
) {
    @PostMapping("/distributeFidicCascade")
    fun distributeFidicCascade(
        @RequestBody request: List<FidicInstructionDto>,
    ): SimplifiedFidicResultDto? {
        staticSimplifiedFidicValidation.validateDistributeCascade(request)

        val fidicInstructions = request.map { it.toModel() }
        return simplifiedFidicService.distributeFidicCascade(fidicInstructions)
    }
}
