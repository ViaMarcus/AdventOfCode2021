fun <T> Sequence<T>.repeat() = sequence { while (true) yieldAll(this@repeat) }

fun <T> Iterator<T>.take(n: Int): List<T> {
	val list = mutableListOf<T>()
	repeat(n) {
		list.add(next())
	}
	return list
}

object Day21 {
	fun part1() {
		var dieRolls = 0
		val lines = object {}::class.java.getResource("inputDay21.txt")!!
			.readText()
			.lines()
		val player1 = Player(position = lines.first().last().digitToInt(), score = 0)
		val player2 = Player(position = lines.last().last().digitToInt(), score = 0)
		val die = (1..100).asSequence().repeat()
		val dieIntIterator = die.iterator()
		var loserScore: Int

		while (true) {
			val player1Roll = dieIntIterator.take(3).sum().also { println("player1 moves $it") }
			dieRolls += 3
			player1.move(player1Roll)
			if (player1.score >= 1000) {
				loserScore = player2.score
				println("player1 won")
				break
			}

			val player2Roll = dieIntIterator.take(3).sum().also { println("player2 moves $it") }
			dieRolls += 3
			player2.move(player2Roll)
			if (player2.score >= 1000) {
				println("player2 won")
				loserScore = player1.score
				break
			}
		}
		println(loserScore * dieRolls)
	}

	fun part2() {
		val lines = object {}::class.java.getResource("inputDay21.txt")!!
			.readText()
			.lines()
		val player1 = Player(position = lines.first().last().digitToInt(), score = 0)
		val player2 = Player(position = lines.last().last().digitToInt(), score = 0)
		var unis = mutableMapOf(Universe(player1, player2) to 1L)
		var player1Wins = 0L
		var player2Wins = 0L
		var player1Turn = true
		wh@ while (true) {
			val newUnis = mutableMapOf<Universe, Long>()
			if (unis.isEmpty()) break@wh
			for (uni in unis) {
				val increment = uni.value
				val split = uni.key.split(player1Turn)
				split.forEach { newUni ->
					newUnis.compute(newUni) { key, value ->
						if (key.hasAWinner()) {
							if (player1Turn) player1Wins += increment else player2Wins += increment
							return@compute null
						}
						return@compute (value ?: 0L) + increment
					}
				}
			}
			println("player1 wins is now $player1Wins, player2 wins is now $player2Wins")
			unis = newUnis.toMutableMap()
			player1Turn = !player1Turn
		}
	}
}

data class Player(
	var score: Int,
	var position: Int,
) {
	fun move(amt: Int): Player {
		position = (position + amt - 1) % 10 + 1
		score += position
		return this
	}

	fun copy() = Player(score, position)
}

data class Universe(val player1: Player, val player2: Player) {
	private val rollsMap = mapOf(
		3 to 1,
		4 to 3,
		5 to 6,
		6 to 7,
		7 to 6,
		8 to 3,
		9 to 1
	)

	fun split(onPlayerOne: Boolean): List<Universe> {
		val newUniverses = mutableListOf<Universe>()
		rollsMap.forEach { entry ->
			repeat(entry.value) {
				if (onPlayerOne) {
					newUniverses.add(Universe(player1.copy().move(entry.key), player2))
				} else {
					newUniverses.add(Universe(player1, player2.copy().move(entry.key)))
				}
			}
		}
		return newUniverses.toList()
	}

	fun hasAWinner(): Boolean {
		return player1.score > 20 || player2.score > 20
	}
}