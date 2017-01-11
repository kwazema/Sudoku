package com.github.matiuri.sudoku.game

import com.badlogic.gdx.scenes.scene2d.Group
import com.github.matiuri.sudoku.Game
import kotlin.properties.Delegates

class Board(game: Game, spx: Float, spy: Float, wh: Float, pad: Float) : Group() {
    private var blocks: Array<Array<Block>> by Delegates.notNull<Array<Array<Block>>>()

    init {
        blocks = Array(3) { x ->
            Array(3) { y ->
                Block(game, x * wh * (3 + pad) + spx, y * wh * (3 + pad) + spy, wh)
            }
        }
        blocks.forEach { it.forEach { addActor(it) } }
    }
}
