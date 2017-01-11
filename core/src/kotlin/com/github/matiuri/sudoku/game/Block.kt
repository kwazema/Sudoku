package com.github.matiuri.sudoku.game

import com.badlogic.gdx.scenes.scene2d.Group
import com.github.matiuri.sudoku.Game
import kotlin.properties.Delegates

class Block(game: Game, xb: Float, yb: Float, wh: Float) : Group() {
    private var cells: Array<Array<Cell>> by Delegates.notNull<Array<Array<Cell>>>()

    init {
        cells = Array(3) { x ->
            Array(3) { y ->
                Cell(game, x * wh + xb, y * wh + yb, wh)
            }
        }
        cells.forEach { it.forEach { addActor(it) } }
    }
}
