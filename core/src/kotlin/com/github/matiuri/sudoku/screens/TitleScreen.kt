package com.github.matiuri.sudoku.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align.center
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.github.matiuri.sudoku.Game
import com.github.matiuri.sudoku.Game.Static.rx
import com.github.matiuri.sudoku.Game.Static.ry
import mati.advancedgdx.screens.Screen
import mati.advancedgdx.utils.addListener1
import mati.advancedgdx.utils.createButton
import mati.advancedgdx.utils.createLabel
import mati.advancedgdx.utils.createNPD
import kotlin.properties.Delegates

class TitleScreen(game: Game) : Screen<Game>(game) {
    private var stage: Stage by Delegates.notNull<Stage>()
    private var checked: Boolean = false
    private val time: Float = 5f
    private var timer: Float = 0f
    private var exit: TextButton by Delegates.notNull<TextButton>()

    override fun show() {
        stage = Stage(ScreenViewport())
        val table: Table = Table()
        stage.addActor(table)
        table.setFillParent(true)
        table.pad(10f)
        //table.debug = true

        //Tile
        table.add(createLabel("Sudoku", game.astManager["UbuntuB64Y", BitmapFont::class])).colspan(3).expandX()
                .align(center)
        table.row()

        val pad: Float = 5f * rx * ry

        //New Game
        val newGame: TextButton = createButton("New Game", game.astManager["UbuntuR16K", BitmapFont::class],
                createNPD(game.astManager["buttonUp", Texture::class], 8),
                createNPD(game.astManager["buttonDown", Texture::class], 8),
                createNPD(game.astManager["buttonDown", Texture::class], 8)
        )
        newGame.addListener1 { e, a ->
            game.scrManager.change("new")
        }
        table.add(newGame).expandX().fillX().pad(pad)
        newGame.color = Color.BLUE

        //Continue
        val continueB: TextButton = createButton("Continue: NYI", game.astManager["UbuntuR16K", BitmapFont::class],
                createNPD(game.astManager["buttonLocked", Texture::class], 8),
                createNPD(game.astManager["buttonLocked", Texture::class], 8),
                createNPD(game.astManager["buttonLocked", Texture::class], 8)
        )
        //TODO: Save current puzzle and allow to resume
        table.add(continueB).expandX().fillX().pad(pad)
        continueB.color = Color.GRAY

        //Exit
        exit = createButton("Exit", game.astManager["UbuntuR16K", BitmapFont::class],
                createNPD(game.astManager["buttonUp", Texture::class], 8),
                createNPD(game.astManager["buttonDown", Texture::class], 8),
                createNPD(game.astManager["buttonDown", Texture::class], 8)
        )
        exit.addListener1 { e, a ->
            if (checked) {
                checked = false
                Gdx.app.exit()
            } else {
                checked = true
                exit.color = Color(.5f, 0f, 0f, 1f)
            }
        }
        table.add(exit).expandX().fillX().pad(pad)
        exit.color = Color.RED

        //Input
        Gdx.input.inputProcessor = stage
    }

    override fun render(delta: Float) {
        if (checked) {
            timer += delta
            if (timer > time) {
                checked = false
                timer = 0f
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
