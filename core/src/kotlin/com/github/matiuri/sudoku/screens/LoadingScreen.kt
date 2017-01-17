package com.github.matiuri.sudoku.screens

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.github.matiuri.sudoku.Game
import com.github.matiuri.sudoku.Game.Static.rx
import com.github.matiuri.sudoku.Game.Static.ry
import mati.advancedgdx.assets.LoadingScreen
import java.lang.Math.sqrt
import kotlin.properties.Delegates

class LoadingScreen(game: Game, manager: AssetManager, after: () -> Unit) : LoadingScreen(game, manager, after) {
    private var sb: SpriteBatch by Delegates.notNull<SpriteBatch>()
    private var font: BitmapFont by Delegates.notNull<BitmapFont>()

    override fun show() {
        super.show()
        sb = SpriteBatch()
        font = BitmapFont()
        font.data.scale(sqrt((rx * ry).toDouble() - 1).toFloat())
    }

    override fun render(delta: Float) {
        sb.begin()
        if (!manager.update()) font.draw(sb, "Loading: ${MathUtils.round(manager.progress * 100)}%", 10f * rx, 20f * ry)
        else {
            font.draw(sb, "Loaded", 10f * rx, 20f * ry)
            after()
        }
        sb.end()
    }

    override fun hide() {
        super.hide()
        sb.dispose()
        font.dispose()
    }
}
