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
        var filtered = ""
        if (dStart >= MAX_LENGTH_IN_INPUT_NAME) return ""

        for (i in start until end) {
            val character = cs[i]
            if (Character.isLetter(character) || Character.isWhitespace(character) || character == Char('-'.code)) {
                filtered += character.toString()
            }
        }

        return filtered
    }

}