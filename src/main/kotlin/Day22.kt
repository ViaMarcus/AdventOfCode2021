import kotlin.math.abs

object Day22 {
	val log = false
	fun getInput(file: String, filter: Boolean): List<Operation> {
		return object {}::class.java.getResource(file)!!
			.readText()
			.lines()
			.map {
				return@map it.split(",", "=", "..", " ")
			}.map {
				Operation(
					state = it[0] == "on",
					x1 = it[2].toInt(), x2 = it[3].toInt(),
					y1 = it[5].toInt(), y2 = it[6].toInt(),
					z1 = it[8].toInt(), z2 = it[9].toInt(),
				)
			}.filterNot { filter && !it.smol }
	}

	fun part1() {
		val operations = getInput("inputDay22.txt", true)

		val onVoxels = mutableSetOf<Voxel>()
		operations.forEach { op ->
			for (z in op.z1..op.z2) {
				for (y in op.y1..op.y2) {
					for (x in op.x1..op.x2) {
						val voxel = Voxel(x, y, z)
						if (op.state) {
							onVoxels.add(voxel)
						} else {
							onVoxels.remove(voxel)
						}
					}
				}
			}
		}
		println(onVoxels.size)
	}

	fun part2() {
		val operation = getInput("alexDay22.txt", false)
		val onBlocks = mutableListOf<VoxelBlock>()

		operation.forEachIndexed { index, newOp ->
			println("$index: running op $newOp")
			println("number of onblocks is now ${onBlocks.size}")
			val operationBlock = VoxelBlock(newOp)
			val newOnBlocks = mutableSetOf<VoxelBlock>()
			val blocksToRemove = mutableSetOf<VoxelBlock>()
			onBlocks.stream().parallel().forEach { onBlock ->
				onBlock.getIntersect(operationBlock)?.let { intersect ->
					if (log) println("$newOp on \n$onBlock gave intersect \n$intersect")
					blocksToRemove.add(onBlock)
					val subChunks = onBlock.removeChunk(intersect)
					if (log) println("generated $subChunks new chunks")
					synchronized(Any()) { newOnBlocks.addAll(subChunks) }
				}
			}
			if (log) println("adding ${newOnBlocks.size} new blocks")
			onBlocks.addAll(newOnBlocks)
			if (log) println("removing ${blocksToRemove.size}")
			onBlocks.removeAll(blocksToRemove)
			if (newOp.state) onBlocks.add(operationBlock)
		}
		println("total is ${onBlocks.sumOf { it.size }}")
	}
}

data class Voxel(val x: Int, val y: Int, val z: Int)

data class VoxelBlock(
	val x1: Int, val y1: Int, val z1: Int,
	val x2: Int, val y2: Int, val z2: Int,
) {
	private val xRange = x1..x2
	private val yRange = y1..y2
	private val zRange = z1..z2

	constructor(op: Operation): this(
		x1 = op.x1, x2 = op.x2, y1 = op.y1, y2 = op.y2, z1 = op.z1, z2 = op.z2
	)

	val size = (x2 - x1 + 1).toLong() * (y2 - y1 + 1) * (z2 - z1 + 1)

	fun removeChunk(other: VoxelBlock): Set<VoxelBlock> {
		if (other == this) return emptySet()
		if (other.size == 0L) return setOf(this)
		if (other.size < 0) throw IllegalStateException()

		val solidSubChunks = mutableSetOf<VoxelBlock>()
		// Baseplate, the missing chunk does not go below this ones floor, so we need a base
		if (other.z1 in (z1 + 1) .. z2) solidSubChunks.add(
			VoxelBlock(x1, y1, z1, x2, y2, other.z1 - 1)
		)
		// Middle parts
		// Back
		if (other.y2 in y1 until y2) {
			solidSubChunks.add(
				VoxelBlock(x1, other.y2 + 1, other.z1, x2, y2, other.z2)
			)
		}

		//Front
		if (other.y1 in (y1 + 1)..y2) {
			solidSubChunks.add(
				VoxelBlock(x1, y1, other.z1, x2, other.y1 - 1, other.z2)
			)
		}
		// Left
		if (other.x1 in (x1 + 1)..x2) {
			solidSubChunks.add(
				VoxelBlock(x1, other.y1, other.z1, other.x1 - 1, other.y2, other.z2)
			)
		}

		//Right
		if (other.x2 in x1 until x2) {
			solidSubChunks.add(
				VoxelBlock(other.x2 + 1, other.y1, other.z1, x2, other.y2, other.z2)
			)
		}

		//Top plate, the missing chunk does not poke through the top of this one, so we need a cap
		if (other.z2 in z1 until z2) solidSubChunks.add(
			VoxelBlock(x1, y1, other.z2 + 1, x2, y2, z2)
		)
		return solidSubChunks
	}

	fun getIntersect(other: VoxelBlock): VoxelBlock? {
		val xIntersect = xRange.intersect(other.xRange)
		val yIntersect = yRange.intersect(other.yRange)
		val zIntersect = zRange.intersect(other.zRange)
		return if (xIntersect.isEmpty() || yIntersect.isEmpty() || zIntersect.isEmpty()) {
			null
		} else {
			VoxelBlock(
				x1 = xIntersect.first(),
				x2 = xIntersect.last(),
				y1 = yIntersect.first(),
				y2 = yIntersect.last(),
				z1 = zIntersect.first(),
				z2 = zIntersect.last()
			)
		}
	}
}

data class Operation(
	val x1: Int, val y1: Int, val z1: Int,
	val x2: Int, val y2: Int, val z2: Int,
	val state: Boolean
) {
	val smol = listOf(abs(x1), abs(x2), abs(y1), abs(y2), abs(z1), abs(z2)).maxOrNull()!! <= 50

}