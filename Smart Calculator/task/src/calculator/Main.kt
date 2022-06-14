package calculator

import java.math.BigInteger

class InvalidExpression : Exception()

class Stack {
    private val operators = mapOf("+" to 0, "-" to 0,
        "*" to 1, "/" to 1, "^" to 1, "(" to 1, ")" to 1)

    private val stack = mutableListOf<Pair<String, Int>>()

    fun isEmpty() = stack.isEmpty()

    fun getTopValue() = stack.last().first

    fun getPriority(value: String) = operators.getValue(value)

    fun getTopPriority() = stack.last().second

    fun push(value: String) {
        if (operators.containsKey(value)) {
            stack.add(value to operators.getValue(value))
        } else stack.add(value to 0)
    }

    fun pop() : String {
        val result = "" + stack.last().first
        stack.removeLast()
        return result
    }

    fun printState() {
        print("stack: ")
        stack.forEach { print("${it.first} ") }
        println("\n")
    }
}


fun main() {
    val inputAsPostfix = mutableListOf<String>()
    val vars = mutableMapOf<String, BigInteger>()

    while (true) {
        val input = readln().trim()

        if (input.contains("^/[a-z]+".toRegex())) {
            when (input) {
                "/help" -> println("""
                    Supported operations: +, -, *, /, ^(power).
                    2 -- 2 equals 2 - (-2) equals 2 + 2.                    
                """.trimIndent())

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
            try {
                val inputAsList = getInputAsList(input, vars)
                inputAsPostfix.addAll(toPostfix(inputAsList))
                println(getResult(inputAsPostfix))
            } catch (e:InvalidExpression) {
                println("Invalid expression")
                continue
            }
        }
    }
}


fun toPostfix(infix: MutableList<String>) : MutableList<String> {
    val stack = Stack()
    val postfix = mutableListOf<String>()

    for (i in infix.indices) {

        if (infix[i].matches("-?\\d+".toRegex())) {
            postfix.add(infix[i])

        } else if (infix[i].matches("^[-+*/^()]$".toRegex())) {
            val currentItemPriority = stack.getPriority(infix[i])

            if (infix[i] != ")" && (stack.isEmpty() ||
                        stack.getTopPriority() < currentItemPriority)) {

                stack.push(infix[i])

            } else if (infix[i] == "(") {
                stack.push(infix[i])

            } else if (infix[i] == ")") {
                if (stack.getTopValue() != "(") {
                    while (stack.getTopValue() != "(") {
                        postfix.add(stack.pop())
                    }
                    stack.pop()
                }

            } else if ((stack.getTopPriority() >= currentItemPriority)) {
                if (stack.getTopValue() != "(") {
                    while (!stack.isEmpty() && stack.getTopValue() != "(" &&
                        (stack.getTopPriority() >= currentItemPriority)) {

                        postfix.add(stack.pop())
                    }
                    stack.push(infix[i])

                } else {
                    stack.push(infix[i])
                }
            }
        }

        if (i == infix.size - 1) {
            while (!stack.isEmpty()) postfix.add(stack.pop())
        }
    }

    return postfix
}


fun getInputAsList(input: String,
                   vars: MutableMap<String, BigInteger>) : MutableList<String> {

    if (input.contains("[*/^]{2,}".toRegex())) throw InvalidExpression()


    val regex = """(^-*\d+)|(~?\d+)|\)|[-+]+|\(|[*/^]|[a-zA-z]""".toRegex()
    val inputString = input.replace("\\(-".toRegex(), "(~")
        .replace("\\s+".toRegex(), "")

    val inputList = regex.findAll(inputString).map { it.value }.toMutableList()

    for (i in inputList.indices) {
        inputList[i] = inputList[i].replace("~", "-")

        if (inputList[i].matches("[+-]+".toRegex())) {
            inputList[i] = if ((inputList[i].count { it == '-' } + 2) % 2 == 1) {
                "-"
            } else "+"
        }

        if (vars.contains(inputList[i])) {
            inputList[i] = vars.getValue(inputList[i]).toString()
        }
    }
    checkInputList(inputList)

    return inputList
}


fun checkInputList(inputList: MutableList<String>) {

    val amountOfDigital = inputList.count { "-?\\d+".toRegex().matches(it) }
    val amountOfOperators = inputList.count { "[-+*/^]".toRegex().matches(it) }
    val amountOfLeftPar = inputList.count { it == "(" }
    val amountOfRightPar = inputList.count { it == ")" }

    if (amountOfDigital != amountOfOperators + 1 ||
            amountOfLeftPar != amountOfRightPar) throw InvalidExpression()
}


fun getVariable(input: String, vars: MutableMap<String, BigInteger>) :
        Map<String,BigInteger> {
    val map = mutableMapOf<String, BigInteger>()

    if (input.matches("[a-zA-Z]+\\s*=\\s*-?\\d+".toRegex())) {
        val (name, value) = input.replace(" ", "")
            .split("=")
        map[name] = value.toBigInteger()

    } else if (input.matches("[a-zA-Z0-9]+\\s*=\\s*\\d+".toRegex())) {
        println("Invalid identifier")

    } else if (input.matches("[a-zA-Z]+\\s*=\\s*[a-zA-Z]+".toRegex())) {
        val (var1, var2) = input.replace(" ", "")
            .split("=")
        if (vars.contains(var2)) {
            map[var1] = vars.getValue(var2)
        } else println("Unknown variable")

    } else println("Invalid assignment")

    return map
}


fun getResult(inputAsList: MutableList<String>) : String {
    val stack = Stack()
    var a: BigInteger
    var b: BigInteger

    for (i in inputAsList.indices) {
        if ("-?\\d+".toRegex().matches(inputAsList[i])) {
            stack.push(inputAsList[i])

        } else {
            b = stack.pop().toBigInteger()
            a = stack.pop().toBigInteger()
            when (inputAsList[i]) {
                "-" -> stack.push((a - b).toString())
                "+" -> stack.push((a + b).toString())
                "*" -> stack.push((a * b).toString())
                "/" -> {
                    if (b == BigInteger.ZERO) {
                        throw InvalidExpression()
                    } else stack.push(((a / b).toString()))
                }
                else -> stack.push((a.pow(b.toInt())).toString())
            }
        }
    }
    return stack.pop()
}