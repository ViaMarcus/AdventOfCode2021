object Day20 {
	var key: List<Int>? = null

	fun part1() {
		val lines = object {}::class.java.getResource("inputDay20.txt")!!
			.readText()
			.lines()

		key = lines.first().toCharArray().map { if (it == '#') 1 else 0 }
		println(key)
		println()

		val imageLines = lines
			.takeLast(lines.size - 2)
			.toMutableList()
		val lineSize = imageLines[0].length
		val paddingLine = ".".repeat(lineSize)
		val paddingSize = 53
		repeat(paddingSize) { imageLines.add(0, paddingLine)}
		repeat(paddingSize) { imageLines.add(paddingLine) }
		val paddingString = ".".repeat(paddingSize)
		val image = imageLines.map { line ->
			(paddingString + line + paddingString).toCharArray().map { if (it == '#') 1 else 0 }
		}

		var xIndices = image[0].indices
		var yIndices = image.indices
		image.forEach { printImage(it) }
		val newImage: MutableList<MutableList<Int>> = mutableListOf()
		for (y in yIndices ) {
			val line = mutableListOf<Int>()
			for (x in xIndices) {
				line.add(get3x3Char(image, x, y,  0))
			}
			newImage.add(line)
		}
		repeat(2) { println() }
		newImage.forEach { printImage(it) }
		xIndices = newImage[0].indices
		yIndices = newImage.indices
		val newImage2: MutableList<MutableList<Int>> = mutableListOf()
		for (y in yIndices ) {
			val line = mutableListOf<Int>()
			for (x in xIndices) {
				line.add(get3x3Char(newImage, x, y, 1))
			}
			newImage2.add(line)
		}
		repeat(2) { println() }
		newImage2.forEach { printImage(it) }
		val pixelCount = newImage2.sumOf { line -> line.sumOf { it } }
		println(pixelCount)
	}

	private fun get3x3Char(image: List<List<Int>>, x: Int, y: Int, default: Int): Int {
		val index =
			256 * getCoordinateOrDefault(image, x - 1, y - 1, default) +
			128 * getCoordinateOrDefault(image, x, y - 1, default) +
			64 * getCoordinateOrDefault(image, x + 1, y - 1, default) +

			32 * getCoordinateOrDefault(image, x - 1, y, default) +
			16 * getCoordinateOrDefault(image, x, y, default) +
			8  * getCoordinateOrDefault(image, x + 1, y, default) +

			4 * getCoordinateOrDefault(image, x - 1, y + 1, default) +
			2 * getCoordinateOrDefault(image, x, y + 1, default) +
			1 * getCoordinateOrDefault(image, x + 1, y + 1, default)
		return key!![index]
	}

	private fun getCoordinateOrDefault(image: List<List<Int>>, x: Int, y: Int, default: Int): Int {
		val xIndices = image[0].indices
		val yIndices = image.indices

		return if (x in xIndices && y in yIndices) image[y][x] else default
	}

	private fun printImage(line: List<Int>) {
		println(
			line.joinToString(separator = "") { if (it == 1) "#" else "." }
		)
	}

}
