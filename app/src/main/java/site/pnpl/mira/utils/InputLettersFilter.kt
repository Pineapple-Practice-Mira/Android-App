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
    ): CharSequence? {
        var filtered = ""
        for (i in start until end) {
            val character = cs[i]
            //Если нужно будет добавить пробел: Character.isWhitespace(character)
            if ( Character.isLetter(character)) {
                filtered += character
            }
        }
        return filtered
    }
}