package calculator

fun main() {

    val vars = mutableMapOf<String, Int>()

    while (true) {
        val input = readln().trim()

        if (input.contains("/")) {
            when (input) {
                "/help" -> println("2 -- 2 equals 2 - (-2) equals 2 + 2")

                "/exit" -> {
                    println("Bye!")
                    return
                }

                else -> println("Unknown command")
            }
        } else if (input.isEmpty()) {
            continue
        } else if (input.matches("^[a-zA-Z]+$".toRegex())) {
            if (vars.contains(input)) {
                println(vars.getValue(input))
            } else println("Unknown variable")
        }else if (input.contains("=")) {
            vars.putAll(getVariable(input, vars))
            continue
        } else {
            val inputAsList = getInputAsList(input, vars)
            printResult(inputAsList)
        }
    }
}


fun getInputAsList(input: String,
                   vars: MutableMap<String, Int>) : MutableList<String> {
    val inputList = input.replace("\\s+".toRegex(), " ")
        .split(" ").toMutableList()

    for (i in inputList.indices) {
        if (inputList[i].matches("[+-]+".toRegex())) {
            inputList[i] = if ((inputList[i].count { it == '-' } + 2) % 2 == 1) {
                "-"
            } else "+"
        }
    }

    for (i in inputList.indices) {
        if (vars.contains(inputList[i])) {
            inputList[i] = vars.getValue(inputList[i]).toString()
        }
    }

    return inputList
}


fun getVariable(input: String, vars: MutableMap<String, Int>) : Map<String,Int> {
    val map = mutableMapOf<String, Int>()

    if (input.matches("[a-zA-Z]+\\s*=\\s*-?\\d+".toRegex())) {
        val (name, value) = input.replace(" ", "")
            .split("=")
        map[name] = value.toInt()

    } else if (input.matches("[a-zA-Z0-9]+\\s*=\\s*\\d+".toRegex())) {
        println("Invalid identifier")

    } else if (input.matches("[a-zA-Z]+\\s*=\\s*[a-zA-Z]+".toRegex())) {
        val (var1, var2) = input.replace(" ", "")
            .split("=")
        if (vars.contains(var2)) {
            map[var1] = vars.getValue(var2)
        } else println("Unknown variable")

    }else println("Invalid assignment")

    return map
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


fun printResult(inputAsList: MutableList<String>) {


    try {
        println(calculateAll(inputAsList))
    } catch (e: Exception) {
        println("Invalid expression")
    }
}