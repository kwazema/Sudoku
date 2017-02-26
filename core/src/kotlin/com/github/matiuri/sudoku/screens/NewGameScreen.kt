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
import com.github.matiuri.sudoku.screens.NewGameScreen.Difficulty.*
import mati.advancedgdx.screens.Screen
import mati.advancedgdx.utils.addListener1
import mati.advancedgdx.utils.createButton
import mati.advancedgdx.utils.createLabel
import mati.advancedgdx.utils.createNPD
import java.lang.Math.sqrt
import kotlin.properties.Delegates

class NewGameScreen(game: Game) : Screen<Game>(game) {
    private var stage: Stage by Delegates.notNull<Stage>()

    override fun show() {
        stage = Stage(ScreenViewport())

        //Background
        stage.addActor(game.background)

        val table: Table = Table()
        stage.addActor(table)
        table.setFillParent(true)

        //Title
        table.add(createLabel("Difficulty", game.astManager["UbuntuB64Y", BitmapFont::class])).expandX().align(center)
        table.row()

        //Pathetic
        val pathetic: TextButton = createButton("Pathetic", game.astManager["UbuntuR16K", BitmapFont::class],
                createNPD(game.astManager["buttonUp", Texture::class], 8),
                createNPD(game.astManager["buttonDown", Texture::class], 8),
                createNPD(game.astManager["buttonDown", Texture::class], 8)
        )
        pathetic.color = Color.LIGHT_GRAY
        pathetic.addListener1 { e, a ->
            (game.scrManager["game"] as GameScreen).difficulty = PATHETIC
            game.scrManager.change("game")
        }
        val pad: Float = 5f * sqrt((rx * ry).toDouble()).toInt()
        table.add(pathetic).expandX().fillX().pad(pad)
        table.row()

        //Easy
        val easy: TextButton = createButton("Easy", game.astManager["UbuntuR16K", BitmapFont::class],
                createNPD(game.astManager["buttonUp", Texture::class], 8),
                createNPD(game.astManager["buttonDown", Texture::class], 8),
                createNPD(game.astManager["buttonDown", Texture::class], 8)
        )
        easy.color = Color.GREEN
        easy.addListener1 { e, a ->
            (game.scrManager["game"] as GameScreen).difficulty = EASY
            game.scrManager.change("game")
        }
        table.add(easy).expandX().fillX().pad(pad)
        table.row()

        //Medium
        val medium: TextButton = createButton("Medium", game.astManager["UbuntuR16K", BitmapFont::class],
                createNPD(game.astManager["buttonUp", Texture::class], 8),
                createNPD(game.astManager["buttonDown", Texture::class], 8),
                createNPD(game.astManager["buttonDown", Texture::class], 8)
        )
        medium.color = Color.YELLOW
        medium.addListener1 { e, a ->
            (game.scrManager["game"] as GameScreen).difficulty = MEDIUM
            game.scrManager.change("game")
        }
        table.add(medium).expandX().fillX().pad(pad)
        table.row()

        //Hard
        val hard: TextButton = createButton("Hard", game.astManager["UbuntuR16K", BitmapFont::class],
                createNPD(game.astManager["buttonUp", Texture::class], 8),
                createNPD(game.astManager["buttonDown", Texture::class], 8),
                createNPD(game.astManager["buttonDown", Texture::class], 8)
        )
        hard.color = Color.RED
        hard.addListener1 { e, a ->
            (game.scrManager["game"] as GameScreen).difficulty = HARD
            game.scrManager.change("game")
        }
        table.add(hard).expandX().fillX().pad(pad)
        table.row()

        //Extreme
        val extreme: TextButton = createButton("Extreme", game.astManager["UbuntuR16K", BitmapFont::class],
                createNPD(game.astManager["buttonUp", Texture::class], 8),
                createNPD(game.astManager["buttonDown", Texture::class], 8),
                createNPD(game.astManager["buttonDown", Texture::class], 8)
        )
        extreme.color = Color.DARK_GRAY
        extreme.addListener1 { e, a ->
            (game.scrManager["game"] as GameScreen).difficulty = EXTREME
            game.scrManager.change("game")
        }
        table.add(extreme).expandX().fillX().pad(pad)
        table.row()

        //Back
        table.add().height(50f)
        table.row()
        val back: TextButton = createButton("Back", game.astManager["UbuntuR16K", BitmapFont::class],
                createNPD(game.astManager["buttonUp", Texture::class], 8),
                createNPD(game.astManager["buttonDown", Texture::class], 8),
                createNPD(game.astManager["buttonDown", Texture::class], 8)
        )
        back.color = Color(.5f, 0f, 0f, 1f)
        back.addListener1 { e, a ->
            game.scrManager.change("title")
        }
        table.add(back).expandX().fillX().pad(pad)

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

    enum class Difficulty(val n: Int) {
        PATHETIC(1), EASY(25), MEDIUM(38), HARD(45), EXTREME(56)
    }
}
