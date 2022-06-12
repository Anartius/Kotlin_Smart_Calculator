fun main() {
    val text = readln()
    println(".*Computer+.*".toRegex().matches(text))
}