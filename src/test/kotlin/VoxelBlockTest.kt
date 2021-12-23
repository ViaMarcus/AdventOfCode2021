import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.math.abs

internal class VoxelBlockTest {
	val vox1 = VoxelBlock(
		-10, -10, -10, 10, 10, 10
	)
	val vox2 = VoxelBlock(
		0, 0, 0, 10, 10, 10
	)

	val vox3 = VoxelBlock(
		-4, -4, -4, 15, 15, 15
	)

	@Test
	fun size() {
		assertEquals(21*21*21, vox1.size)
		assertEquals(11*11*11, vox2.size)
	}

	@Test
	fun removeChunk() {
		val newChunks = vox1.removeChunk(vox2)
		assertEquals(3, newChunks.size )
		val newChunks2 = vox1.removeChunk(vox3)
		assertEquals(3, newChunks2.size)
	}

	@Test
	fun getIntersect() {
		val intersect = vox1.getIntersect(vox2) ?: fail()
		assertEquals(1331, intersect.size)
		val intersect2 = vox2.getIntersect(vox1) ?: fail()
		assertEquals(1331, intersect2.size)
	}
}