package com.github.matiuri.sudoku.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.github.matiuri.sudoku.Game
import com.github.matiuri.sudoku.Game.Static.rx
import com.github.matiuri.sudoku.Game.Static.ry
import com.github.matiuri.sudoku.game.Block
import com.github.matiuri.sudoku.game.Board
import com.github.matiuri.sudoku.game.Cell
import com.github.matiuri.sudoku.screens.NewGameScreen.Difficulty
import mati.advancedgdx.screens.Screen
import mati.advancedgdx.utils.addListener1
import mati.advancedgdx.utils.createButton
import mati.advancedgdx.utils.createNPD
import java.lang.Math.sqrt
import kotlin.properties.Delegates

class GameScreen(game: Game) : Screen<Game>(game) {
    private var stage: Stage by Delegates.notNull<Stage>()
    private val time: Float = 5f
    private var timer: Float = 0f
    private var checked: Boolean = false
    private var exit: TextButton by Delegates.notNull<TextButton>()
    var difficulty: Difficulty = Difficulty.PATHETIC

    override fun show() {
        stage = Stage(ScreenViewport())
        Block.count = 1
        Cell.counter = 1
        val wh = 32f
        val pad = .1f
        val size = wh * 9 + 4 * pad
        val spx = stage.width / 2f - (size / 2f) * rx
        val spy = stage.height / 2f - (size / 2f) * ry
        stage.addActor(Board(game, spx, spy, wh * sqrt((rx * ry).toDouble()).toFloat(), pad, difficulty, stage))

        val pause: Dialog = Dialog("Paused", WindowStyle(game.astManager["UbuntuB64Y", BitmapFont::class],
                Color.WHITE, createNPD(game.astManager["buttonUp", Texture::class], 8))
        )

        exit = createButton("Exit", game.astManager["UbuntuR16K", BitmapFont::class],
                createNPD(game.astManager["buttonUp", Texture::class], 8),
                createNPD(game.astManager["buttonDown", Texture::class], 8),
                createNPD(game.astManager["buttonDown", Texture::class], 8)
        )
        exit.color = Color.RED
        exit.addListener1 { e, a ->
            if (checked) {
                checked = false
                game.scrManager.change("title")
            } else {
                checked = true
                exit.color = Color(0.5f, 0f, 0f, 1f)
            }
        }

        val continueGame: TextButton = createButton("Continue", game.astManager["UbuntuR16K", BitmapFont::class],
                createNPD(game.astManager["buttonUp", Texture::class], 8),
                createNPD(game.astManager["buttonDown", Texture::class], 8),
                createNPD(game.astManager["buttonDown", Texture::class], 8)
        )
        continueGame.color = Color.BLUE
        continueGame.addListener1 { e, a ->
            pause.hide()
            exit.color = Color.RED
            checked = false
        }

        val pauseButton: TextButton = createButton("P", game.astManager["UbuntuMB32K", BitmapFont::class],
                createNPD(game.astManager["buttonUp", Texture::class], 8),
                createNPD(game.astManager["buttonDown", Texture::class], 8),
                createNPD(game.astManager["buttonDown", Texture::class], 8)
        )
        pauseButton.addListener1 { e, a ->
            pause.show(stage)
        }
        pauseButton.setBounds(10f * rx, stage.height - 42f * ry, 32f * rx, 32f * ry)
        pauseButton.color = Color.RED
        stage.addActor(pauseButton)

        pause.button(exit)
        pause.button(continueGame)
        pause.color = Color.BLUE
        pause.background.minWidth = stage.width - 50f
        pause.background.minHeight = stage.height - 100f
        pause.buttonTable.cells.forEach { it.expandX().fillX() }
        Gdx.input.inputProcessor = stage

        game.specificCode.filter { it.first == "gameButtons" }.forEach {
            it.second(arrayListOf(5f, 10f, game, stage))
        }
    }

    override fun render(delta: Float) {
        if (checked) {
            timer += delta
            if (timer > time) {
                timer = 0f
                checked = false
                exit.color = Color.RED
            }
        }

        stage.act(delta)
        stage.draw()
    }

    override fun hide() {
        checked = false
        stage.dispose()
    }
}
