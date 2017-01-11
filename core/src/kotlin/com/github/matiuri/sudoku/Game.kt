package com.github.matiuri.sudoku

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.github.matiuri.sudoku.screens.GameScreen
import mati.advancedgdx.AdvancedGame
import mati.advancedgdx.assets.AssetLoader
import mati.advancedgdx.utils.glClearColor
import kotlin.properties.Delegates

class Game : AdvancedGame() {
    override fun create() {
        super.create()
        init(this)
        glClearColor(Color.BLACK)
        prepare()
    }

    private fun prepare() {
        scrManager
                .add("game", GameScreen(this))
        astManager
                .queue("cell", "Textures/Cell.png", Texture::class)
                .load {
                    scrManager.loadAll()
                    scrManager.change("game")
                }
    }

    override fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        super.render()
    }
}
