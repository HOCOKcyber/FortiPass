package com.hocok.fortipass.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hocok.fortipass.R

private val MONTSERRAT = FontFamily(
    Font(resId = R.font.montserrat_regular, weight = FontWeight.Normal, style = FontStyle.Normal),
    Font(resId = R.font.montserrat_bold, weight = FontWeight.Bold, style = FontStyle.Normal),
)

val Typography = Typography(
    bodyMedium = TextStyle(
        fontFamily = MONTSERRAT,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        color = mainTextColor,
    ),
    bodyLarge = TextStyle(
        fontFamily = MONTSERRAT,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = mainTextColor,
    ),
    titleMedium = TextStyle(
        fontFamily = MONTSERRAT,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = secondaryTextColor,
    ),
    labelMedium = TextStyle(
        fontFamily = MONTSERRAT,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = secondaryTextColor,
    ),
    bodySmall = TextStyle(
        fontFamily = MONTSERRAT,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        color = onSecondTitleColor,
    )
)