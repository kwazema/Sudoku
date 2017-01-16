package com.github.matiuri.sudoku.input

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.github.matiuri.sudoku.game.Cell
import com.github.matiuri.sudoku.game.Cell.Mode
import com.github.matiuri.sudoku.game.Cell.Mode.NONE

class CellInputListener(val cell: Cell) : InputListener() {
    override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
        if (!cell.hidden) return false
        if (Cell.active != cell)
            Cell.active?.setMode(-1)
        cell.mode = Mode.values()[(cell.mode.ordinal + 1) % Mode.values().size]
        if (cell.mode != NONE)
            Cell.active = cell
        else Cell.active = null
        return true
    }
}
