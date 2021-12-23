data class Space(
	var amph: Amphipod? = null,
	var previous: Space? = null,
	var next: Space? = null,
	var branch: Space? = null, // this.branch.branch == this
	var right: Space? = null,
	val homeType: Flavor? = null
) {
	fun blockingFor(am: Amphipod): Boolean {
		return !(amph == null && homeType?.equals(am.flavor) == false )
	}

	fun addRoom(homeType: Flavor) {
		branch = Space(homeType = homeType)
		branch!!.branch = this
		branch!!.next = Space(branch = branch, homeType = homeType)
		branch!!.next!!.previous = branch!!.next
	}

	fun draw(firstLine: StringBuilder = StringBuilder(), secondLine: StringBuilder = StringBuilder(""), thirdLine: StringBuilder = StringBuilder(), drawBranch: Boolean = true) {
		firstLine.append(this.toString())
		secondLine.append(branch ?: " ")
		thirdLine.append(branch ?: " ")
		next?.draw(firstLine, secondLine, thirdLine)
		if (drawBranch) branch?.draw(thirdLine, StringBuilder(), StringBuilder(), false)
		previous ?: kotlin.run {
			println(firstLine.toString())
			println(secondLine.toString())
			println(thirdLine.toString())
		}
	}

	override fun toString(): String {
		return amph.toString()
	}
}