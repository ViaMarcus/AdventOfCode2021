import kotlin.math.min
import kotlin.math.pow
import kotlin.system.exitProcess

typealias NodeList = MutableList<Node>

object Day23 {
	fun getInitHallway(test: Boolean): Hallway {
		val file = if (test) "test.txt" else "inputDay23.txt"
		val lines = object {}::class.java.getResource(file)!!
			.readText()
			.lines()
		val startPosition = lines.drop(2).take(4).map { listOf(it[3], it[5], it[7], it[9]) }
		return Hallway(startPosition)
	}

	fun part1() {
		val init = getInitHallway(true)
		val statesWithCost = mutableMapOf(init to 0)
		var smallestCostSoFar = Int.MAX_VALUE
		while (statesWithCost.isNotEmpty()) {
			val newStates = mutableMapOf<Hallway, Int>()
			println("working with ${statesWithCost.size} states")
			statesWithCost.keys.first().print()
			if (statesWithCost.size > 1_000_000) exitProcess(1)
			statesWithCost.forEach { (hallway, cost) ->
				val legalSwaps = hallway.getLegalSwaps()
				legalSwaps.forEach { (node1Id, node2Id) ->
					val newHallway = hallway.clone()
					val node1 = newHallway.getNodeFromId(node1Id)
					val node2 = newHallway.getNodeFromId(node2Id)
					val weight = node1.element?.cost ?: node2.element?.cost!!
					val addedCost = weight * node1.distanceToNode(node2)!!
					node1.swap(node2)
					if (newHallway.isDone()) {
						smallestCostSoFar = smallestCostSoFar.coerceAtMost(cost + addedCost)
					} else {
						newStates.compute(newHallway) { _, old -> min(cost + addedCost, (old ?: Int.MAX_VALUE)) }
					}
				}
			}
			statesWithCost.clear()
			statesWithCost.putAll(newStates)
		}
		println(smallestCostSoFar)
	}

}

class Hallway(
	val start: List<List<Char>>,
	corridorInit: String? = null
) {
	val leftestStash = HallwayNode()
	val leftStash = HallwayNode(left = leftestStash).also { leftestStash.prev = it }
	val room1Node = HallwayNode(left = leftStash).also { leftStash.prev = it }
	val parking1 = HallwayNode(left = room1Node).also { room1Node.prev = it }
	val room2Node = HallwayNode(left = parking1).also { parking1.prev = it }
	val parking2 = HallwayNode(left = room2Node).also { room2Node.prev = it }
	val room3Node = HallwayNode(left = parking2).also { parking2.prev = it }
	val parking3 = HallwayNode(left = room3Node).also { room3Node.prev = it }
	val room4Node = HallwayNode(left = parking3).also { parking3.prev = it }
	val rightStash = HallwayNode(left = room4Node).also { room4Node.prev = it }
	val rightestStash = HallwayNode(left = rightStash).also { rightStash.prev = it }

	val occupiable = listOf(leftestStash, leftStash, parking1, parking2, parking3, rightStash, rightestStash)
	val roomNodes = listOf(room1Node, room2Node, room3Node, room4Node)
	val hallwayNodes = listOf(leftestStash, leftStash,room1Node, parking1, room2Node, parking2, room3Node, parking3, room4Node, rightStash, rightestStash)
	val rooms = mutableListOf<Room>()

	var nodeListForIds: List<Node>

	var hashString = ""

	init {
		repeat(4) { rep ->
			val exit = roomNodes[rep]
			val amp1 = start[0][rep].takeIf { "ABCD".contains(it) }?.let { Amphipod(it) }
			val space1 = RoomNode(element = amp1, exit = exit).also { exit.branch = it }
			val amp2 = start[1][rep].takeIf { "ABCD".contains(it) }?.let { Amphipod(it) }
			val space2 = RoomNode(outer = space1, element = amp2).also { space1.next = it }
			val amp3 = start[2][rep].takeIf { "ABCD".contains(it) }?.let { Amphipod(it) }
			val space3 = RoomNode(outer = space2, element = amp3).also { space2.next = it }
			val amp4 = start[3][rep].takeIf { "ABCD".contains(it) }?.let { Amphipod(it) }
			val space4 = RoomNode(outer = space3, element = amp4).also { space3.next = it }
			rooms.add(Room(space1, space2, space3, space4, Flavor.values()[rep]))
		}
		corridorInit?.let {
			occupiable.forEachIndexed { index, node ->
				if (it[index] != '.') node.element = Amphipod(it[index])
			}
		}
		nodeListForIds = rooms.flatMap { room -> listOf(room.node1, room.node2, room.node3, room.node4) } + occupiable
	}

	fun gethashString() = nodeListForIds.joinToString("") { it.getChar().toString() }

	fun getLegalSwaps(): List<Pair<Int, Int>> {
		val legalActions = mutableListOf<Pair<Int, Int>>()
		occupiable.filter { it.element == null }.forEach { hallNode ->
			rooms.filter { !it.isDone() && !it.isFillable()}.forEach { room ->
				room.getOutermostNonEmpty()?.let {
					if (!it.pathIsBlocked(hallNode)) {
						legalActions.add(Pair(getNodeId(it), getNodeId(hallNode)))
					}
				}
			}
		}
		rooms.filter { !it.isDone() && it.isFillable() }.forEach { room ->
			occupiable.filter { hallwayNode ->
				hallwayNode.element?.flavor?.equals(room.flavor) == true
			}.forEach { hallnode ->
				if (!hallnode.pathIsBlocked(room.getInnerMostNonOccupied())) {
					legalActions.add(getNodeId(hallnode) to getNodeId(room.getInnerMostNonOccupied()))
				}
			}
		}
		return legalActions
	}

	fun getNodeId(node: Node) = nodeListForIds.indexOf(node)
	fun getNodeFromId(id: Int) = nodeListForIds[id]

	fun clone(): Hallway {
		val rooms = listOf(
			listOf(
				rooms[0].node1.element?.getChar() ?: '.',
				rooms[1].node1.element?.getChar() ?: '.',
				rooms[2].node1.element?.getChar() ?: '.',
				rooms[3].node1.element?.getChar() ?: '.'
			),
			listOf(
				rooms[0].node2.element?.getChar() ?: '.',
				rooms[1].node2.element?.getChar() ?: '.',
				rooms[2].node2.element?.getChar() ?: '.',
				rooms[3].node2.element?.getChar() ?: '.'
			),
			listOf(
				rooms[0].node3.element?.getChar() ?: '.',
				rooms[1].node3.element?.getChar() ?: '.',
				rooms[2].node3.element?.getChar() ?: '.',
				rooms[3].node3.element?.getChar() ?: '.'
			),
			listOf(
				rooms[0].node4.element?.getChar() ?: '.',
				rooms[1].node4.element?.getChar() ?: '.',
				rooms[2].node4.element?.getChar() ?: '.',
				rooms[3].node4.element?.getChar() ?: '.'
			),
		)

		return Hallway(rooms, occupiable.joinToString(separator = "") { it.getChar().toString() })
	}

	fun isDone() = rooms.all { it.isDone() }

	override fun hashCode(): Int {
		return gethashString().hashCode()
	}

	fun print() {
		println(hallwayNodes.joinToString(separator = "") { it.getChar().toString() })
		repeat(4) {
			print("  ")
			rooms.forEach { room ->
				print(room.nodes[it].getChar())
				print(" ")
			}
			println()
		}
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Hallway

		if (hashString != other.hashString) return false

		return true
	}
}

class Room(
	val node1: RoomNode,
	val node2: RoomNode,
	val node3: RoomNode,
	val node4: RoomNode,
	val flavor: Flavor
) {
	val nodes = listOf(node1, node2, node3, node4)

	fun getOutermostNonEmpty(): Node? {
		return when {
			node1.element != null -> node1
			node2.element != null -> node2
			node3.element != null -> node3
			node4.element != null -> node4
			else -> null
		}
	}

	fun isDone() = nodes.all { it.element?.flavor?.equals(flavor) == true }
	fun isFillable() = nodes.all { it.element?.flavor?.equals(flavor) != false }
	fun getInnerMostNonOccupied(): Node {
		return when {
			node4.element == null -> node4
			node3.element == null -> node3
			node2.element == null -> node2
			else -> node1
		}
	}
}

class HallwayNode(
	left: HallwayNode? = null,
	right: HallwayNode? = null,
	room: RoomNode? = null,
	element: Amphipod? = null
) : Node(element, left, right, room)

class RoomNode(
	inner: RoomNode? = null,
	outer: RoomNode? = null,
	exit: HallwayNode? = null,
	element: Amphipod?
) : Node(element, outer, inner, exit)

abstract class Node(
	var element: Amphipod?,
	var prev: Node?,
	var next: Node?,
	var branch: Node?
) {
	fun swap(other: Node) {
		val tmp = other.element
		other.element = element
		element = tmp
	}

	fun distanceToNode(other: Node): Int? {
		val returnList: NodeList = mutableListOf()
		getPathBetweenNodes(other, null, returnList)
		return returnList.size - 1
	}

	fun getPathBetweenNodes(other: Node, fromNode: Node? = null, returnList: NodeList = mutableListOf()): Boolean {
		if (other == this) {
			returnList.add(this)
			return true
		}
		if (prev?.takeIf { it != fromNode }?.getPathBetweenNodes(other, this, returnList) == true ||
			next?.takeIf { it != fromNode }?.getPathBetweenNodes(other, this, returnList) == true ||
			branch?.takeIf { it != fromNode }?.getPathBetweenNodes(other, this, returnList) == true
		) {
			returnList.add(this)
			return true
		}
		return false
	}

	fun pathIsBlocked(other: Node): Boolean {
		val returnList: NodeList = mutableListOf()
		getPathBetweenNodes(other, null, returnList)

		return returnList.dropLast(1).any { it.isBlocked() }
	}

	private fun isBlocked(): Boolean {
		return this.element != null
	}

	fun getChar(): Char {
		return element?.getChar() ?: '.'
	}

}

data class Amphipod(
	val flavor: Flavor
) {
	constructor(char: Char) : this(
		Flavor.valueOf(char.toString())
	)

	val cost = 10.0.pow(Flavor.values().indexOf(flavor)).toInt()

	fun getChar(): Char {
		return flavor.toString()[0]
	}
}

enum class Flavor {
	A, B, C, D
}