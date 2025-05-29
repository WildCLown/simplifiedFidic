package br.com.baycap.simplifiedfidic.model

enum class FidicOperation(val id: Int) {
    UNKNOWN(0),
    RECEIVE_CASH(1),
    REGISTER_DEFAULT(2),
    DISTRIBUTE_CASH(3),
}
