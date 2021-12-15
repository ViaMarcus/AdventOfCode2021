@OptIn(ExperimentalStdlibApi::class)
object Day15 {
	fun solvePart1() {
		val lines = object {}::class.java.getResource("inputDay15.txt")!!.readText().lines()
		val map = lines.map { it.toCharArray().map { char -> Integer.parseInt(char.toString()) } }
		solve(map)
	}

	fun solvePart2() {
		val lines = object {}::class.java.getResource("inputDay15.txt")!!.readText().lines()
		val map = lines.map { it.toCharArray().map { char -> Integer.parseInt(char.toString()) }.toMutableList() }
		val newLines = mutableListOf<List<Int>>()

		for (i in lines.indices) {
			val tmp = map[i].toList()
			map[i].addAll(tmp.map { (it + 1 - 1) % 9 + 1 })
			map[i].addAll(tmp.map { (it + 2 - 1) % 9 + 1 })
			map[i].addAll(tmp.map { (it + 3 - 1) % 9 + 1 })
			map[i].addAll(tmp.map { (it + 4 - 1) % 9 + 1 })
		}

		for (i in 0..4) {
			for (j in map.indices) {
				val newLine = map[j].map { (it + i - 1) % 9 + 1 }
				newLines.add(newLine)
			}
		}
		solve(newLines)
	}

	private fun solve(map: List<List<Int>>) {
		val ySize = map.size
		val xSize = map.first().size
		val nodeMap = mutableMapOf<Pair<Int, Int>, Node>()
		for (y in 0 until ySize) {
			for (x in 0 until xSize) {
				val node = Node(x = x, y = y, weight = map[y][x])
				nodeMap[Pair(x, y)] = node
			}
		}
		nodeMap.forEach { it.value.setNeighbours(nodeMap) }
		val start = nodeMap[Pair(0, 0)]!!
		start.start = true
		start.min = 0
		update.invoke(start)
		println(nodeMap[Pair(xSize - 1, ySize - 1)]!!.min)
	}

	private val update = DeepRecursiveFunction<Node, Unit> { node ->
		val newMin = node.neighbours.minByOrNull { it.min }!!.min + node.weight
		val didUpdate = node.min > newMin
		if (didUpdate) {
			node.min = newMin
		}
		if (didUpdate || node.start) node.neighbours.toSet().forEach { callRecursive(it) }
	}

	class Node(
		val x: Int,
		val y: Int,
		var up: Node? = null,
		var left: Node? = null,
		var right: Node? = null,
		var down: Node? = null,
		val weight: Int
	) {
		val neighbours = mutableSetOf<Node>()
		var min = 1_000_000
		var start = false

		fun setNeighbours(nodeMap: MutableMap<Pair<Int, Int>, Node>) {
			up = nodeMap[Pair(x, y - 1)]
			down = nodeMap[Pair(x, y + 1)]
			left = nodeMap[Pair(x - 1, y)]
			right = nodeMap[Pair(x + 1, y)]
			neighbours.addAll(listOfNotNull(up, down, left, right))
		}
	}
}