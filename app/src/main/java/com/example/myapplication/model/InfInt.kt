package com.example.myapplication.model

import kotlinx.serialization.Serializable

@Serializable
class InfInt {
    protected var value = 0
    protected var level = 0

    protected fun normalize() {
        while (value >= 10_000 || value >= 1_000 && level > 0) {
            level++
            value /= 1000
        }
    }

    public constructor() {}

    public constructor(other: InfInt) {
        this.value = other.value
        this.level = other.level
    }

    public constructor(other: Int) {
        value = other
        normalize()
    }

    public override fun toString(): String {
        if (level == 0) {
            return value.toString()
        }
        return value.toString() + ('A' + level - 1)
    }

    public fun isEmpty(): Boolean {
        return value == 0 && level == 0
    }

    public fun duplicate(): Unit {
        value *= 2
        normalize()
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if (other !is InfInt) {
            return false
        }
        return (other as InfInt).level == level && (other as InfInt).value == value
    }

    // Функция сложение чисел
    // Гарантируется, что текущее число - наибольшее
    private fun sum(other: InfInt): InfInt {
        // Если наш уровень больше чем 1
        if (this.level - other.level > 1) {
            return this
        }
        val result = InfInt()
        result.level = other.level
        if (this.level - other.level == 1) {
            // Если наш уровень на 1 больше
            result.value = other.value + 1000 * this.value
        } else {
            result.value = other.value + this.value
        }
        result.normalize()
        return result
    }

    public operator fun plus(other: InfInt): InfInt {
        if (this >= other) {
            return this.sum(other)
        } else {
            return other.sum(this)
        }
    }

    public operator fun compareTo(other: InfInt): Int {
        if (this.level > other.level) {
            return 1
        } else if (this.level < other.level) {
            return -1
        }
        if (this.value > other.value) {
            return 1
        } else if (this.value > other.value) {
            return -1
        }
        return 0
    }
}
