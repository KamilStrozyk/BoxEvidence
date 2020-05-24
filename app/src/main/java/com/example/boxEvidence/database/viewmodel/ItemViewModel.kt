package com.example.boxEvidence.database.viewmodel

import android.graphics.Bitmap
import android.media.Image


public data class ItemViewModel(val name: String, val photo: Bitmap?)

public data class ItemViewModelWithBox(val name: String, val box: String, val photo: Bitmap?)