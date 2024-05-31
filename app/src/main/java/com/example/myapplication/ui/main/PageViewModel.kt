package com.example.myapplication.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.myapplication.R

private val TAB_CONTENTS = arrayOf(
    R.string.tab_1,
    R.string.tab_2,
    R.string.tab_3,
)

class PageViewModel : ViewModel() {

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> = _text.map { it }

    fun setText(text: String) {
        _text.value = text
    }
}