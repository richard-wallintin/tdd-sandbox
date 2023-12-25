package y2023.day20

import java.util.*

typealias NetworkState = Map<String, Module>

class Network(moduleList: NetworkState) {
    private val modules = moduleList.toMutableMap()

    init {
        moduleList.values.forEach { m ->
            m.connect.forEach {
                modules[it] = modules.getOrPut(it) { SinkModule(it) }.add(m.name)
            }
        }
    }

    private fun module(name: String) =
        modules[name] ?: throw IllegalStateException("module $name unknown")

    fun pushButton(start: Pulse = Pulse("button", "broadcaster", PulseValue.LOW)) = sequence {
        val q: Queue<Pulse> = LinkedList()

        enqueue(q, start)

        while (q.isNotEmpty()) {
            val pulse = q.remove()

            val (changedState, output) = module(pulse.to).receive(pulse)
            modules[changedState.name] = changedState

            output.forEach {
                enqueue(q, it)
            }
        }
    }

    private val currentState: NetworkState get() = modules.toMap()

    private suspend fun SequenceScope<Pulse>.enqueue(
        q: Queue<Pulse>,
        start: Pulse
    ) {
        q.offer(start.also { yield(it) })
    }

    private fun cycle(maxRounds: Long): Pair<Long, Long> {
        val seen = mutableSetOf(currentState)
        var (high, low) = 0 to 0
        (1..maxRounds).forEach { round ->
            pushButton().forEach {
                if (it.high) high++ else low++
            }
            if (!seen.add(currentState))
                return round to (high.toLong() * low.toLong())
        }
        System.err.println("no cycle detected after 1000 button pushes")
        return maxRounds to (high.toLong() * low.toLong())
    }

    fun cyclePulseValue(totalRounds: Long = 1_000): Long {
        val (cycle, pulseValue) = cycle(totalRounds)
        return pulseValue * (totalRounds / cycle).let { it * it }
    }

    companion object {
        fun setup(sampleInput: String): Network {
            return Network(sampleInput.lineSequence().map { Module.of(it) }
                .associateBy(Module::name))
        }
    }

}
