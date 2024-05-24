package com.example.myapplication.model

import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
class InfIntColored: InfInt {
    private var _color: Int = 0
    public val color: Int
        get() = _color

    public constructor(): super() {}

    public constructor(other: Int): super(other) {
        calculateColor()
    }

    public constructor(other: InfInt): super(other) {
        calculateColor()
    }

    private fun chechColor(r: Int, g: Int, b: Int): Boolean {
        val s = r + g + b
        if (s < 255)
            return false
        if (s > 255 * 2)
            return false
        return true
    }

    private fun calculateColor() {
        val random = Random(this.value  + this.level.shl(16))
        var r: Int
        var g: Int
        var b: Int
        do {
            r = random.nextInt(256)
            g = random.nextInt(256)
            b = random.nextInt(256)
        } while (!chechColor(r, g, b))
        _color = (r shl 16) + (g shl 8) + b
    }
}