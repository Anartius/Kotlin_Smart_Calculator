package calculator

fun main() {
    val numbers = mutableListOf<Int>()

    while (true) {

        when (val input = readln()) {
            "/help" -> println("The program calculates the sum of numbers")

            "/exit" -> {
                println("Bye!"); return
            }

            else -> {
                numbers.clear()

                try {
                    numbers.addAll(input.split(" ").map { it.toInt() }.toList())
                } catch (e: NumberFormatException) {
                    continue
                }

                println(numbers.sum())
            }
        }
    }
}