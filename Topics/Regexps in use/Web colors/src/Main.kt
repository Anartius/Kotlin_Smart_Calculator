fun main() {
    val text = readLine()!!
    val regexColors = "#[0-9a-fA-F]{6}\\b".toRegex()
    val matches = regexColors.findAll(text)
    for (item in matches) println(item.value)
}