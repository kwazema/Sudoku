package com.github.matiuri.sudoku.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.github.matiuri.sudoku.Game
import com.github.matiuri.sudoku.desktop.input.CellInputListener

object DesktopLauncher {
    @JvmStatic fun main(arg: Array<String>) {
        val cfg = LwjglApplicationConfiguration()
        cfg.width = 360
        cfg.height = 640
        cfg.resizable = false
        cfg.title = "Sudoku"
        LwjglApplication(Game(CellInputListener::class), cfg)
    }
}
