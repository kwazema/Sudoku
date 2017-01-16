package com.github.matiuri.sudoku

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.github.matiuri.sudoku.game.Cell
import com.github.matiuri.sudoku.input.CellInputListener
import mati.advancedgdx.utils.addListener1
import mati.advancedgdx.utils.createButton

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
                        TextureRegionDrawable(TextureRegion(game.astManager["cell", Texture::class])),
                        TextureRegionDrawable(TextureRegion(game.astManager["cell", Texture::class]))
                )
                button.addListener1 { e, a ->
                    when (Cell.active?.mode ?: Cell.Mode.NONE) {
                        Cell.Mode.INSERT -> Cell.active?.usrnum = n
                        Cell.Mode.POSSIBILITIES -> Cell.active?.switchPossibility(n)
                        Cell.Mode.NONE -> {
                            //Nothing
                        }
                    }
                }
                button.setBounds(x + 35f * n, y, 32f, 32f)
                stage.addActor(button)
            }
        }
        initialize(Game(CellInputListener::class, arrayListOf(Pair("gameButtons", gameButtons))), config)
    }
}
