object Day11 {
	fun solvePart1() {
		val lines = object {}::class.java.getResource("test.txt")
			.readText()
			.lines()
		val grid = lines.map { line -> line.toCharArray().map { it.code } }

	}

}
