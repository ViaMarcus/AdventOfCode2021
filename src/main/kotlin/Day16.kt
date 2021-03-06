object Day16 {
	var totalVer = 0

	fun part1() {
		val hexInput = object {}::class.java.getResource("inputDay16.txt")!!.readText()
		val binary = hexInput.toCharArray().map {
			val int = it.toString().toInt(16)
			listOf(int / 8, (int /4) % 2, (int / 2) % 2, int % 2)
		}.flatten()
		val binaryInputLength = binary.size
		println("size of binary input is $binaryInputLength")
		println(binary)
		val iter = binary.toIntArray().iterator()
		val result = readPackage(iter)
		println(result)
	}

	private fun readPackage(iter: IntIterator): Long {
		try {
			val typeId = readHeaders(iter)
			println("Package with typeId $typeId was read")
			return when (typeId) {
				0 -> readOperator(iter).reduce { acc, l -> acc + l }
				1 -> readOperator(iter).reduce { acc, l -> acc * l }
				2 -> readOperator(iter).minOrNull()!!
				3 -> readOperator(iter).maxOrNull()!!
				4 -> readLiteral(iter)
				5 -> readOperator(iter).reduce { val1, val2 -> return@reduce if (val1 > val2) 1 else 0 }
				6 -> readOperator(iter).reduce { val1, val2 -> return@reduce if (val1 < val2) 1 else 0 }
				else -> readOperator(iter).reduce { val1, val2 -> return@reduce if (val1 == val2) 1 else 0 }
			}
		} finally {
			println("EOF reached, totalVer is now $totalVer, ")
		}
	}

	private fun readHeaders(iter: IntIterator): Int {
		val packetVer = 4 * iter.next() + 2 * iter.next() + iter.next()
		totalVer += packetVer
		return 4 * iter.next() + 2 * iter.next() + iter.next() // typeId
	}

	private fun readOperator(iter: IntIterator): List<Long> {
		val lengthTypeId = iter.next()
		val list = mutableListOf<Long>()
		println("op with lenId = $lengthTypeId")
		if (lengthTypeId == 0) {
			val length = readNBits(iter, 15).toInt(2)
			val subPackages = readNBits(iter, length)
			println(subPackages)
			val subPackagesIter = subPackages.map { it.digitToInt() }.toIntArray().iterator()
			while (subPackagesIter.hasNext()) {
				list.add(readPackage(subPackagesIter))
			}
		} else {
			val numberOfSubPacks = readNBits(iter, 11).toInt(2)
			repeat(numberOfSubPacks) {
				list.add(readPackage(iter))
			}
		}
		return list
	}

	private fun readLiteral(iter: IntIterator): Long {
		var read = true
		var string = ""
		while (read) {
			read = iter.next() == 1
			string += "${iter.next()}${iter.next()}${iter.next()}${iter.next()}"
		}
		val literal = string.toLong(2)
		println("read literal $literal")
		return literal
	}

	private fun readNBits(iter: IntIterator, numberOfBits: Int): String {
		val sb = StringBuilder()
		for (i in 0 until numberOfBits) sb.append(iter.next())
		return sb.toString()
	}
}
