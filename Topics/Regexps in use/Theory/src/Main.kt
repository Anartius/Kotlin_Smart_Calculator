// You can experiment here, it won’t be checked

fun main(args: Array<String>) {
    val str = readln()
    val amount = readln().toInt()
    println(str.split("\\s+".toRegex(), amount).joinToString("\n"))
}
