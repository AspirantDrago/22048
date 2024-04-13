package com.example.myapplication.model

interface IModel {
    public var score: InfInt
    public val size: Int
    public val isGaming: Boolean

    public fun get(row: Int, col: Int): InfInt
    public fun move(t: MoveType)
}