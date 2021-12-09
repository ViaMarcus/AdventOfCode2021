import java.util.regex.Pattern
import kotlin.system.exitProcess

object Day9 {
    fun solveDay9Part2() {
        val fileContent: String = object {}::class.java.getResource("inputDay9.txt")?.readText() ?: exitProcess(1)
        val lines = fileContent.lines().toMutableList()
        // Create a border of nines around the grid
        val topAndBottom = "9".repeat(lines[0].length)
        lines.add(0, topAndBottom)
        lines.add(topAndBottom)
        lines.forEach { println(it) }
        val matrix = lines.map { line ->
            val padded = "9" + line + "9"
            return@map padded.trim().split(Pattern.compile(""), line.length + 2).map { it.toInt() }
        }
        val lowPoints = getLowPoints(matrix)
        println(lowPoints.map { mapBasin(matrix, it) }
            .sortedDescending()
            .take(3)
            .reduce { acc, i -> acc * i  }
        )
    }

    private fun getLowPoints(map: List<List<Int>>): Set<Pair<Int, Int>> {
        val lowPoints = mutableSetOf<Pair<Int, Int>>()
        for (i in 1..map.size - 2) {
            for (j in 1..map[0].size - 2) {
                val current = map[i][j]
                if (map[i - 1][j] > current &&
                    map[i + 1][j] > current &&
                    map[i][j + 1] > current &&
                    map[i][j - 1] > current) {
                    lowPoints.add(Pair(i,j))
                }
            }
        }
        return lowPoints
    }

    fun mapBasin(map: List<List<Int>>, lowPoint: Pair<Int, Int>): Int {
        val basinCoords = mutableSetOf(lowPoint)
        var previousSize = 0
        while(previousSize != basinCoords.size) {
            previousSize = basinCoords.size
            val toAdd = mutableSetOf<Pair<Int, Int>>()
            basinCoords.forEach { (x, y) ->
                if (map[x - 1][y] != 9) {
                    toAdd.add(Pair(x - 1, y))
                }
                if (map[x + 1][y] != 9) {
                    toAdd.add(Pair(x + 1, y))
                }
                if (map[x][y + 1] != 9) {
                    toAdd.add(Pair(x, y + 1))
                }
                if (map[x][y - 1] != 9) {
                    toAdd.add(Pair(x, y - 1))
                }
            }
            basinCoords.addAll(toAdd)
        }
        return basinCoords.size
    }
}