package com.hocok.fortipass.domain.usecase

import android.util.Log
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import com.hocok.fortipass.presentation.generator.PasswordOption
import com.hocok.fortipass.presentation.ui.theme.blueForNumberColor
import com.hocok.fortipass.presentation.ui.theme.mainTextColor
import com.hocok.fortipass.presentation.ui.theme.redForSymbolColor

private const val numbers = "0123456789"
private const val upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
private const val lowerCase = "abcdefghijklmnopqrstuvwxyz"
private const val symbols = "!@#\$%^&*"
private const val similar = "1iIlL0oO"

class CreatePassword{

    private data class CustomAlphabet(
        val alphabet: String,
        val style: SpanStyle,
    )

    operator fun invoke(passwordOption: PasswordOption,isSimilarSymbolsExclude: Boolean): TextFieldValue{
        return  if (checkOptions(passwordOption) ) TextFieldValue(createRandomPassword(passwordOption, isSimilarSymbolsExclude))
                else TextFieldValue("")
    }

    private fun createRandomPassword(passwordOption: PasswordOption, isSimilarSymbolsExclude: Boolean): AnnotatedString{

        val listOfAlphabet = buildList {
            if (passwordOption.isNumberInclude) add(CustomAlphabet(
                alphabet = excludeSimilar(isSimilarSymbolsExclude, numbers),
                style = SpanStyle(blueForNumberColor))
            )
            if (passwordOption.isUpperCaseInclude) add(CustomAlphabet(
                alphabet = excludeSimilar(isSimilarSymbolsExclude, upperCase),
                style = SpanStyle(mainTextColor))
            )
            if (passwordOption.isLowerCaseInclude) add(CustomAlphabet(
                alphabet = excludeSimilar(isSimilarSymbolsExclude, lowerCase),
                style = SpanStyle(mainTextColor))
            )
            if (passwordOption.isSymbolsInclude) add(CustomAlphabet(
                alphabet = excludeSimilar(isSimilarSymbolsExclude, symbols),
                style = SpanStyle(redForSymbolColor))
            )
        }


        return buildAnnotatedString {
            repeat(passwordOption.length.toInt()){
                val customAlphabet = when{
                    it == 2 -> listOfAlphabet[0]
                    it == 4 && listOfAlphabet.size >= 2 -> listOfAlphabet[1]
                    it == 6 && listOfAlphabet.size >= 3 -> listOfAlphabet[2]
                    it == 7 && listOfAlphabet.size == 4 -> listOfAlphabet[3]
                    else -> listOfAlphabet.random()
                }
                addStyle(customAlphabet.style, start = it, end = it + 1)
                val newSymbol = customAlphabet.alphabet.random()
                Log.d("newSymbol", newSymbol.toString())
                append(newSymbol)
            }
        }
    }

    private fun excludeSimilar(isSimilarSymbolsExclude: Boolean, alphabet: String): String{
        return  if (isSimilarSymbolsExclude) alphabet.filter { it !in similar }
                else alphabet
    }

    private fun checkOptions(passwordOption: PasswordOption): Boolean{
        return passwordOption.isNumberInclude ||
            passwordOption.isSymbolsInclude ||
            passwordOption.isLowerCaseInclude ||
            passwordOption.isUpperCaseInclude
    }

    operator fun invoke(mask: String,isSimilarSymbolsExclude: Boolean): TextFieldValue{
        val result = try {
                        createMaskPassword(
                            mask = if (mask.count{it == '/'} % 2 != 0) "$mask/" else mask,
                            isSimilarSymbolsExclude = isSimilarSymbolsExclude)
                    } catch (e: ErrorMask){
                        AnnotatedString(e.message.toString())
                    } catch (e: Exception){
                        AnnotatedString(e.message.toString())
                    }

        return TextFieldValue(result)
    }

    private fun createMaskPassword(mask: String, isSimilarSymbolsExclude: Boolean): AnnotatedString{
        val mapCustomAlphabet = mapOf(
            'X' to CustomAlphabet(
                alphabet = excludeSimilar(isSimilarSymbolsExclude, upperCase),
                style = SpanStyle(mainTextColor)),
            'x' to CustomAlphabet(
                alphabet = excludeSimilar(isSimilarSymbolsExclude, lowerCase),
                style = SpanStyle(mainTextColor)),
            '#' to CustomAlphabet(
                alphabet = excludeSimilar(isSimilarSymbolsExclude, numbers),
                style = SpanStyle(blueForNumberColor)),
            'S' to CustomAlphabet(
                alphabet = excludeSimilar(isSimilarSymbolsExclude, symbols),
                style = SpanStyle(redForSymbolColor)),
            'R' to CustomAlphabet(
                alphabet = excludeSimilar(isSimilarSymbolsExclude, numbers + upperCase + lowerCase),
                style = SpanStyle(mainTextColor)),
        )

        var index = 0
        return buildAnnotatedString {
            while (index < mask.length) {
                when (val specialSymbol = mask[index]) {
                    '/' -> {
                        index++
                        val nextSlashIndex = mask.substring(index).indexOf('/') + index
                        val subMask = mask.substring(index..<nextSlashIndex)
                        subMask.forEach{subMaskElement ->
                            append(
                                AnnotatedString(
                                    text = subMaskElement.toString(),
                                    spanStyle = getStyleBySymbol(subMaskElement))
                            )
                        }
                        index = nextSlashIndex
                    }

                    in "Xx#SR" -> {
                        val customAlphabet = mapCustomAlphabet[specialSymbol]!!
                        val newSymbolFromAlphabet = customAlphabet.alphabet.random()
                        val style = if (specialSymbol == 'R') getStyleBySymbol(newSymbolFromAlphabet)
                                    else customAlphabet.style
                        append(
                            AnnotatedString(
                                text = newSymbolFromAlphabet.toString(),
                                spanStyle = style)
                        )
                    }

                    else -> {
                        throw ErrorMask("$specialSymbol?")
                    }
                }
                index++
            }
        }
    }

    private fun getStyleBySymbol(symbol: Char): SpanStyle{
        return when(symbol){
            in numbers -> SpanStyle(blueForNumberColor)
            in symbols -> SpanStyle(redForSymbolColor)
            else -> SpanStyle(mainTextColor)
        }
    }

    class ErrorMask(message: String): Exception(message)
}