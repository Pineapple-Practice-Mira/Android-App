package site.pnpl.mira.utils

import android.text.InputFilter
import android.text.Spanned

class InputLettersFilter: InputFilter {
    override fun filter(
        cs: CharSequence,
        start: Int,
        end: Int,
        spanned: Spanned?,
        dStart: Int,
        dEnd: Int
    ): CharSequence {
        if (dStart >= MAX_LENGTH_IN_INPUT_NAME) return ""

        var filtered = ""
        var charSequence = cs

        if (dStart + end > MAX_LENGTH_IN_INPUT_NAME) {
            charSequence = charSequence.subSequence(0, MAX_LENGTH_IN_INPUT_NAME - dStart)
        }

        charSequence.forEach { character ->
            if (Character.isLetter(character) || Character.isWhitespace(character) || character == Char('-'.code)) {
                filtered += character.toString()
            }
        }

        return filtered
    }

}