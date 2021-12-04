fun main() {

    val input = readInput("Day04")

    val boards: List<BingoBoard> =
        input.slice(IntRange(2, input.size -1))
            .windowed(5, 6)
            .map { list -> BingoBoard(list) }

    val boardInputs = input[0]
        .split(",")
        .map { it.toInt() }

    //part 1
    for (num in boardInputs) {
        boards.forEach{it.applyNumber(num)}
        val solvedBoards = boards.filter { it.isSolved() }
        if (solvedBoards.isNotEmpty()) {
            println("Found solved Board at number $num")
            println("Solution to puzzle: " + solvedBoards[0].sumOfAllUnmarked() * num)
            break
        }
    }

    //part 2
    var board: BingoBoard? = null
    for (num in boardInputs) {
        boards.forEach{it.applyNumber(num)}
        val unSolvedBoards = boards.filter { !it.isSolved() }
        if (unSolvedBoards.size == 1 || unSolvedBoards.isEmpty()) {
            if(unSolvedBoards.size == 1){
                board = unSolvedBoards[0]
            }
            if (board?.isSolved() == true) {
                println("Last board to win solved solved with number $num")
                println("Solution to puzzle2: " + board.sumOfAllUnmarked() * num)
                break
            }
        }
    }
}

class BingoBoard(input: List<String>) {

    private val board: List<List<Field>> //Spalte, Zeile, Zahl

    data class Field(val number: Int, var checked: Boolean)

    init {
        board =
            input.map { it.split(Regex("\\s+")) }
                .map {
                    it.filter { s -> s.isNotEmpty() }
                    .map { numberString -> Field(numberString.toInt(), false) }
                }
    }

    fun applyNumber(number: Int) {
        val field:Field? = board.flatten().firstOrNull { field -> field.number == number }
        if(field != null)
            field.checked = true
    }

    fun isSolved(): Boolean = hasFinishedRow() || hasFinishedColumn()

    private fun hasFinishedRow(): Boolean {
        val checkedRows: List<Boolean> =
            board.map { column ->
                column.map { field -> field.checked }
                    .filter { it }
                    .size == board.size
            }
                .filter { it }
        return checkedRows.isNotEmpty()
    }

    private fun hasFinishedColumn(): Boolean {
        val checkedColumns: List<Boolean> =
            IntRange(0, board.size -1)
                .map { column ->
                    board.flatten()
                        .slice(IntRange(column, board.size * board[0].size - column -1))
                        .windowed(1, board.size)
                        .flatten()
                        .map {field -> field.checked }
                        .filter { it }
                        .size == board.size
                }
                .filter { it }
        return checkedColumns.isNotEmpty()
    }

    fun sumOfAllUnmarked(): Int {
        return board.flatten()
            .filter { !it.checked }
            .sumOf { it.number }
    }

    fun printBoard() {
        board.forEach {println(it)}
    }

}

