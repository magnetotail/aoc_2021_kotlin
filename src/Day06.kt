fun main() {
    fun part1(input: List<String>): Int {
        val swarm = FishSwarm(input)
        val finalSwarm = IntRange(0, 79).fold(swarm) { swarm, it ->
            swarm.age()
        }
        println(finalSwarm)
        return finalSwarm.getSwarmSize()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("Day06")
    println(FishSwarm(input))

    println(part1(input))
    println(part2(input))
}

class FishSwarm(input: List<String>) {

    private var swarm: MutableMap<LanternFish, Count>

    init {
        val map: MutableMap<LanternFish, Count> = HashMap<LanternFish, Count>().toMutableMap()
        swarm = input.map { s -> s.split(",") }
            .flatten()
            .map { LanternFish(it.toInt()) }
            .fold(map) { map, fish ->
                map.putIfAbsent(fish, Count(0))
                map[fish]?.num = if (map.get(fish) != null) map[fish]?.num?.plus(1)!! else 1
                return@fold map
            }
    }

    fun age(): FishSwarm {
        swarm = swarm
            .mapKeys { fish -> fish.key.dec() }
            .toMutableMap()
        var readyFish :Int = 0
        for (mutableEntry in swarm) {
            if (mutableEntry.key.age == 0) {
                readyFish = mutableEntry.value.num
            }
        }

        swarm.computeIfAbsent(LanternFish(6)) { Count(0) }.increaseBy(readyFish)
        swarm.computeIfAbsent(LanternFish(8)) { Count(0) }.increaseBy(readyFish)
        swarm = swarm.filter { entry -> entry.key.age != 0 }.toMutableMap()
        return this
    }

    fun getSwarmSize(): Int {
        return swarm.map { it.value.num }.sum()
    }

    override fun toString(): String {
        return swarm.toString()
    }

}

data class LanternFish(var age: Int) {

    operator fun dec(): LanternFish {
        return copy(age = age-1)
    }

    fun readyForBirth(): Boolean {
        return age == 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LanternFish

        if (age != other.age) return false

        return true
    }

    override fun hashCode(): Int {
        return age
    }
}

data class Count(var num: Int) {
    fun increaseBy(number: Int) :Count {
        num += number
        return this
    }
}