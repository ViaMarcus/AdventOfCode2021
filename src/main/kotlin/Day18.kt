import com.google.gson.Gson
import com.google.gson.JsonArray

object Day18 {
	fun part1(testInput: String? = null): SnailNumber {
		val gson = Gson()
		val lines = (testInput ?: object {}::class.java.getResource("inputDay18.txt")!!.readText()).lines()
		val map = lines.map { jsonToSnailNumber(gson.fromJson(it, JsonArray::class.java), 0) }

		map.forEach { println("before: $it"); it.reduce(); println("after: $it") }
		if (map.size == 1) {
			return map.first()
		} else {
			val finalAnswer = map.reduce { acc, snailNumber ->
				println("adding $acc and  $snailNumber")
				val runSum = SnailNumber(left = acc, right = snailNumber)
				.also { it.setParents() }
				runSum.reduce()
				return@reduce runSum
			}
			println(finalAnswer.getMagnitude())
			return finalAnswer
		}
	}

	fun part2() {
		var maxValue = 0
		val gson = Gson()
		val lines = object {}::class.java.getResource("inputDay18.txt")!!.readText().lines()
		val map = lines.map { jsonToSnailNumber(gson.fromJson(it, JsonArray::class.java), 0) }

		map.forEach { it.reduce() }
		i@ for (i in map.indices) {
		j@	for (j in map.indices) {
				if (i == j) continue@j;
				val left = map[i]
				val right = map[j]
				val sn = part1(SnailNumber(left = left, right = right).toString())
				println(sn)
				sn.reduce()
				println(sn)
				val value =	sn.getMagnitude().toInt()
				println("first: $value")
				maxValue = maxValue.coerceAtLeast(value)
			}
		}
		println(maxValue)
	}

	fun jsonToSnailNumber(jsonArray: JsonArray, depth: Int): SnailNumber {
		val left = if (jsonArray.get(0).isJsonPrimitive) {
			SnailNumber(literal = jsonArray.get(0).asNumber.toInt())
		} else {
			jsonToSnailNumber(jsonArray.get(0).asJsonArray, depth = depth + 1)
		}
		val right = if (jsonArray.get(1).isJsonPrimitive) {
			SnailNumber(literal = jsonArray.get(1).asNumber.toInt())
		} else {
			jsonToSnailNumber(jsonArray.get(1).asJsonArray, depth = depth + 1)
		}
		return SnailNumber(left, right).also {
			it.setParents()
		}
	}
}
