package com.github.matiuri.sudoku.game

import com.badlogic.gdx.math.MathUtils.random
import com.badlogic.gdx.math.MathUtils.randomBoolean
import java.util.*

object Tools {
    fun generate(cells: Array<Array<Cell>>) {
        val cols: Array<List<Cell>> = colsList(cells)
        val rows: Array<List<Cell>> = rowsList(cells)
        val blocks: Array<List<Cell>> = blocksList(cells)

        cells.forEach { it.forEach { it.number = 0 } }
        cells.forEach {
            it.forEach { cell ->
                cell.hidden = randomBoolean(.65f)
                val related: Set<Cell> = relatedCells(blocks, cell, cols, rows)
                var candidate: Int
                val attempts: MutableSet<Int> = HashSet()
                do {
                    candidate = random(1, 9)
                    attempts.add(candidate)
                    if (attempts.size == 9) throw IllegalStateException("Impossible Game, Generator")
                } while (related.filter { it.num != cell.num }.fold(false) { f, c -> c.number == candidate || f })
                cell.number = candidate
                if (!cell.hidden) cell.usrnum = cell.number
                else cell.usrnum = 0
            }
        }
    }

    fun solve(cells: Array<Array<Cell>>) {
        val cols: Array<List<Cell>> = colsList(cells)
        val rows: Array<List<Cell>> = rowsList(cells)
        val blocks: Array<List<Cell>> = blocksList(cells)

        var solved: Boolean = false
        while (!solved) {
            //Thread.sleep(1000)
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
            //Thread.sleep(5000)
            var impossible: Boolean = true
            cells.forEach {
                it.filter { it.hidden && it.usrnum == 0 && it.possibilities.size == 1 }.forEach {
                    impossible = false
                    it.usrnum = it.possibilities.first()
                }
            }
            if (impossible) throw IllegalStateException("Impossible Game, Solver")
            solved = cells.fold(true) {f_, c_ ->
                f_ && c_.fold(true) { f, c ->
                    f && (!c.hidden || c.number == c.usrnum)
                }
            }
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
