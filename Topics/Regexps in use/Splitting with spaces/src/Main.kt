fun main() {
    val str = readln()
    val amount = readln().toInt()
    println(str.split("\\s+".toRegex(), amount).joinToString("\n"))
}