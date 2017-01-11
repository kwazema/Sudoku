package com.github.matiuri.sudoku.game

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Actor
import com.github.matiuri.sudoku.Game

class Cell(private val game: Game, x: Float, y: Float, wh: Float, val col: Int, val row: Int, val block: Int) :
        Actor() {
    companion object Static {
        var counter: Int = 1
    }

    var num: Int = 0
    @Volatile
    var number: Int = 0
    @Volatile
    var hidden: Boolean = false
    @Volatile
    var usrnum: Int = 0

    init {
        setBounds(x, y, wh, wh)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (!hidden) batch?.color = Color.GRAY
        batch?.draw(game.astManager["cell", Texture::class], x, y, width, height)
        if (!hidden) batch?.color = Color.WHITE
        if (number != 0 && !hidden)
            game.astManager["UbuntuMB32W", BitmapFont::class].draw(batch, "$number", x + 8f, top - 8f)
        if (usrnum != 0 && hidden)
            game.astManager["UbuntuMB32B", BitmapFont::class].draw(batch, "$usrnum", x + 8f, top - 8f)
    }
}
