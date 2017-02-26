package com.github.matiuri.sudoku

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.github.matiuri.sudoku.screens.GameScreen
import com.github.matiuri.sudoku.screens.LoadingScreen
import com.github.matiuri.sudoku.screens.NewGameScreen
import com.github.matiuri.sudoku.screens.TitleScreen
import mati.advancedgdx.AdvancedGame
import mati.advancedgdx.assets.FontLoader.FontLoaderParameter
import mati.advancedgdx.utils.glClearColor
import java.lang.Math.sqrt
import java.util.*
import kotlin.properties.Delegates
import kotlin.reflect.KClass

class Game(val cellListener: KClass<out InputListener>? = null,
           val specificCode: List<Pair<String, (List<Any>) -> Unit>> = ArrayList())
    : AdvancedGame() {
    companion object Static {
        var rx: Float = 0f
        var ry: Float = 0f
    }

    var background: Image by Delegates.notNull<Image>()
    private var lastFPS: Int = 0

    override fun create() {
        super.create()
        Gdx.app.logLevel = LOG_DEBUG
        init(this)
        Gdx.input.isCatchBackKey = true
        Gdx.input.isCatchMenuKey = true
        glClearColor(Color.BLACK)
        rx = Gdx.graphics.width.toFloat() / 360f
        ry = Gdx.graphics.height.toFloat() / 640f
        log.d(this.javaClass.simpleName, "rx = $rx | ry = $ry")
        astManager.screen = LoadingScreen::class.java
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
                .queue("background", "Textures/Background.png", Texture::class)
                //Fonts
                .queue("UbuntuMB32B", "UbuntuMB32B", BitmapFont::class,
                        FontLoaderParameter(astManager["UbuntuMono-B"]) {
                            it.color = Color.BLUE
                            it.size = 32 * (sqrt((rx * ry).toDouble())).toInt()
                            it.borderColor = Color.YELLOW
                            it.borderWidth = 2f * (sqrt((rx * ry).toDouble())).toFloat()
                        })
                .queue("UbuntuMB32W", "UbuntuMB32W", BitmapFont::class,
                        FontLoaderParameter(astManager["UbuntuMono-B"]) {
                            it.color = Color.WHITE
                            it.size = 32 * (sqrt((rx * ry).toDouble())).toInt()
                            it.borderColor = Color.BLACK
                            it.borderWidth = 2f * (sqrt((rx * ry).toDouble())).toFloat()
                        })
                .queue("UbuntuMB32K", "UbuntuMB32K", BitmapFont::class,
                        FontLoaderParameter(astManager["UbuntuMono-B"]) {
                            it.color = Color.BLACK
                            it.size = 32 * (sqrt((rx * ry).toDouble())).toInt()
                            it.borderColor = Color.WHITE
                            it.borderWidth = 2f * (sqrt((rx * ry).toDouble())).toFloat()
                        })
                .queue("UbuntuMB32R", "UbuntuMB32R", BitmapFont::class,
                        FontLoaderParameter(astManager["UbuntuMono-B"]) {
                            it.color = Color.RED
                            it.size = 32 * (sqrt((rx * ry).toDouble())).toInt()
                            it.borderColor = Color.BLACK
                            it.borderWidth = 2f * (sqrt((rx * ry).toDouble())).toFloat()
                        })
                .queue("UbuntuB64Y", "UbuntuB64Y", BitmapFont::class,
                        FontLoaderParameter(astManager["Ubuntu-B"]) {
                            it.color = Color.YELLOW
                            it.size = 64 * (sqrt((rx * ry).toDouble())).toInt()
                            it.borderColor = Color.BLACK
                            it.borderWidth = 2f * (sqrt((rx * ry).toDouble())).toFloat()
                            if (it.size > 128) {
                                it.size = 128
                                it.borderWidth = 4f
                            }
                        })
                .queue("UbuntuB32Y", "UbuntuB32Y", BitmapFont::class,
                        FontLoaderParameter(astManager["Ubuntu-B"]) {
                            it.color = Color.YELLOW
                            it.size = 32 * (sqrt((rx * ry).toDouble())).toInt()
                            it.borderColor = Color.BLACK
                            it.borderWidth = 2f * (sqrt((rx * ry).toDouble())).toFloat()
                            if (it.size > 100) {
                                it.size = 100
                                it.borderWidth = 4f
                            }
                        })
                .queue("UbuntuR16K", "UbuntuR16K", BitmapFont::class,
                        FontLoaderParameter(astManager["Ubuntu-B"]) {
                            it.color = Color.BLACK
                            it.size = 16 * (sqrt((rx * ry).toDouble())).toInt()
                            it.borderColor = Color.WHITE
                            it.borderWidth = 2f * (sqrt((rx * ry).toDouble())).toFloat()
                        })
                //Load Assets
                .load {
                    //Dispose font generators, which are useless since this time
                    astManager.remove("Ubuntu-R").remove("Ubuntu-B").remove("UbuntuMono-R").remove("UbuntuMono-B")

                    background = Image(astManager["background", Texture::class])
                    background.setFillParent(true)
                    background.color = Color.GOLDENROD

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
