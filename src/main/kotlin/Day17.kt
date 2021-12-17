import kotlin.math.abs
import kotlin.math.sqrt

object Day17 {
	fun part2() {
		val input = object {}::class.java.getResource("inputDay17.txt")!!.readText()
		val ints = input.replace("target area: x=", "")
			.replace("..", ",")
			.replace(", y=", ",")
			.split(",")
			.map { it.toInt() }
		val xMin = ints[0]
		val xMax = ints[1]
		val yMin = ints[2]
		val yMax = ints[3]
		val minXSpeed = sqrt(xMin * 2.0).toInt() - 1
		val viableXSpeeds = mutableSetOf<Pair<Int, Int>>()
		for (speed in minXSpeed  .. xMax) {
			var distance = 0
			var timeSteps = 0
			var currSpeed = speed
			while (distance < xMax && currSpeed >= 0 && timeSteps < 3 * abs(yMin)) {
				distance += currSpeed
				currSpeed = (--currSpeed).coerceAtLeast(0)
				timeSteps++
				if (distance in xMin..xMax) viableXSpeeds.add(Pair(speed, timeSteps))
			}
		}
		val viableSpeeds = mutableSetOf<Pair<Int, Int>>()
		for (pair in viableXSpeeds) {

			for (ySpeed in yMin .. -yMin ) {
				if (sumYSpeed(ySpeed, pair.second) in yMin..yMax)
					viableSpeeds.add(Pair(pair.first, ySpeed))
			}
		}
		println(viableSpeeds)
		println(viableSpeeds.size)
	}

	private fun sumYSpeed(startSpeed: Int, steps: Int): Int {
		if (steps == 1) return startSpeed
		return startSpeed + sumYSpeed(startSpeed - 1, steps - 1)
	}
}
