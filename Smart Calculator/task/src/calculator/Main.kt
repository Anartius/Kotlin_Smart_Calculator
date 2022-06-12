package calculator

fun main() {

    while (true) {
        when (val input = readln()) {
            "/help" -> println("2 -- 2 equals 2 - (-2) equals 2 + 2")

            "/exit" -> {
                println("Bye!")
                return
            }

            else -> if (input.contains("/")) {
                println("Unknown command")
            } else if (input.matches("\\s*".toRegex())) {
                continue
            } else printResult(input)
        }
    }
}


fun getInputAsList(input: String) : MutableList<String> {
    val inputList = input.replace("\\s+".toRegex(), " ")
        .split(" ").toMutableList()

    for (i in inputList.indices) {
        if (inputList[i].matches("[+-]+".toRegex())) {
            inputList[i] = if ((inputList[i].count { it == '-' } + 2) % 2 == 1) "-" else "+"
        }
    }

    return inputList
}


fun calculateAll(inputList: MutableList<String>) : String {
    var a: Int
    var b: Int
    var tempResult = 0
    var command: String

    if (inputList.size == 1) return inputList[0].toInt().toString()

    for (i in 0..(inputList.size - 2) / 2) {
        a = if (i == 0) inputList[i * 2].toInt() else tempResult
        command = inputList[i * 2 + 1]
        b = inputList[i * 2 + 2].toInt()

        tempResult = calculateTwo(a, b, command)
    }

    return tempResult.toString()
}


fun calculateTwo(a: Int, b: Int, command: String) : Int {
    return when(command) {
        "+" -> a + b
        "-" -> a - b
        else -> 0
    }
}


fun printResult(input: String) {
    val inputAsList = getInputAsList(input)

    try {
        println(calculateAll(inputAsList))
    } catch (e: Exception) {
        println("Invalid expression")
    }
}