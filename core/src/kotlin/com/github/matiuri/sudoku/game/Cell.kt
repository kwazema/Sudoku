package com.github.matiuri.sudoku.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.github.matiuri.sudoku.Game

class Cell(private val game: Game, x: Float, y: Float, wh: Float) : Actor() {
    init {
        setBounds(x, y, wh, wh)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch?.draw(game.astManager["cell", Texture::class], x, y, width, height)
    }
}
