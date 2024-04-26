package com.example.myapplication.model

import kotlinx.serialization.Serializable

@Serializable
class DoubleGameModel public constructor(
    private val _game1: GameModel,
    private val _game2: GameModel
) : IModel {

    public constructor(size: Int) : this(GameModel(size), GameModel(size))

    public val game1: GameModel
        get() = _game1

    public val game2: GameModel
        get() = _game2

    public override val isGaming: Boolean
        get() = _game1.isGaming && _game2.isGaming

    public override var score: InfInt = InfInt()
        get() = _game1.score + _game2.score

    public override val size: Int
        get() = _game1.size

    public override fun move(t: MoveType) {
        if (isGaming) {
            _game1.move(t)
            _game2.move(t)
        }
    }

    public override fun get(row: Int, col: Int): InfInt {
        // Игровые поля соединены горизонтально
        if (col < size) {
            return _game1.get(row, col)
        } else {
            return _game2.get(row, col - size)
        }
    }
}
