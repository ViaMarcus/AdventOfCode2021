import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Hallway2Test {
	val hallway = Day23.getInitHallway(true)

	@Test
	fun construct() {
	}

	@Test
	fun traverse() {
		assertEquals(4, hallway.leftStash.distanceToNode(hallway.parking2))
		assertEquals(14, hallway.rooms[0].node4.distanceToNode(hallway.rooms[3].node4))
	}

	@Test
	fun findPath() {
		val path = mutableListOf<Node>()
		hallway.leftestStash.getPathBetweenNodes(hallway.parking2, null, path)
		assertEquals(6, path.size)
	}

	@Test
	fun hashString() {
		assertEquals("BDDACCBDBBACDACA.......",  hallway.hashString)
	}

	@Test
	fun hashCodeTest() {
		assertNotEquals("BDDACCBDBBACDACA.......".hashCode(), "BDDACCBDBBACDAC.....A..".hashCode())
	}
}