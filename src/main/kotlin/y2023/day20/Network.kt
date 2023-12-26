package y2023.day20

import util.lcm
import java.math.BigInteger
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

    val allModules get() = modules.values.toList()

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

    fun firstLow(name: String) = firstLow(module(name))

    private fun firstLow(module: Module): Long = when (module) {
        is SinkModule -> firstLow(allModules.first { it.connect.contains(module.name) })
        is ConjunctionModule -> findFirstLow(module)
        else -> throw IllegalArgumentException("cannot compute first low of $module")
    }

    private fun findFirstLow(module: ConjunctionModule): Long {
        val lengths = mutableMapOf<String, Long>()
        val inputs = module.inputs

        forever().filter { it.second.high && it.second.to == module.name }
            .forEach { (round, pulse) ->
                lengths.putIfAbsent(pulse.from, round)

                if (lengths.keys == inputs) {
                    return lengths.values.map { BigInteger.valueOf(it) }.reduce(::lcm)
                        .longValueExact()
                }
            }
        throw IllegalStateException("reached end of forever...")
    }

    companion object {
        fun setup(sampleInput: String): Network {
            return Network(sampleInput.lineSequence().map { Module.of(it) }
                .associateBy(Module::name))
        }
    }
}
