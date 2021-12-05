import java.util.*

fun main() {
    fun part1(input: List<VentLine>): Int {
        val result = input
            .filter { it.isCardinal() }
            .fold(VentMap(input)) { map, line -> map.applyVentLine(line) }
        println(result)
        return result.getVentNumGreaterOne()
    }

    fun part2(input: List<VentLine>): Int {
        val result = input
            .fold(VentMap(input)) { map, line -> map.applyVentLine(line) }
        println(result)
        return result.getVentNumGreaterOne()
    }

    val input = readInput("Day05")
    val ventLines = input.map { VentLine(it) }

    println(part1(ventLines))
    println(part2(ventLines))
}

class VentMap(input: List<VentLine>) {

    private val vents: List<MutableList<Vent>>

    init {
        val maxX = input.map { line -> line.coordinateA }
            .plus(input.map { line -> line.coordinateB })
            .map { it.x }
            .maxOf { it }

        val maxY = input.map { line -> line.coordinateA }
            .plus(input.map { line -> line.coordinateB })
            .map { it.y }
            .maxOf { it }

        vents = IntRange(0, maxY)
            .map {
                IntRange(0, maxX)
                    .map { Vent(0) }
                    .toMutableList()
            }.toMutableList()
    }

    fun applyVentLine(line: VentLine): VentMap {
        line.coordinateA.getCoordinatesTo(line.coordinateB).forEach { vents[it.y][it.x] = vents[it.y][it.x].inc() }
        return this
    }

    fun getVentNumGreaterOne(): Int {
        return vents.flatten().filter { it.num > 1 }.size
    }

    override fun toString(): String {
        return vents.toString()
            .replace(",", "")
            .replace("] [", "]\n[")
            .replace(" ", "")
            .substring(1)
    }
}

data class Vent(val num: Int) {
    override fun toString(): String {
        return if (num != 0) num.toString() else "."
    }

    operator fun inc(): Vent = copy(num = num + 1)
}

data class Coordinate(
    val input: String,
    val x: Int = input.split(",")[0].toInt(),
    val y: Int = input.split(",")[1].toInt()
) {
    fun getCoordinatesTo(otherCoordinate: Coordinate): List<Coordinate> {
        val biggerX = if (x > otherCoordinate.x) x else otherCoordinate.x
        val smallerX = if (x < otherCoordinate.x) x else otherCoordinate.x
        val biggerY = if (y > otherCoordinate.y) y else otherCoordinate.y
        val smallerY = if (y < otherCoordinate.y) y else otherCoordinate.y

        if (biggerX == smallerX) {
            return IntRange(smallerY, biggerY)
                .map { Coordinate("", smallerX, it) }
        }
        if (biggerY == smallerY) {
            return IntRange(smallerX, biggerX)
                .map { Coordinate("", it, smallerY) }
        }
        if((otherCoordinate.x > x) xor (otherCoordinate.y > y)) {
            val coords: MutableList<Coordinate> = Collections.emptyList<Coordinate?>().toMutableList()
            for (i in biggerX downTo smallerX)
                coords.add(Coordinate("", i, smallerY + biggerX - i))
            return coords
        }

        return IntRange(smallerX, biggerX)
            .map { Coordinate("", it, smallerY + it - smallerX) }
    }

    override fun toString(): String {
        return "$x, $y"
    }
}

data class VentLine(
    val input: String,
    val coordinateA: Coordinate = Coordinate(input.split(" -> ")[0]),
    val coordinateB: Coordinate = Coordinate(input.split(" -> ")[1])
) {
    fun isCardinal(): Boolean {
        return coordinateA.x == coordinateB.x || coordinateA.y == coordinateB.y
    }
}