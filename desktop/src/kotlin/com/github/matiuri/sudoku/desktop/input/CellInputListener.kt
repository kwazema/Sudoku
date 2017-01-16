package com.github.matiuri.sudoku.desktop.input

import com.badlogic.gdx.Input.Keys.*
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.github.matiuri.sudoku.game.Block
import com.github.matiuri.sudoku.game.Board
import com.github.matiuri.sudoku.game.Cell

class CellInputListener(private val cell: Cell) : InputListener() {
    override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
        if (!cell.hidden) return false
        if (Cell.active != cell)
            Cell.active?.setMode(-1)
        cell.setMode(button)
        return true
    }

    override fun keyDown(event: InputEvent?, keycode: Int): Boolean {
        when (cell.mode) {
            Cell.Mode.NONE -> return false
            Cell.Mode.INSERT -> {
                when (keycode) {
                    NUMPAD_0, NUM_0 -> cell.usrnum = 0
                    NUMPAD_1, NUM_1 -> cell.usrnum = 1
                    NUMPAD_2, NUM_2 -> cell.usrnum = 2
                    NUMPAD_3, NUM_3 -> cell.usrnum = 3
                    NUMPAD_4, NUM_4 -> cell.usrnum = 4
                    NUMPAD_5, NUM_5 -> cell.usrnum = 5
                    NUMPAD_6, NUM_6 -> cell.usrnum = 6
                    NUMPAD_7, NUM_7 -> cell.usrnum = 7
                    NUMPAD_8, NUM_8 -> cell.usrnum = 8
                    NUMPAD_9, NUM_9 -> cell.usrnum = 9
                    else -> return false
                }
                (((cell.parent) as Block).parent as Board).check()
            }
            Cell.Mode.POSSIBILITIES -> {
                when (keycode) {
                    NUMPAD_1, NUM_1 -> cell.switchPossibility(1)
                    NUMPAD_2, NUM_2 -> cell.switchPossibility(2)
                    NUMPAD_3, NUM_3 -> cell.switchPossibility(3)
                    NUMPAD_4, NUM_4 -> cell.switchPossibility(4)
                    NUMPAD_5, NUM_5 -> cell.switchPossibility(5)
                    NUMPAD_6, NUM_6 -> cell.switchPossibility(6)
                    NUMPAD_7, NUM_7 -> cell.switchPossibility(7)
                    NUMPAD_8, NUM_8 -> cell.switchPossibility(8)
                    NUMPAD_9, NUM_9 -> cell.switchPossibility(9)
                    else -> return false
                }
            }
        }
        return true
    }
}
