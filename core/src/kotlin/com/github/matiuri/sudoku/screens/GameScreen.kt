package com.github.matiuri.sudoku.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.github.matiuri.sudoku.Game
import com.github.matiuri.sudoku.game.Board
import com.github.matiuri.sudoku.screens.NewGameScreen.Difficulty
import mati.advancedgdx.screens.Screen
import kotlin.properties.Delegates

class GameScreen(game: Game) : Screen<Game>(game) {
    private var stage: Stage by Delegates.notNull<Stage>()
    var difficulty: Difficulty = Difficulty.PATHETIC

    override fun show() {
        stage = Stage(FitViewport(360f, 640f))
        val wh = 32f
        val pad = .1f
        val size = wh * 9 + 4 * pad
        val spx = stage.width / 2f - size / 2f
        val spy = stage.height / 2f - size / 2f
        stage.addActor(Board(game, spx, spy, wh, pad, difficulty))
        Gdx.input.inputProcessor = stage
    }

    override fun render(delta: Float) {
        stage.act(delta)
        stage.draw()
    }

    override fun hide() {
        stage.dispose()
    }
}
