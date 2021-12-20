import kotlin.math.ceil
import kotlin.math.floor

val log = false

class SnailNumber(
	var left: SnailNumber? = null,
	var right: SnailNumber? = null,
	var parent: SnailNumber? = null
) {
	var literal: Int? = null

	init {
		setParents()
	}

	constructor(literal: Int): this() {
		this@SnailNumber.literal = literal
	}

	fun setParents() {
		left?.parent = this@SnailNumber
		right?.parent = this@SnailNumber
	}

	@Suppress("ControlFlowWithEmptyBody")
	fun reduce(): SnailNumber {
		while (true) {
			println(this)
			explode() ?: split() ?: break
		}
		return this
	}

	fun explode(): Any? {
		try {

			val depth = getDepth()
			if (depth == 4 && literal == null) {
				if (log) println("exploding $this")
				findLiteralToLeft()?.addToLiteral(left!!.literal!!)
				findLiteralToRight()?.addToLiteral(right!!.literal!!)
				left = null
				right = null
				literal = 0
				return "arse"
			} else if (literal == null) {
				return left!!.explode() ?: right!!.explode()
			}
			return null
		} catch (ex: Exception) {
			println(this)
			return null
		}
	}

	private fun findLiteralToLeft(): SnailNumber? {
		parent ?: return null
		return if (parent?.left != this) {
			if (log) println("finding rightmost literal in ${parent!!.left}")
			parent?.left!!.findRightMostLiteral()
		} else {
			if (log) println("navigating1 up to $parent")
			parent?.findLiteralToLeft().let { if (log) print(", was: $it"); it }
		}
	}

	private fun findLiteralToRight(): SnailNumber? {
		parent ?: return null
		return if (parent?.right != this) {
			if (log) println("finding leftmost literal in ${parent!!.right}")
			parent?.right!!.findLeftMostLiteral()
		} else {
			if (log) println("navigating2 up to $parent")
			parent?.findLiteralToRight().let { if (log) print(", was: $it"); it }
		}
	}

	private fun findRightMostLiteral(): SnailNumber {
		return if (literal != null) this else right!!.findRightMostLiteral()
	}

	private fun findLeftMostLiteral(): SnailNumber {
		return if (literal != null) this else left!!.findLeftMostLiteral()
	}

	private fun split(): Any? {
		if (literal != null && literal!! >= 10) {
			if (log) println("splitting $this")
			val half = literal!!.toDouble() / 2
			left = SnailNumber(literal = floor(half).toInt())
			right = SnailNumber(literal = ceil(half).toInt())
			setParents()
			literal = null
			return "ass"
		} else if (literal == null) {
			return left!!.split() ?: right!!.split()
		}
		return null
	}

	private fun addToLiteral(toAdd: Int) {
		literal = literal?.plus(toAdd)
	}

	private fun getDepth(): Int {
		parent ?: return 0
		return parent!!.getDepth() + 1
	}

	override fun toString(): String {
		if (literal != null) return literal.toString()
		return "[${left.toString()},${right.toString()}]"
	}

	fun getMagnitude(): Long {
		val leftMag = left?.getMagnitude()
		val rightMag = right?.getMagnitude()
		return literal?.toLong() ?: (3 * leftMag!! + 2 * rightMag!!)
	}
}