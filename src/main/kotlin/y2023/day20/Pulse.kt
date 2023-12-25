package y2023.day20

enum class PulseValue {
    HIGH, LOW;

    override fun toString() = name.lowercase()
}

data class Pulse(val from: String, val to: String, val value: PulseValue) {
    val low = value == PulseValue.LOW
    val high = value == PulseValue.HIGH

    companion object {
        fun high(from: String, to: String) = Pulse(from, to, PulseValue.HIGH)
        fun low(from: String, to: String) = Pulse(from, to, PulseValue.LOW)

        fun String.pulse(value: PulseValue, to: List<String>) =
            to.map { Pulse(this, it, value) }
    }


    override fun toString(): String {
        return "$from -$value-> $to"
    }
}
