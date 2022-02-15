package com.jetpack.showcaseview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun ShowCaseText(
    currentTarget: ShowCaseProperty,
    boundsInParent: Rect,
    targetRadius: Float,
    onGloballyPositioned: (LayoutCoordinates) -> Unit
) {
    var txtOffsetY by remember { mutableStateOf(0f) }

    Column(
        modifier = Modifier
            .offset(y = with(LocalDensity.current) {
                txtOffsetY.toDp()
            })
            .onGloballyPositioned {
                onGloballyPositioned(it)
                val textHeight = it.size.height
                val possibleTop = boundsInParent.center.y - targetRadius - textHeight
                txtOffsetY = if (possibleTop > 0) {
                    possibleTop
                } else {
                    boundsInParent.center.y + targetRadius
                }
            }
            .padding(16.dp)
    ) {
        Text(
            text = currentTarget.title,
            fontSize = 24.sp,
            color = currentTarget.titleColor,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = currentTarget.subTitle,
            fontSize = 16.sp,
            color = currentTarget.subTitleColor,
            fontWeight = FontWeight.Normal
        )
    }
}

fun getOutCircleCenter(
    targetBound: Rect,
    textBound: Rect,
    targetRadius: Float,
    textHeight: Int,
    isInGutter: Boolean
): Offset {
    val outerCenterX: Float
    var outerCenterY: Float
    val onTop = targetBound.center.y - targetRadius - textHeight > 0
    val left = min(
        textBound.left,
        targetBound.left - targetRadius
    )
    val right = max(
        textBound.right,
        targetBound.right + targetRadius
    )
    val centerY = if (onTop) targetBound.center.y - targetRadius - textHeight
        else targetBound.center.y + targetRadius + textHeight

    outerCenterY = centerY
    outerCenterX = (left + right) / 2
    if (isInGutter) {
        outerCenterY = targetBound.center.y
    }

    return Offset(outerCenterX, outerCenterY)
}

fun getOuterRadius(
    textRect: Rect,
    targetRect: Rect
) : Float {
    val topLeftX = min(textRect.topLeft.x, targetRect.topLeft.x)
    val topLeftY = min(textRect.topLeft.y, targetRect.topLeft.y)
    val bottomRightX = max(textRect.bottomRight.x, targetRect.bottomRight.x)
    val bottomRightY = max(textRect.bottomRight.y, targetRect.bottomRight.y)
    val expandedBounds = Rect(topLeftX, topLeftY, bottomRightX, bottomRightY)
    val d = sqrt(expandedBounds.height.toDouble().pow(2.0) + expandedBounds.width.toDouble().pow(2.0)).toFloat()
    return (d / 2f)
}



















