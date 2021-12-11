object Day5 {
	fun solveDay5Part2() {
		val fileContent = object {}::class.java.getResource("inputDay5.txt")
			.readText()
			.lines()
		val lines: List<Set<Pair<Int, Int>>> = fileContent
			.map { it.replace(" -> ", ",").split(",").map { it.toInt() } }
			.map { line ->
				val forward = line[0] < line[2]
				if (forward) {
					return@map Line(line[0], line[1], line[2], line[3]).getCoords()
				} else {
					return@map Line(line[2], line[3], line[0], line[1]).getCoords()
				}
			}
		val numberOfCrosses = lines
			.flatten()
			.groupBy { it }
			.map { it.value.size }
			.filter { it >= 2 }
			.size
		println(numberOfCrosses)
	}
}

class Line(val x1: Int, val y1: Int, val x2: Int, val y2: Int) {
	val xAdd = if (x1 == x2) 0 else 1
	val yAdd = if (y1 == y2) 0 else if (y2 > y1) 1 else -1

	fun getCoords(): Set<Pair<Int, Int>> {
		var done = false
		val coords = mutableSetOf<Pair<Int, Int>>()
		var x = x1
		var y = y1
		while (!done) {
			coords.add(Pair(x, y))
			println("Added coord ($x, $y) for line $x1,$y1 -> $x2,$y2")
			done = x == x2 && y == y2
			x += xAdd
			y += yAdd
		}
		return coords
	}
}

