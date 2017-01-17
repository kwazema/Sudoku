package com.github.matiuri.sudoku.game

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle
import com.github.matiuri.sudoku.Game
import com.github.matiuri.sudoku.Game.Static.rx
import com.github.matiuri.sudoku.Game.Static.ry
import com.github.matiuri.sudoku.game.Tools.generate
import com.github.matiuri.sudoku.screens.NewGameScreen.Difficulty
import mati.advancedgdx.AdvancedGame.Static.log
import mati.advancedgdx.utils.addListener1
import mati.advancedgdx.utils.createButton
import mati.advancedgdx.utils.createLabel
import mati.advancedgdx.utils.createNPD
import kotlin.properties.Delegates

class Board(private val game: Game, spx: Float, spy: Float, wh: Float, pad: Float, difficulty: Difficulty, stage: Stage)
    : Group() {
    private val blocks: Array<Array<Block>>
    private val cells: Array<Array<Cell>>
    private val generator: Thread
    private var win: Dialog by Delegates.notNull<Dialog>()
    private val generating: Dialog = Dialog("Generating", WindowStyle(game.astManager["UbuntuMB32R", BitmapFont::class],
            Color.WHITE, createNPD(game.astManager["buttonUp", Texture::class], 8))
    )
    var generated: Boolean = false

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

        generator = Thread(Runnable {
            generating.show(stage)
            Thread.sleep(500)
            var done: Boolean
            var countg: Int = 1
            var counts: Int = 1
            do {
                try {
                    generate(cells)
                    Tools.remove(cells, difficulty.n)
                    cells.forEach {
                        it.forEach {
                            it.usrnum = 0
                            it.solverPossibilities.clear()
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
        }, "Generator")
        generator.start()

        win = Dialog("Congratulations!", Window.WindowStyle(game.astManager["UbuntuB32Y", BitmapFont::class],
                Color.WHITE, createNPD(game.astManager["buttonUp", Texture::class], 8))
        )
        win.color = Color.GREEN
        win.text(createLabel("You've complete this Sudoku", game.astManager["UbuntuR16K", BitmapFont::class]))

        val exit: TextButton = createButton("Exit", game.astManager["UbuntuR16K", BitmapFont::class],
                createNPD(game.astManager["buttonUp", Texture::class], 8),
                createNPD(game.astManager["buttonDown", Texture::class], 8),
                createNPD(game.astManager["buttonDown", Texture::class], 8)
        )
        exit.color = Color.RED
        exit.addListener1 { e, a ->
            game.scrManager.change("title")
        }
        win.button(exit)
        win.background.minWidth = 200f * rx
        win.background.minHeight = 200f * ry
        win.buttonTable.cells.forEach { it.expandX().fillX() }
        generating.color = Color(.5f, 0f, 0f, 1f)
    }

    override fun act(delta: Float) {
        if (!generator.isAlive && !generated) {
            generated = true
            generating.hide()
            cells.forEach {
                it.filter(Cell::hidden).forEach {
                    if (game.cellListener != null)
                        it.addListener(game.cellListener.java.constructors[0].newInstance(it) as InputListener)
                }
            }
        }
    }

    fun check() {
        if (generated && cells.fold(true) { f_, c_ ->
            f_ && c_.filter(Cell::hidden).fold(true) { f, c ->
                f && c.number == c.usrnum
            }
        }) win.show(stage)
    }
}
