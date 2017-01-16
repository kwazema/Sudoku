package com.github.matiuri.sudoku.gui

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.github.matiuri.sudoku.Game
import com.github.matiuri.sudoku.Game.Static.rx
import com.github.matiuri.sudoku.Game.Static.ry

class Possibility(game: Game) {
    private val texture: Texture = game.astManager["possibility", Texture::class]
    private val possibilities: Array<Boolean> = Array(9) { false }

    fun switch(n: Int) {
        if (n != 0)
            possibilities[n - 1] = !possibilities[n - 1]
    }

    fun draw(batch: Batch?, X: Float, Y: Float, WH: Float) {
        val pairedPossibilities: Array<Pair<Int, Boolean>> = Array(9) { Pair(it + 1, possibilities[it]) }
        pairedPossibilities.filter(Pair<Int, Boolean>::second).forEach {
            val n: Int = it.first

            val x: Float = X + 1f * rx + ((n - 1) % 3) * (WH / 3f)
            val y: Float = Y - 9f * ry - ((n - 1) / 3) * (WH / 3f)
            batch?.draw(texture, x, y, 8f * rx, 8f * ry)
        }
    }
}
