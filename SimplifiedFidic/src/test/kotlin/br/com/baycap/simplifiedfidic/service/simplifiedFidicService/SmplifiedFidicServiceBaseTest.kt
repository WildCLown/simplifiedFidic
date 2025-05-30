package br.com.baycap.simplifiedfidic.service.simplifiedFidicService

import br.com.baycap.simplifiedfidic.service.impl.SimplifiedFidicServiceImpl
import org.junit.jupiter.api.AfterEach

open class SmplifiedFidicServiceBaseTest {
    // We are not using any DB, so no need for Mocks in this particular case
    // If we used other classes as injection, we would need it
    val impl = SimplifiedFidicServiceImpl()

    @AfterEach
    fun tearDown() {
        // clearMocks(mocks)
    }
}
