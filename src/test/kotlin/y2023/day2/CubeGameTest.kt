package y2023.day2

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import y2023.day2.Cubes.Companion.blue
import y2023.day2.Cubes.Companion.green
import y2023.day2.Cubes.Companion.red
import kotlin.math.max


class CubeGameTest {

    private val referenceBag = 12.red + 13.green + 14.blue

    private val refGame1 = Game(
        id = 1,
        sets = listOf(
            3.blue + 4.red,
            1.red + 2.green + 6.blue,
            2.green
        )
    )

    private val refGame3 = Game(
        id = 1,
        sets = listOf(
            8.green + 6.blue + 20.red,
            5.blue + 4.red + 13.green,
            5.green + 1.red
        )
    )

    @Test
    fun `Game possible vs Bag`() {
        //Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
        refGame1.possible(referenceBag) shouldBe true

        //Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
        refGame3.possible(referenceBag) shouldBe false
    }

    @Test
    fun `cubes subset check`() {
        ((3.blue + 4.red) in referenceBag) shouldBe true
        ((8.green + 4.red + 20.red) in referenceBag) shouldBe false
    }

    @Test
    fun `parse game`() {
        Game.of("Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green") shouldBe refGame1
    }

    private val refData = """
                Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
                Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
                Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
                Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
                Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
            """.trimIndent()

    @Test
    fun `reference part one`() {
        refData.countPossibleGames() shouldBe 8
    }

    private val aocInput = AOC.getInput("/2023/day2.txt")

    @Test
    fun `part one`() {
        aocInput.countPossibleGames() shouldBe 2169
    }

    private fun String.countPossibleGames() = lineSequence()
        .map { Game.of(it) }
        .filter { it.possible(referenceBag) }
        .map { it.id }
        .sum()

    @Test
    fun power() {
        refGame1.minSet shouldBe Cubes(red = 4, blue = 6, green = 2)
        refGame3.minSet shouldBe Cubes(red = 20, blue = 6, green = 13)

        Cubes(red = 4, blue = 6, green = 2).power shouldBe 4 * 6 * 2
        refGame1.power shouldBe 4 * 6 * 2
    }

    @Test
    fun `ref part two`() {
        refData.lineSequence().map { Game.of(it).power }.sum() shouldBe 2286
    }

    @Test
    fun `part two`() {
        aocInput.lineSequence().map { Game.of(it).power }.sum() shouldBe 60_948
    }
}

data class Cubes(val red: Int = 0, val blue: Int = 0, val green: Int = 0) {
    val power: Int = red * blue * green

    operator fun plus(o: Cubes) =
        copy(red = this.red + o.red, blue = this.blue + o.blue, green = this.green + o.green)

    operator fun contains(o: Cubes): Boolean {
        return this.red >= o.red && this.blue >= o.blue && this.green >= o.green
    }

    fun min(o: Cubes): Cubes {
        return Cubes(
            red = max(this.red, o.red),
            green = max(this.green, o.green),
            blue = max(this.blue, o.blue)
        )
    }

    companion object {
        fun of(s: String): Cubes {
            return s.split(Regex("\\s*,\\s*")).fold(Cubes()) { cubes, str ->
                cubes + (Regex("(\\d+)\\s+(red|green|blue)").matchEntire(str)?.destructured?.let { (no, color) ->
                    when (color) {
                        "red" -> Cubes(red = no.toInt())
                        "green" -> Cubes(green = no.toInt())
                        "blue" -> Cubes(blue = no.toInt())
                        else -> throw IllegalArgumentException("unknown color $color")
                    }

                } ?: throw IllegalArgumentException("cant parse cubes $str"))
            }
        }

        val Int.blue: Cubes get() = Cubes(blue = this)
        val Int.red: Cubes get() = Cubes(red = this)
        val Int.green: Cubes get() = Cubes(green = this)
    }
}

data class Game(val id: Int, val sets: List<Cubes>) {
    val power by lazy { minSet.power }
    val minSet: Cubes by lazy { sets.reduce(Cubes::min) }

    fun possible(bag: Cubes) = sets.all { it in bag }

    companion object {
        fun of(s: String): Game {
            return Regex("Game (\\d+): (.+)").matchEntire(s)?.destructured?.let { (gameID, sets) ->
                Game(
                    id = gameID.toInt(),
                    sets = sets.split(Regex(";\\s*")).map {
                        Cubes.of(it)
                    }
                )
            } ?: throw IllegalArgumentException("cant parse game: $s")
        }
    }
}
