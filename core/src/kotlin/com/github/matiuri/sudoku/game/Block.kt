package com.github.matiuri.sudoku.game

import com.badlogic.gdx.scenes.scene2d.Group
import com.github.matiuri.sudoku.Game

class Block(game: Game, xb: Float, yb: Float, wh: Float, br: Int, bc: Int) : Group() {
    companion object Static {
        var count: Int = 1
    }

    val cells: Array<Array<Cell>>
    private val num: Int

    init {
        num = count
        count++
        cells = Array(3) { x ->
            Array(3) { y ->
                Cell(game, x * wh + xb, y * wh + yb, wh, x + br, y + bc, num)
            }
        }
        cells.forEach { it.forEach { addActor(it) } }
    }
}
