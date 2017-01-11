package com.github.matiuri.sudoku

import android.os.Bundle

import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.github.matiuri.sudoku.Game

class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()
        initialize(Game(), config)
    }
}
