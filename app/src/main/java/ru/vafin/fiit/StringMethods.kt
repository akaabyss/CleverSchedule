package ru.vafin.fiit

fun String.toDefaultString(): String {
    var newStr: String = this[0].uppercase()
    for (index in 1..this.lastIndex) {
        newStr += this[index].lowercase()
    }
    return newStr
}
