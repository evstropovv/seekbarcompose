package com.evstropovv.seekbarcompose

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


const val SeekBarTestTag = "SeekBarTestTag"

/**
 * SeekBar
 * For show and control progress
 */

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Seekbar(
    modifier: Modifier = Modifier,
    position: State<Long>,
    duration: State<Long>,
    onNewProgress: (progress: Long) -> Unit,
    onDragStart: (progress: Long) -> Unit,
    onDragEnd: (progress: Long) -> Unit,
    progressColor: Color = MaterialTheme.colors.primary,
    backgroundColor: Color = Color.Gray,
    sliderColor: Color = MaterialTheme.colors.primary,
    progressLineHeight: Dp = 4.dp,
    sliderWidth: Dp = 20.dp,
    sliderHeight: Dp = 12.dp
) {

    val isDragging = remember { mutableStateOf(false) }
    val dragProgress = remember { mutableStateOf(0f) }

    val progressLineHeightPx = with(LocalDensity.current) { progressLineHeight.toPx() }
    val sliderWidthPx = with(LocalDensity.current) { sliderWidth.toPx() }
    val sliderHeightPx = with(LocalDensity.current) { sliderHeight.toPx() }
    val sliderSize = Size(sliderWidthPx, sliderHeightPx)
    val sliderSizeOnDragging = Size(sliderWidthPx * 1.5f, sliderHeightPx * 1.5f)

    Box(
        modifier = modifier
            .testTag(SeekBarTestTag)
            .height(64.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            val p = it.x / (size.width - sliderWidthPx)
                            dragProgress.value = p
                            val newPosition = (dragProgress.value * duration.value).toLong()
                            onDragStart(newPosition)
                            isDragging.value = true
                            Log.d(SeekBarTestTag, "onDragStart $p")
                        },
                        onDragEnd = {
                            val newPosition = (dragProgress.value * duration.value).toLong()
                            onDragEnd(newPosition)
                            isDragging.value = false
                            Log.d(SeekBarTestTag, "onDragEnd ${dragProgress.value}")
                        },
                        onDrag = { change: PointerInputChange, dragAmount ->
                            change.consumeAllChanges()
                            //calculate progress from 0.0f to 1.0f
                            val newProgress = dragAmount.x / (size.width - sliderWidthPx)
                            dragProgress.value = (dragProgress.value + newProgress).roundTo1()
                            val newPosition = (dragProgress.value * duration.value).toLong()
                            onNewProgress(newPosition)
                            Log.d(SeekBarTestTag, "onDrag ${change.position.x}")
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectTapGestures {
                        val newProgress = it.x / (size.width - sliderWidthPx)
                        dragProgress.value = newProgress.roundTo1()
                        val newPosition = (dragProgress.value * duration.value).toLong()
                        onNewProgress(newPosition)
                        onDragEnd(newPosition)
                        Log.d(SeekBarTestTag, "tap ${dragProgress.value}")
                    }
                }
        ) {

            val canvasWidth = size.width
            val canvasHeight = size.height

            val progressFloat = (position.value.toDouble() / duration.value.toDouble()).toFloat()
            val offsetX = (canvasWidth - sliderSize.width) * progressFloat

            //progress line
            drawLine(
                start = Offset(x = 0f, y = canvasHeight / 2),
                end = Offset(x = offsetX, y = canvasHeight / 2),
                color = progressColor,
                strokeWidth = progressLineHeightPx
            )

            //background
            drawLine(
                start = Offset(x = offsetX, y = canvasHeight / 2),
                end = Offset(x = canvasWidth, y = canvasHeight / 2),
                color = backgroundColor,
                strokeWidth = progressLineHeightPx
            )

            val p = if (isDragging.value) {
                dragProgress.value
            } else {
                progressFloat
            }

            val sliderOffsetX = (canvasWidth - sliderSize.width) * p

            //increase slider size on touch
            val adaptiveSliderSize = if (isDragging.value) sliderSizeOnDragging else sliderSize

            //slider
            drawRoundRect(
                color = sliderColor,
                topLeft = Offset(x = sliderOffsetX, y = canvasHeight / 2 - adaptiveSliderSize.height / 2),
                size = adaptiveSliderSize,
                cornerRadius = CornerRadius(12f, 12f)
            )

        }
    }

}

fun Float.roundTo1() : Float{
  return  when (this) {
        in (0f..1f) -> this
        in (Float.NEGATIVE_INFINITY..0f) -> 0f
        else -> 1f
    }
}

@Preview
@Composable
fun SeekbarPreview() {
    Seekbar(
        modifier = Modifier.fillMaxWidth(),
        duration  = mutableStateOf(1000),
        position = mutableStateOf(300),
        onNewProgress = { },
        onDragStart = {},
        onDragEnd = {})
}