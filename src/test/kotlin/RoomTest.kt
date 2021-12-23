
import kotlin.test.Test

internal class RoomTest() {
	val aAmphi = Amphipod('A')
	val bAmphi = Amphipod('B')
	val node1 = RoomNode(element = aAmphi)
	val node2 = RoomNode(element = aAmphi)
	val node3 = RoomNode(element = aAmphi)
	val node4 = RoomNode(element = aAmphi)

	val nullNode = RoomNode(element = null)

	val bNode = RoomNode(element = bAmphi)

	val doneRoom = Room(
		node1, node2, node3, node4, Flavor.A
	)

	val notDoneRoom = Room(
		node1, node2, bNode, node3, Flavor.A
	)

	val fillableRoom = Room(
		node1, nullNode, nullNode, nullNode, Flavor.A
	)

	val emptyRoom = Room(
		nullNode, nullNode, nullNode, nullNode, Flavor.A
	)

	val bRoom = Room(
		node1, node2, node3, node4, Flavor.B
	)

	@Test
	fun isDone() {
		assert(doneRoom.isDone())
		assert(!notDoneRoom.isDone())
		assert(!fillableRoom.isDone())
		assert(!emptyRoom.isDone())
		assert(!bRoom.isDone())
	}

	@Test
	fun isFillable() {
		assert(doneRoom.isFillable())
		assert(!notDoneRoom.isFillable())
		assert(fillableRoom.isFillable())
		assert(emptyRoom.isFillable())
		assert(!bRoom.isFillable())
	}
}