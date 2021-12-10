object Day10 {
    fun solveDay10Part1(): List<String> {
        val lines = object {}::class.java.getResource("inputDay10.txt")
            .readText()
            .lines()
        val reduced = lines.map {
            replaceValid(it)
        }
        val total = reduced.map { score(it) }.reduce { acc, i -> acc + i }
        println("part1=$total") //Solution to this part

        return reduced.filter { score(it) == 0 } // Reuse code for part 2
    }

    fun solveDay10Part2() {
        val incomplete = solveDay10Part1()
        val scores = incomplete.map {
            val reversed = it.reversed()
            var completingString = ""
            var scoreString = ""
            reversed.forEach { char ->
                when (char) {
                    '(' -> {
                        scoreString += 1
                        completingString += ')'
                    }
                    '[' -> {
                        scoreString += 2
                        completingString += ']'
                    }
                    '{' -> {
                        scoreString += 3
                        completingString += '}'
                    }
                    '<' -> {
                        scoreString += 4
                        completingString += '>'
                    }
                    else -> {}
                }
            }
            val lineTotal = if (scoreString.isNotEmpty()) scoreString.toLong(5) else 0
            println("$it scored $lineTotal")
            lineTotal
        }
        println(scores.sorted()[scores.size / 2])
    }

    private fun score(bracketSequence: String): Int {
        val pad = bracketSequence + "x"
        val first = pad.first {
            listOf(')', ']', '>', '}', 'x').contains(it)
        }
        return when (first) {
            ')' -> 3
            ']' -> 57
            '}' -> 1197
            '>' -> 25137
            else -> 0
        }
    }

    private fun replaceValid(it: String): String {
        var prevLen = -1
        var currStr = it
        while (currStr.length != prevLen) {
            prevLen = currStr.length
            currStr = currStr
                .replace("<>", "")
                .replace("()", "")
                .replace("[]", "")
                .replace("{}", "")
        }
        return currStr
    }

}
