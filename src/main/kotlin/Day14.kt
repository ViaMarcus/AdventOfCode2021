object Day14 {
	fun solvePart1(){
		val lines = object {}::class.java.getResource("inputDay14.txt")
			.readText()
			.lines()
		val seed = lines.first()
		println(seed)
		val rules = lines
			.takeLast(lines.size - 2)
			.associateBy( {it.take(2) } ) { it.last() }

		println(rules)
		var string = seed
		repeat(10) {
			val newString = string.toCharArray().toMutableList()
			for (i in 1 until string.length) {
				newString.add(i * 2 - 1, rules[string.substring(i-1..i)]!!)
			}

			string = newString.joinToString(separator = "")
			println(string)
			println(string.length)
		}
		string.toCharArray().sortedArray().groupBy { it }.map { it.value.size }.sorted().run {
			println(last() - first())
		}
	}

	fun solvePart2() {
		val start = System.currentTimeMillis()
		val lines = object {}::class.java.getResource("inputDay14.txt")
			.readText()
			.lines()
		val seed = lines.first()
		println(seed)
		val rules = lines
			.takeLast(lines.size - 2)
			.associateBy( {it.take(2) } ) {
				val key = it.take(2)
				val last = it.last()
				Pair("${key.first()}$last", "$last${key.last()}")
			}

		println(rules)
		var pairsMap = mutableMapOf<String, Long>()
		seed.reduce { prev, next ->
			val key = "$prev$next"
			pairsMap.computeIfAbsent(key) { 0 }
			pairsMap.compute(key) { _, oldValue -> oldValue?.plus(1) }
			return@reduce next
		}

		repeat(40) {
			val newMap = mutableMapOf<String, Long>()
			println(pairsMap)
			pairsMap.forEach { (key, value) ->
				val pairsToAdd = rules[key]!!
				val firstPair = pairsToAdd.first
				val secondPair = pairsToAdd.second

				newMap.compute(firstPair) { _, oldValue -> oldValue?.plus(value) ?: value }
				newMap.compute(secondPair) { _, oldValue -> oldValue?.plus(value) ?: value }
			}
			pairsMap = newMap
		}

		val letters = mutableMapOf<Char, Long>()
//
		pairsMap.forEach { (key, value) ->
			letters.compute(key[0]) { _, oldValue -> oldValue?.plus(value) ?: value}
		}
		letters.compute(seed.last()) { _, oldValue -> oldValue?.plus(1) ?: 1}

		println(letters)
		letters.map { it.value }.sorted().run {
			println(last() - first())
		}
		println(System.currentTimeMillis() - start)
	}
}