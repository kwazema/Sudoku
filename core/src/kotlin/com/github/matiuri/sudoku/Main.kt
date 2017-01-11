package com.github.matiuri.sudoku

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import mati.advancedgdx.AdvancedGame
import mati.advancedgdx.utils.glClearColor

class Main : AdvancedGame() {
    override fun create() {
        super.create()
        init(this)
        glClearColor(Color.BLACK)
    }

    override fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        super.render()
    }
}
