package com.github.matiuri.sudoku.game

import com.badlogic.gdx.math.MathUtils.random
import com.badlogic.gdx.math.MathUtils.randomBoolean
import java.util.*

object Tools {
    fun generate(cells: Array<Array<Cell>>) {
        cells.forEach { it.forEach { it.number = 0 } }
        val cols: Array<List<Cell>> = Array(9) { i ->
            val list: MutableList<Cell> = ArrayList()
            cells.forEach { it.filter { it.col == i + 1 }.forEach { list.add(it) } }
            list
        }
        val rows: Array<List<Cell>> = Array(9) { i ->
            val list: MutableList<Cell> = ArrayList()
            cells.forEach { it.filter { it.row == i + 1 }.forEach { list.add(it) } }
            list
        }
        val blocks: Array<List<Cell>> = Array(9) { i ->
            val list: MutableList<Cell> = ArrayList()
            cells.forEach { it.filter { it.block == i + 1 }.forEach { list.add(it) } }
            list
        }

        cells.forEach {
            it.forEach { cell ->
                cell.hidden = randomBoolean(0.7f)
                val col: Int = cell.col - 1
                val row: Int = cell.row - 1
                val block: Int = cell.block - 1
                val related: Set<Cell> = cols[col].union(rows[row].union(blocks[block]))
                var candidate: Int
                val attempts: MutableSet<Int> = HashSet()
                do {
                    candidate = random(1, 9)
                    attempts.add(candidate)
                    if (attempts.size == 9) throw IllegalStateException("Impossible Game")
                } while (related.filter { it.num != cell.num }.fold(false) { f, c -> c.number == candidate || f })
                cell.number = candidate
            }
        }
    }
}
