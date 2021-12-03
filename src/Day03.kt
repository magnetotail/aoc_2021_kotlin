fun main() {
    fun part1(input: List<String>): Int {
        return IntRange(0, input[0].length -1)
            .map { getMostCommonBit(input, it) }
            .joinToString("", "", "")
            .toInt(2) *
            IntRange(0, input[0].length -1)
            .map { getMostCommonBit(input, it) }
            .map { it.toBinaryBoolean().not().toBinaryString() }
            .joinToString("", "", "")
            .toInt(2)
    }

    fun part2(input: List<String>): Int {
        val valueOne = IntRange(0, input[0].length -1).fold(input) { list, it -> filterForMostCommon(list, it)}[0]
        val valueTwo = IntRange(0, input[0].length -1).fold(input) { list, it -> filterForLeastCommon(list, it)}[0]
        return valueOne.toInt(2) * valueTwo.toInt(2)
    }

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))

}

fun filterForMostCommon(input: List<String>, position: Int): List<String> {
    if(input.size == 1)
        return input
    return input.filter { it -> it[position] == getMostCommonBit(input, position).toString()[0] }
}

fun filterForLeastCommon(input: List<String>, position: Int): List<String> {
    if(input.size == 1)
        return input
    return input.filter { it -> it[position] == getLeastCommonBit(input, position).toString()[0] }
}

fun getMostCommonBit(input: List<String>, position: Int): String {
    val stringLength = input[0].length
    val num = input.map { it -> it.split("") }
        .flatten() // make sequential list of all numbers
        .filter { it -> it.isNotEmpty() } // filter empty strings from start/end of line
        .slice(IntRange(position, input.size * stringLength - 1)) // start at bit position we want to look at
        .windowed(1, stringLength, true) // take every nth bit
        .flatten() //smush everything together
        .filter { it -> it == "1" }
        .size // count ones to know if more ones or zeroes
    val result = num > input.size / 2 || input.size.toFloat() / num.toFloat() == 2f // check if even between ones and zeroes
//    println("input: $input. Least common for position $position: $result")
    return result.toBinaryString()
}

fun getLeastCommonBit(input: List<String>, position: Int): String {
    val stringLength = input[0].length
    val num = input.map { it -> it.split("") }
        .flatten() // make sequential list of all numbers
        .filter { it -> it.isNotEmpty() } // filter empty strings from start/end of line
        .slice(IntRange(position, input.size * stringLength - 1)) // start at bit position we want to look at
        .windowed(1, stringLength, true) // take every nth bit
        .flatten() //smush everything together
        .filter { it -> it == "1" }
        .size // count ones to know if more ones or zeroes
    val result = (num <= input.size / 2 && !(input.size.toFloat() / num.toFloat() == 2f)).toBinaryString() // check if even between ones and zeroes
//    println("input: $input. Least common for position $position: $result")
    return result
}

fun Boolean.toBinaryString(): String {
    if(this)
        return "1"
    return "0"
}

fun String.toBinaryBoolean(): Boolean {
    if(this == "1")
        return true
    return false
}

