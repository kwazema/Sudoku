package com.github.matiuri.sudoku.game

import com.badlogic.gdx.scenes.scene2d.Group
import com.github.matiuri.sudoku.Game
import com.github.matiuri.sudoku.game.Tools.generate
import mati.advancedgdx.AdvancedGame.Static.log

class Board(game: Game, spx: Float, spy: Float, wh: Float, pad: Float) : Group() {
    private val blocks: Array<Array<Block>>
    private val cells: Array<Array<Cell>>

    init {
        blocks = Array(3) { x ->
            Array(3) { y ->
                Block(game, x * wh * (3 + pad) + spx, y * wh * (3 + pad) + spy, wh, x + x * 2 + 1, y + y * 2 + 1)
            }
        }
        blocks.forEach { it.forEach { addActor(it) } }
        cells = Array(9) { x ->
            Array(9) { y ->
                blocks[x / 3][y / 3].cells[x - 3 * (x / 3)][y - 3 * (y / 3)]
            }
        }

        cells.forEach {
            it.forEach {
                it.num = Cell.counter
                Cell.counter++
            }
        }

        Thread(Runnable {
            Thread.sleep(500)
            var done: Boolean
            var countg: Int = 1
            var counts: Int = 1
            do {
                try {
                    generate(cells)
                    Tools.remove(cells, 56) //MAX = 56
                    cells.forEach {
                        it.forEach {
                            it.usrnum = 0
                            it.possibilities.clear()
                        }
                    }
                    done = true
                } catch (e: IllegalStateException) {
                    done = false
                    if (e.message?.contains("Generator") ?: false) countg++
                    else counts++
                }
            } while (!done)
            log.d(this.javaClass.simpleName, "$countg | $counts | ${countg + counts}")
            log.d(Thread.currentThread().name, "Dead!")
        }, "Generator").start()
    }
}
