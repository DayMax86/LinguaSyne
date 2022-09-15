package com.example.linguasyne.screens

import androidx.compose.runtime.Composable
import com.example.linguasyne.ui.animations.AnimateLoading

@Composable
fun Animate(
    toAnimate: Boolean,
){
    AnimateLoading(toAnimate)
}