package com.example.firstapp

data class BoardItem(val name: String, val phone: String, val email: String): Comparable<BoardItem> {
    var isVisible: Boolean = true

    override fun compareTo(other: BoardItem): Int {
        return this.name.compareTo(other.name)
    }
}