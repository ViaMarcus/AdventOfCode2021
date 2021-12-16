import java.lang.Integer.max
import kotlin.system.exitProcess

@OptIn(ExperimentalStdlibApi::class)
object Day15 {
	fun solvePart1() {
		val lines = object {}::class.java.getResource("inputDay15.txt")!!.readText().lines()
		val map = lines.map { it.toCharArray().map { char -> Integer.parseInt(char.toString()) } }
		println(map.flatten().average())
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
		println(newLines.flatten().average())
		solve2(newLines)
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

	private val update = DeepRecursiveFunction<Node, Int> { node ->
		val newMin = node.neighbours.minByOrNull { it.min }!!.min + node.weight
		val didUpdate = node.min > newMin
		if (didUpdate) {
			node.min = newMin
		}
		var depth = 1
		if (didUpdate || node.start) node.neighbours.forEach {
			it.let { nude ->
				depth = max(callRecursive(nude) + 1, depth)
			}
		}
		if (depth % 10_000 == 0) println(depth)
		return@DeepRecursiveFunction depth
	}

	fun solve2(map: List<List<Int>>) {
		val ySize = map.size
		val naiveMin = map[0].reduce { acc, i -> acc + i } + map.map { it[0] }.reduce { acc, i -> acc + i }
		val xSize = map.first().size
		val nodeMap = mutableMapOf<Pair<Int, Int>, Node>()
		for (y in 0 until ySize) {
			for (x in 0 until xSize) {
				val node = Node(x = x, y = y, weight = map[y][x])
				nodeMap[Pair(x, y)] = node
			}
		}
		val list: Array<MutableList<Node>> = Array(naiveMin) { mutableListOf() }
		println(list.size)

		val start = nodeMap[Pair(0, 0)]!!
		val end = nodeMap[Pair(xSize - 1, ySize - 1)]
		start.setNeighbours(nodeMap)
		start.min = 0
		list[0].add(start)
		for (i in list.indices) {
			list[i].forEach { nowNode ->
				if (nowNode == end) { println(nowNode.min); exitProcess(0) } // we are done

				nowNode.setNeighbours(nodeMap)
				nowNode.neighbours.forEach { neigh ->
					if (neigh.min > nowNode.min + neigh.weight) {
						neigh.min = nowNode.min + neigh.weight
						list[neigh.min].add(neigh)
					}
				}
			}
		}

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