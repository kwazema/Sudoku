package com.github.matiuri.sudoku

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.github.matiuri.sudoku.screens.GameScreen
import mati.advancedgdx.AdvancedGame
import mati.advancedgdx.assets.FontLoader.FontLoaderParameter
import mati.advancedgdx.utils.glClearColor

class Game : AdvancedGame() {
    override fun create() {
        super.create()
        Gdx.app.logLevel = LOG_DEBUG
        init(this)
        glClearColor(Color.BLACK)
        prepare()
    }

    private fun prepare() {
        scrManager
                .add("game", GameScreen(this))

        astManager
                //Font Generators
                .queue("Ubuntu-R", "Fonts/Ubuntu-R.ttf", FreeTypeFontGenerator::class)
                .queue("Ubuntu-B", "Fonts/Ubuntu-B.ttf", FreeTypeFontGenerator::class)
                .queue("UbuntuMono-R", "Fonts/UbuntuMono-R.ttf", FreeTypeFontGenerator::class)
                .queue("UbuntuMono-B", "Fonts/UbuntuMono-B.ttf", FreeTypeFontGenerator::class)
                //Textures
                .queue("cell", "Textures/Cell.png", Texture::class)
                //Fonts
                .queue("UbuntuMB32B", "UbuntuMB32B", BitmapFont::class,
                        FontLoaderParameter(astManager["UbuntuMono-B"]) {
                            it.color = Color.BLUE
                            it.size = 32
                            it.borderColor = Color.YELLOW
                            it.borderWidth = 2f
                        })
                .queue("UbuntuMB32W", "UbuntuMB32W", BitmapFont::class,
                        FontLoaderParameter(astManager["UbuntuMono-B"]) {
                            it.color = Color.WHITE
                            it.size = 32
                            it.borderColor = Color.BLACK
                            it.borderWidth = 2f
                        })
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
