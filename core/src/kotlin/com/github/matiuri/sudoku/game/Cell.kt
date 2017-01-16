package com.github.matiuri.sudoku.game

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Actor
import com.github.matiuri.sudoku.Game
import com.github.matiuri.sudoku.gui.Possibility
import java.util.*

class Cell(private val game: Game, x: Float, y: Float, wh: Float, val col: Int, val row: Int, val block: Int) :
        Actor() {
    companion object Static {
        var counter: Int = 1
        var active: Cell? = null
    }

    var num: Int = 0
    var mode: Mode = Mode.NONE
    private val possibilities: Possibility = Possibility(game)

    @Volatile
    var number: Int = 0
    @Volatile
    var hidden: Boolean = false
    @Volatile
    var usrnum: Int = 0
    @Volatile
    var solverPossibilities: MutableList<Int> = ArrayList() //For automatic solver

    init {
        setBounds(x, y, wh, wh)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (!hidden) batch?.color = Color.GRAY
        else batch?.color = mode.color
        batch?.draw(game.astManager["cell", Texture::class], x, y, width, height)
        if ((((parent as Block).parent) as Board).generated) {
            if (number != 0 && !hidden)
                game.astManager["UbuntuMB32W", BitmapFont::class].draw(batch, "$number", x + 8f, top - 8f)
            if (usrnum != 0 && hidden)
                game.astManager["UbuntuMB32B", BitmapFont::class].draw(batch, "$usrnum", x + 8f, top - 8f)
            batch?.color = Color.WHITE
            if (usrnum == 0 && hidden) possibilities.draw(batch, x, top, width)
        }
    }

    enum class Mode(val color: Color) {
        NONE(Color.WHITE), INSERT(Color.BLUE), POSSIBILITIES(Color.RED)
    }

    fun setMode(i: Int) {
        when (i) {
            0 -> switch(Mode.INSERT)
            1 -> switch(Mode.POSSIBILITIES)
            else -> switch(Mode.NONE)
        }
    }

    private fun switch(newMode: Mode) {
        if (mode == newMode || (newMode == Mode.POSSIBILITIES && usrnum != 0)) mode = Mode.NONE
        else mode = newMode
        if (mode != Mode.NONE) {
            active = this
            stage.keyboardFocus = this
        } else {
            active = null
            stage.keyboardFocus = null
        }
    }

    fun switchPossibility(n: Int) {
        possibilities.switch(n)
    }
}
