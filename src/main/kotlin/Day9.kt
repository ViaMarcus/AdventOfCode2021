object Day9 {
    fun solveDay9Part1() {
        val fileContent = object {}::class.java.getResource("test.txt").readText()
        val lines = fileContent.lines().toMutableList()
        var matrix = lines.map { it.split("") }
        // Ooor we do it in Excel...
    }
}