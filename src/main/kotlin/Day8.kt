object Day8 {
  fun solveDay8Part2() {
    val fileContent = object {}::class.java.getResource("inputDay8.txt").readText()
    var total = 0
    val lines = fileContent.lines()
    println(lines[0])
    lines.forEach { line ->
      println("Line is $line")
      val split = line.split(" ")
      val inputs = split.take(10)
      val one = inputs.first { it.length == 2 }
      val four = inputs.first { it.length == 4 }
      val seven = inputs.first { it.length == 3 }
      val eight = inputs.first { it.length == 7 }

      val three = inputs.filter { it.length == 5 }
        .first { it.contains(one[0]) && it.contains(one[1]) }

      val six = inputs.filter { it.length == 6 }
        .first { !it.contains(one[0]) || !it.contains(one[1]) }

      val bd = four
        .toCharArray()
        .filter { !one.contains(it) }

      val five = inputs.filter { it.length == 5 }
        .first { it.contains(bd[0]) && it.contains(bd[1]) }

      val two = inputs.first { it.length == 5 && it != five && it != three }

      val nine = inputs.filter { it.length == 6 }
        .first { it.contains(bd[0]) && it.contains(bd[1]) && it != six }

      val zero = inputs.filter { it.length == 6 }
        .first { it != six && it != nine }

      println("0=$zero, 1=$one, 2=$two, 3=$three, 4=$four, 5=$five, 6=$six 7=$seven, 8=$eight, 9=$nine")
      val numbers = listOf(
        order(zero),
        order(one),
        order(two),
        order(three),
        order(four),
        order(five),
        order(six),
        order(seven),
        order(eight),
        order(nine),
      )
      val result = split.slice(11..14).map { order(it) }
      println(result)
      val value =
        1000 * numbers.indexOf(result[0]) + 100 * numbers.indexOf(result[1]) + 10 * numbers.indexOf(result[2]) + numbers.indexOf(
          result[3]
        )
      println(value)
      total += value
    }
    println("Total is $total")
  }

  fun order(string: String) = string.toCharArray().sortedArray().joinToString("")
}