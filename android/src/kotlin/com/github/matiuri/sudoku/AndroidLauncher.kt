package com.github.matiuri.sudoku

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.github.matiuri.sudoku.Game.Static.rx
import com.github.matiuri.sudoku.Game.Static.ry
import com.github.matiuri.sudoku.game.Block
import com.github.matiuri.sudoku.game.Board
import com.github.matiuri.sudoku.game.Cell
import com.github.matiuri.sudoku.input.CellInputListener
import mati.advancedgdx.utils.addListener1
import mati.advancedgdx.utils.createButton
import mati.advancedgdx.utils.createNPD

class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()
        config.useGyroscope = false
        config.useAccelerometer = false
        config.useCompass = false
        config.useImmersiveMode = true
        config.useWakelock = true
        config.hideStatusBar = true

        val gameButtons: (List<Any>) -> Unit = { l ->
            //Arguments: x: Float, y: Float, game: Game, stage: Stage
            val x: Float = l[0] as Float
            val y: Float = l[1] as Float
            val game: Game = l[2] as Game
            val stage: Stage = l[3] as Stage

            (0..9).forEach { n ->
                val button: TextButton = createButton(if (n != 0) "$n" else "-",
                        game.astManager["UbuntuMB32K", BitmapFont::class],
                        createNPD(game.astManager["buttonUp", Texture::class], 8),
                        createNPD(game.astManager["buttonDown", Texture::class], 8)
                )
                button.color = Color(n / 9f, 1f - n / 9f, 1f / (n + 1f), 1f)
                button.addListener1 { e, a ->
                    when (Cell.active?.mode ?: Cell.Mode.NONE) {
                        Cell.Mode.INSERT -> {
                            Cell.active?.usrnum = n
                            (((Cell.active?.parent) as Block).parent as Board).check()
                        }
                        Cell.Mode.POSSIBILITIES -> Cell.active?.switchPossibility(n)
                        Cell.Mode.NONE -> {
                            //Nothing
                        }
                    }
                }
                button.setBounds((x + 35f * n) * rx, y * ry, 32f * rx, 32f * ry)
                stage.addActor(button)
            }
        }
        initialize(Game(CellInputListener::class, arrayListOf(Pair("gameButtons", gameButtons))), config)
    }
}
