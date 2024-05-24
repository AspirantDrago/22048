package com.example.myapplication.model

import kotlinx.serialization.Serializable

@Serializable
class GameModel public constructor(
    protected var _score: InfInt,
    protected var _board: MutableList<MutableList<InfIntColored>>,
    protected var _size: Int,
    protected var _isGaming: Boolean,
    protected var _lastMove: MoveType
) : IModel {
    protected val EMPTY_CELL = InfIntColored()

    public constructor(size: Int) : this(InfInt(), MutableList(size) {
        MutableList(size) { InfIntColored() }
    }, size, true, MoveType.NONE) {
        addNewNumber()
    }

    public override var score: InfInt
        get() = _score
        set(value) { _score = value }

    public override val size: Int
        get() = _size

    public override val isGaming: Boolean
        get() = _isGaming

    public override fun get(row: Int, col: Int): InfInt {
        return _board[row][col]
    }

    private fun addNewNumber(): Boolean {
        val emptyCells: MutableList<Pair<Int, Int>> = mutableListOf<Pair<Int, Int>>()
        for (row in 0 until _size) {
            for (col in 0 until _size) {
                if (_board[row][col] == EMPTY_CELL) {
                    emptyCells.add(Pair(row, col))
                }
            }
        }
        if (emptyCells.isEmpty()) {
            return false
        }
        val Cell = emptyCells.random()
        _board[Cell.first][Cell.second] = InfIntColored(1)
        return true
    }

    private fun check(): Boolean {
        for (row in 0 until _size) {
            for (col in 0 until _size) {
                if (_board[row][col].isEmpty()) {
                    return true
                }
                if (col > 0) {
                    if (_board[row][col] == _board[row][col - 1]) {
                        return true
                    }
                }
                if (row > 0) {
                    if (_board[row][col] == _board[row - 1][col]) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun _move(drow: Int = 0, dcol: Int = 0): Unit {
        var isStable: Boolean
        do {
            isStable = true
            for (col in 0 until _size) {
                for (row in 0 until _size) {
                    var row2 = row + drow
                    var col2 = col + dcol
                    if (row2 < 0 || row2 >= _size || col2 < 0 || col2 >= _size) {
                        continue
                    }
                    if (!_board[row][col].isEmpty()) {
                        if (_board[row2][col2].isEmpty()) {
                            // Перемещение числа на пустое место
                            _board[row2][col2] = _board[row][col]
                            _board[row][col] = EMPTY_CELL
                            isStable = false
                        } else if (_board[row2][col2] == _board[row][col]) {
                            // Сложение соседних одинаковых чисел
                            _board[row2][col2].duplicate()
                            score += _board[row2][col2]
                            _board[row][col] = EMPTY_CELL
                            isStable = false
                        }
                    }
                }
            }
        } while (!isStable)
    }

    public override fun move(t: MoveType) {
        _lastMove = t
        when (t) {
            MoveType.UP -> _move(-1, 0)
            MoveType.DOWN -> _move(1, 0)
            MoveType.LEFT -> _move(0, -1)
            MoveType.RIGHT -> _move(0, 1)
            MoveType.NONE -> return
        }
        if (!addNewNumber()) {
            if (!check()) {
                _isGaming = false
            }
        }
    }
}
