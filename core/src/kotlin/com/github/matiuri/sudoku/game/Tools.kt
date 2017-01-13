package com.github.matiuri.sudoku.game

import com.badlogic.gdx.math.MathUtils.random
import mati.advancedgdx.AdvancedGame
import java.util.*

object Tools {
    fun generate(cells: Array<Array<Cell>>) {
        val cols: Array<List<Cell>> = colsList(cells)
        val rows: Array<List<Cell>> = rowsList(cells)
        val blocks: Array<List<Cell>> = blocksList(cells)

        cells.forEach {
            it.forEach {
                it.number = 0
                it.usrnum = 0
                it.possibilities.clear()
                it.hidden = false
            }
        }
        cells.forEach {
            it.forEach { cell ->
                val related: Set<Cell> = relatedCells(blocks, cell, cols, rows)
                var candidate: Int
                val attempts: MutableSet<Int> = HashSet()
                do {
                    if (attempts.size == 9) {
                        throw IllegalStateException("Impossible Game, Generator")
                    }
                    candidate = random(1, 9)
                    attempts.add(candidate)
                } while (related.filter { it.num != cell.num }.fold(false) { f, c -> c.number == candidate || f })
                cell.number = candidate
                cell.usrnum = cell.number
            }
        }
    }

    fun remove(cells: Array<Array<Cell>>, difficulty: Int = 30) {
        AdvancedGame.log.d(this.javaClass.simpleName, "Attempting to remove cells")
        (1..difficulty).forEach {
            var cell: Cell
            val possibleCells = cells.flatMap { it.asIterable() }.filter { !it.hidden }
            val tries: MutableSet<Cell> = HashSet()
            do {
                val possibilities = possibleCells.filter { !tries.contains(it) }
                if (possibilities.isEmpty()) throw IllegalStateException("Impossible Game, Generator")
                cell = possibilities[random(0, possibilities.size - 1)]
                cell.hidden = true
                cell.usrnum = 0
                var failed: Boolean
                try {
                    AdvancedGame.log.d(this.javaClass.simpleName, "Attempting to solve")
                    solve(cells)
                    cell.usrnum = 0
                    failed = false
                } catch (e: IllegalStateException) {
                    cell.hidden = false
                    cell.usrnum = cell.number
                    tries.add(cell)
                    failed = true
                }
                AdvancedGame.log.d(this.javaClass.simpleName, "$failed")
            } while (failed)
        }
        AdvancedGame.log.d(this.javaClass.simpleName, "Removed")
    }

    private fun solve(cells: Array<Array<Cell>>) {
        val cols: Array<List<Cell>> = colsList(cells)
        val rows: Array<List<Cell>> = rowsList(cells)
        val blocks: Array<List<Cell>> = blocksList(cells)

        cells.forEach {
            it.forEach {
                it.usrnum = 0
                it.possibilities.clear()
            }
        }
        var solved: Boolean = false
        while (!solved) {
            cells.forEach {
                it.filter { it.hidden && it.usrnum == 0 }.forEach { cell ->
                    cell.possibilities.clear()
                    val related = relatedCells(blocks, cell, cols, rows).filter { it.num != cell.num }
                    (1..9).filter { i ->
                        related.fold(true) { f, c ->
                            (c.hidden || c.number != i) && (!c.hidden || c.usrnum != i) && f
                        }
                    }.forEach {
                        cell.possibilities.add(it)
                    }
                }
            }
            var impossible: Boolean = true
            cells.forEach {
                it.filter { it.hidden && it.usrnum == 0 && it.possibilities.size == 1 }.forEach {
                    impossible = false
                    it.usrnum = it.possibilities.first()
                }
            }
            if (impossible) throw IllegalStateException("Impossible Game, Solver")
            solved = cells.fold(true) { f_, c_ ->
                f_ && c_.fold(true) { f, c ->
                    f && (!c.hidden || c.number == c.usrnum)
                }
            }
            AdvancedGame.log.d(this.javaClass.simpleName, "Solving try")
        }
    }

    private fun relatedCells(blocks: Array<List<Cell>>, cell: Cell, cols: Array<List<Cell>>, rows: Array<List<Cell>>):
            Set<Cell> {
        val col: Int = cell.col - 1
        val row: Int = cell.row - 1
        val block: Int = cell.block - 1
        return cols[col].union(rows[row].union(blocks[block]))
    }

    private fun blocksList(cells: Array<Array<Cell>>): Array<List<Cell>> {
        val blocks: Array<List<Cell>> = Array(9) { i ->
            val list: MutableList<Cell> = ArrayList()
            cells.forEach { it.filter { it.block == i + 1 }.forEach { list.add(it) } }
            list
        }
        return blocks
    }

    private fun rowsList(cells: Array<Array<Cell>>): Array<List<Cell>> {
        val rows: Array<List<Cell>> = Array(9) { i ->
            val list: MutableList<Cell> = ArrayList()
            cells.forEach { it.filter { it.row == i + 1 }.forEach { list.add(it) } }
            list
        }
        return rows
    }

    private fun colsList(cells: Array<Array<Cell>>): Array<List<Cell>> {
        val cols: Array<List<Cell>> = Array(9) { i ->
            val list: MutableList<Cell> = ArrayList()
            cells.forEach { it.filter { it.col == i + 1 }.forEach { list.add(it) } }
            list
        }
        return cols
    }
}
