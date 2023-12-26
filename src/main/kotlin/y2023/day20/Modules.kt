package y2023.day20

import y2023.day20.Pulse.Companion.pulse

sealed class Module {
    abstract val name: String
    abstract val connect: List<String>

    open fun add(input: String): Module = this
    open fun receive(pulse: Pulse): Pair<Module, List<Pulse>> = noop()

    protected fun noop(): Pair<Module, List<Pulse>> = this to emptyList()
    protected fun output(value: PulseValue) = name.pulse(value, connect)

    companion object {
        fun of(text: String) =
            Regex("(\\S+) -> (.+)").matchEntire(text)?.destructured?.let { (moduleName, destinations) ->
                if (moduleName.startsWith("%"))
                    FlipFlopModule(moduleName.substring(1), split(destinations))
                else if (moduleName.startsWith("&"))
                    ConjunctionModule(moduleName.substring(1), split(destinations))
                else if (moduleName == "broadcaster")
                    BroadcasterModule(split(destinations))
                else
                    throw IllegalArgumentException("module? '$text'")
            } ?: throw IllegalArgumentException("module? '$text'")

        private fun split(destinations: String) = destinations.split(Regex(", "))
    }
}

data class FlipFlopModule(
    override val name: String,
    override val connect: List<String>,
    private val on: Boolean = false
) : Module() {

    override fun receive(pulse: Pulse) = if (pulse.low) {
        flip() to output(if (on) PulseValue.LOW else PulseValue.HIGH)
    } else noop()

    private fun flip() = copy(on = !on)
}

data class ConjunctionModule(
    override val name: String,
    override val connect: List<String>,
    private val status: Map<String, PulseValue> = emptyMap()
) : Module() {
    val inputs = status.keys

    override fun add(input: String) = copy(
        status = status + (input to PulseValue.LOW)
    )

    override fun receive(pulse: Pulse) =
        (status + (pulse.from to pulse.value)).let { status ->
            copy(
                status = status
            ) to output(if (status.values.all { it == PulseValue.HIGH }) PulseValue.LOW else PulseValue.HIGH)
        }
}

data class BroadcasterModule(override val connect: List<String>) : Module() {
    override val name: String = "broadcaster"
    override fun receive(pulse: Pulse) = this to output(pulse.value)
}

data class SinkModule(override val name: String) : Module() {
    override val connect: List<String> = emptyList()
}