package calculator

fun main() {
    val (a, b) = readln().split(" ").map { it.toInt() }.toList()
    println(a + b)
}