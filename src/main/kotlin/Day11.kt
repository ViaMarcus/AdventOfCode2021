object Day11 {
	fun solvePart1() {
		val lines = object {}::class.java.getResource("test.txt")
			.readText()
			.lines()
		val grid = lines.map { line -> line.toCharArray().map { it.code }.toMutableList() }
		for (time in 1..100) {
			for (x in 0..9) {
				for (y in 0..9) {
					grid[x][y] += 1
				}
			}
		}
		val neededFlash = false
		do {
			for (time in 1..100) {
				for (x in 0..9) {
					for (y in 0..9) {
						if (grid[x][y] > 9) {

						}
					}
				}
			}
		} while (neededFlash)

	}
	private fun flash(x: Int, y: Int, grid: List<List<Int>>) {

	}
}

