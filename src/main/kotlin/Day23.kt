import java.util.*
import kotlin.math.abs
import kotlin.math.pow

object Day23 {
	private fun getInitHallway(test: Boolean): Hallway {
		val file = if (test) "test.txt" else "inputDay23.txt"
		val lines = object {}::class.java.getResource(file)!!
			.readText()
			.lines()
		return Hallway(
			room1 = Room(
				occ = lines.drop(2).map { it[3] },
				homeType = Flavor.A
			),
			room2 = Room(
				occ = lines.drop(2).map { it[5] },
				homeType = Flavor.B
			),
			room3 = Room(
				occ = lines.drop(2).map { it[7] },
				homeType = Flavor.C
			),
			room4 = Room(
				occ = lines.drop(2).map { it[9] },
				homeType = Flavor.A
			)
		)
	}

	private fun getNodeTree(test: Boolean): Space {
		val head = Space()
		var prevEntry: Space? = head
		repeat(10) {
			val newNode = Space(previous = prevEntry)
			prevEntry?.next?.let { it.next = newNode }
			prevEntry = newNode
		}
		var currentNode = head
		Flavor.values().forEach {
			currentNode = currentNode.next!!.next!!
			currentNode.addRoom(it)
		}
		return head
	}

	fun part1() {
		val init = getInitHallway(true)
		init.print()
		val possibleStates = mutableSetOf(init)

		while (possibleStates.isNotEmpty()) {
			val newStates = mutableSetOf<Hallway>()
			possibleStates.forEach { state ->
				when {

				}

			}
		}

	}

}

class Hallway2(
	val start: List<List<Amphipod>>
) {
	val leftStash1 = HallwayNode()
	val leftStash2 = HallwayNode(left = leftStash1).also { leftStash1.right = it }
	val room1Node = HallwayNode(left = leftStash2).also { leftStash2.right = it }
	val parking1 = HallwayNode(left = room1Node).also { room1Node.right = it }
	val room2Node = HallwayNode(left = parking1).also { parking1.right = it }
	val parking2 = HallwayNode(left = room2Node).also { room2Node.right = it }
	val room3Node = HallwayNode(left = parking2).also { parking2.right = it }
	val parking3 = HallwayNode(left = room3Node).also { room3Node.right = it }
	val room4Node = HallwayNode(left = parking3).also { parking3.right = it }
	val rightStash1 = HallwayNode(left = room4Node).also { room4Node.right = it }
	val rightStash2 = HallwayNode(left = rightStash1).also { rightStash1.right = it }

	val occupiable = listOf(leftStash1, leftStash2, parking1, parking2, parking3, rightStash1, rightStash2)
	val roomNodes = listOf(room1Node, room2Node, room3Node, room4Node)

}


class HallwayNode(
	var left: HallwayNode? = null,
	var right: HallwayNode? = null,
	var room: RoomNode? = null
) {

}

class RoomNode(
	var inner: RoomNode? = null,
	var outer: RoomNode? = null,
	var exit: HallwayNode? = null
) {

}

data class Hallway(
	val room1: Room,
	val room2: Room,
	val room3: Room,
	val room4: Room,
	private val hall:	LinkedList<Amphipod?> = LinkedList()
) {
	init {
		repeat(11) { hall.add(null) }
	}

	var totalCost = 0

	fun print() {
		println("*" + hall.joinToString(separator = "") { it?.toString() ?: "." } + "*")
		repeat(4) {
			println("***${room1.occ[it]}*${room2.occ[it]}*${room3.occ[it]}*${room4.occ[it]}***")
		}
	}

	fun isDone() = room1.isDone() && room2.isDone() && room3.isDone() && room4.isDone()

	fun copy() {
		Hallway(
			room1.copy(), room2.copy(), room3.copy(), room4.copy(), hall.clone() as LinkedList<Amphipod?>
		)
	}
}

data class Room(
	val occ: List<Char>,
	val homeType: Flavor,
	val exitNode: Amphipod
) {
	fun distanceToSlot(otherRoom: Room, fromInner: Boolean, toInner: Boolean): Int {
		if (otherRoom.homeType == this.homeType) {
			return (fromInner != toInner).toInt()
		} else {
			val sum = abs(Flavor.values().indexOf(this.homeType) - Flavor.values().indexOf(otherRoom.homeType)) * 2
			+ 4 + fromInner.toInt() + toInner.toInt()
		}
		return 1
	}

	fun isDone(): Boolean {
		return !occ.any { Flavor.valueOf(it.toString()) != homeType }
	}

	fun enterable() {
		!occ.any { ".${homeType}".contains(it) } // We only contain the correct type or empty spaces
	} // When true, we may stuff stuff in here

	fun exit() {

	}

	override fun equals(other: Any?): Boolean {

		return super.equals(other)
	}
}

data class Amphipod(
	val flavor: Flavor
) {
	constructor(char: Char): this(
		Flavor.valueOf(char.toString())
	)

	val cost =  10.0.pow(Flavor.values().indexOf(flavor)).toInt()

	override fun toString(): String {
		return flavor.toString()
	}
}

enum class Flavor {
	A, B, C, D
}

fun Boolean.toInt() = if (this) 1 else 0