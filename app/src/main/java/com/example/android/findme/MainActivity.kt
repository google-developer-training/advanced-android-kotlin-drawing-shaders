/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.findme

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

/**
 * This app demonstrates a simple game using BitmapShaders.
 *
 * Game play:
 *
 * The user is presented with a dark surface, on touching the screen a white circle is displayed.
 * This represents a wall shone at with a spotlight. Touching the
 * surface hides an android image. The light cone then follows continuous
 * motion. If the image of an android is discovered, the screen lights up and
 * the word "WIN!" appears. To restart touch the screen again.
 *
 * The following limitations are imposed to keep the code focused.
 *
 *     * No startup screen or any other functionality other than game play.
 *     * No saving of state, game, or user data.
 *     * No acrobatics to handle edge cases.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dialog = createInstructionsDialog()
        dialog.show()
    }

    private fun createInstructionsDialog(): Dialog {
        val builder = AlertDialog.Builder(this)

        builder.setIcon(R.drawable.android)
                .setTitle(R.string.instructions_title)
                .setMessage(R.string.instructions)
                .setPositiveButtonIcon(ContextCompat.getDrawable(this, android.R.drawable.ic_media_play))
        return builder.create()
    }
}
