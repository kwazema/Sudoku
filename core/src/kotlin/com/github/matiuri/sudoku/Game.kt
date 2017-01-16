package com.github.matiuri.sudoku

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.github.matiuri.sudoku.screens.GameScreen
import com.github.matiuri.sudoku.screens.NewGameScreen
import com.github.matiuri.sudoku.screens.TitleScreen
import mati.advancedgdx.AdvancedGame
import mati.advancedgdx.assets.FontLoader.FontLoaderParameter
import mati.advancedgdx.utils.glClearColor
import kotlin.reflect.KClass

class Game(val cellListener: KClass<out InputListener>? = null) : AdvancedGame() {
    private var lastFPS: Int = 0

    override fun create() {
        super.create()
        Gdx.app.logLevel = LOG_DEBUG
        init(this)
        glClearColor(Color.BLACK)
        prepare()
    }

    private fun prepare() {
        scrManager
                .add("title", TitleScreen(this))
                .add("new", NewGameScreen(this))
                .add("game", GameScreen(this))

        astManager
                //Font Generators
                .queue("Ubuntu-R", "Fonts/Ubuntu-R.ttf", FreeTypeFontGenerator::class)
                .queue("Ubuntu-B", "Fonts/Ubuntu-B.ttf", FreeTypeFontGenerator::class)
                .queue("UbuntuMono-R", "Fonts/UbuntuMono-R.ttf", FreeTypeFontGenerator::class)
                .queue("UbuntuMono-B", "Fonts/UbuntuMono-B.ttf", FreeTypeFontGenerator::class)
                //Textures
                .queue("cell", "Textures/Cell.png", Texture::class)
                .queue("possibility", "Textures/Possibility.png", Texture::class)
                .queue("buttonUp", "Textures/ButtonUp.png", Texture::class)
                .queue("buttonDown", "Textures/ButtonDown.png", Texture::class)
                .queue("buttonLocked", "Textures/ButtonLocked.png", Texture::class)
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
                .queue("UbuntuMB32K", "UbuntuMB32K", BitmapFont::class,
                        FontLoaderParameter(astManager["UbuntuMono-B"]) {
                            it.color = Color.BLACK
                            it.size = 32
                            it.borderColor = Color.WHITE
                            it.borderWidth = 2f
                        })
                .queue("UbuntuB64Y", "UbuntuB64Y", BitmapFont::class,
                        FontLoaderParameter(astManager["Ubuntu-B"]) {
                            it.color = Color.YELLOW
                            it.size = 64
                            it.borderColor = Color.BLACK
                            it.borderWidth = 2f
                        })
                .queue("UbuntuB32Y", "UbuntuB32Y", BitmapFont::class,
                        FontLoaderParameter(astManager["Ubuntu-B"]) {
                            it.color = Color.YELLOW
                            it.size = 32
                            it.borderColor = Color.BLACK
                            it.borderWidth = 2f
                        })
                .queue("UbuntuR16K", "UbuntuR16K", BitmapFont::class,
                        FontLoaderParameter(astManager["Ubuntu-B"]) {
                            it.color = Color.BLACK
                            it.size = 16
                            it.borderColor = Color.WHITE
                            it.borderWidth = 2f
                        })
                //Load Assets
                .load {
                    //Dispose font generators, which are useless since this time
                    astManager.remove("Ubuntu-R").remove("Ubuntu-B").remove("UbuntuMono-R").remove("UbuntuMono-B")

                    scrManager.loadAll()
                    scrManager.change("title")
                }
    }

    override fun render() {
        if (Gdx.graphics.framesPerSecond != lastFPS) {
            log.l(this.javaClass.simpleName, "FPS: ${Gdx.graphics.framesPerSecond}")
            lastFPS = Gdx.graphics.framesPerSecond
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        super.render()
    }
}
