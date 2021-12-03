import java.io.File

fun main(args: Array<String>) {
    println(getDepthIncreases(readInput("Day01")))
}

//bad way!! not kotlin style!
fun getDepthIncrease(depths: List<String>): Int {

    var increases = 0
    var prevDepth = Int.MAX_VALUE
    for (i in 0 until depths.size - 2) {
        var depth1 = depths[i].toInt()
        var depth2 = depths[i + 1].toInt()
        var depth3 = depths[i + 2].toInt()
        var depthSum = depth1 + depth2 + depth3
        if (depthSum > prevDepth) {
            increases++
        }
        prevDepth = depthSum
    }

    return increases;
}

fun getDepthIncreases(depths: List<String>): Int{
    return depths
        .map { it.toInt() }
        .windowed(3)
        .map { list -> list.sum() }
        .windowed(2)
        .map { list: List<Int> -> list[0] < list[1] }
        .filter { increase -> increase }
        .size
}