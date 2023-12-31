package ru.vafin.cleverschedule

fun String.toDefaultString(): String {
    var newStr: String = this[0].uppercase()
    for (index in 1..this.lastIndex) {
        newStr += this[index].lowercase()
    }
    return newStr
}
