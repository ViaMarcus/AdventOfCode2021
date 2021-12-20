import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SnailNumberTest {
	val sn1 = Day18.part1("[[[[[9,8],1],2],3],4]")
	val sn2 = Day18.part1("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]")
	val sn3 = Day18.part1("[[[[5,0],[7,4]],[5,5]],[6,6]]")

	@Test
	fun `with sn1, explode gives the correct result`() {
		sn1.explode()
		assertEquals("[[[[0,9],2],3],4]", sn1.toString())
	}

	@Test
	fun `with sn2, explode gives the correct result`() {
		sn2.explode()
		assertEquals("[[3,[2,[8,0]]],[9,[5,[7,0]]]]", sn2.toString())
	}

	@Test
	fun `with sn3, magnitude gives correct result`() {
		sn3.getMagnitude()
		assertEquals(1137, sn3.getMagnitude())
	}
}