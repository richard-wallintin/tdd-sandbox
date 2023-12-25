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

    private suspend fun SequenceScope<Pulse>.enqueue(q: Queue<Pulse>, pulse: Pulse) {
        yield(pulse)
        q.offer(pulse)
    }

    private fun warmup(maxRounds: Int = 1_000) = sequence {
        val seen = mutableSetOf(currentState)
        (1..maxRounds).forEach { round ->
            pushButton().forEach {
                yield(round to it)
            }
            if (!seen.add(currentState))
                return@sequence
        }
    }

    private fun forever() = sequence {
        var round = 0L
        while (true) {
            round++
            pushButton().forEach { yield(round to it) }
        }
    }

    private fun cycle(maxRounds: Int): Pair<Int, Long> {
        var (high, low) = 0 to 0

        val lastRound = warmup(maxRounds).onEach { (_, pulse) ->
            if (pulse.high) high++ else low++
        }.maxOf { it.first }

        return lastRound to (high.toLong() * low.toLong())
    }

    fun cyclePulseValue(totalRounds: Int = 1_000): Long {
        val (cycle, pulseValue) = cycle(totalRounds)
        return pulseValue * (totalRounds / cycle).let { it * it }
    }

    fun firstRound(predicate: (Pulse) -> Boolean) =
        forever().first { (_, pulse) -> predicate(pulse) }.first

    companion object {
        fun setup(sampleInput: String): Network {
            return Network(sampleInput.lineSequence().map { Module.of(it) }
                .associateBy(Module::name))
        }
    }
}
