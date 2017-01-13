package com.github.matiuri.sudoku.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align.center
import com.badlogic.gdx.utils.viewport.FitViewport
import com.github.matiuri.sudoku.Game
import mati.advancedgdx.screens.Screen
import mati.advancedgdx.utils.addListener1
import mati.advancedgdx.utils.createButton
import mati.advancedgdx.utils.createLabel
import mati.advancedgdx.utils.createNPD
import kotlin.properties.Delegates

class TitleScreen(game: Game) : Screen<Game>(game) {
    private var stage: Stage by Delegates.notNull<Stage>()

    override fun show() {
        stage = Stage(FitViewport(360f, 640f))
        val table: Table = Table()
        stage.addActor(table)
        table.setFillParent(true)
        table.pad(10f)
        //table.debug = true

        //Tile
        table.add(createLabel("Sudoku", game.astManager["UbuntuB64Y", BitmapFont::class])).colspan(3).expandX()
                .align(center)
        table.row()

        //New Game
        val newGame: TextButton = createButton("New Game", game.astManager["UbuntuR16K", BitmapFont::class],
                createNPD(game.astManager["buttonUp", Texture::class], 8),
                createNPD(game.astManager["buttonDown", Texture::class], 8),
                createNPD(game.astManager["buttonDown", Texture::class], 8)
        )
        newGame.addListener1 { e, a ->
            game.scrManager.change("new")
        }
        table.add(newGame).expandX().fillX().pad(5f)
        newGame.color = Color.BLUE

        //Continue
        val continueB: TextButton = createButton("Continue: NYI", game.astManager["UbuntuR16K", BitmapFont::class],
                createNPD(game.astManager["buttonLocked", Texture::class], 8),
                createNPD(game.astManager["buttonLocked", Texture::class], 8),
                createNPD(game.astManager["buttonLocked", Texture::class], 8)
        )
        //TODO: Save current puzzle and allow to resume
        table.add(continueB).expandX().fillX().pad(5f)
        continueB.color = Color.GRAY

        //Exit
        val exit: TextButton = createButton("Exit", game.astManager["UbuntuR16K", BitmapFont::class],
                createNPD(game.astManager["buttonUp", Texture::class], 8),
                createNPD(game.astManager["buttonDown", Texture::class], 8),
                createNPD(game.astManager["buttonDown", Texture::class], 8)
        )
        exit.addListener1 { e, a ->
            //TODO: Double-ask to exit
            Gdx.app.exit()
        }
        table.add(exit).expandX().fillX().pad(5f)
        exit.color = Color.RED

        //Input
        Gdx.input.inputProcessor = stage
    }

    override fun render(delta: Float) {
        stage.act(delta)
        stage.draw()
    }

    override fun hide() {
        stage.dispose()
    }
}
