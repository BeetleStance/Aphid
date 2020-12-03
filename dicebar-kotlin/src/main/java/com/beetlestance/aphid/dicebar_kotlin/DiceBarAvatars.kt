package com.beetlestance.aphid.dicebar_kotlin

import androidx.annotation.StringDef

/**
 * Do you want to create male, female or abstract avatars? You have the choice between several
 * lovely designed avatar styles.
 *
 * @see <a href="https://avatars.dicebear.com/styles">DiceBar Styles</a>
 * */
@StringDef(
    DiceBarAvatars.AVATAAARS,
    DiceBarAvatars.BOTTTS,
    DiceBarAvatars.FEMALE,
    DiceBarAvatars.GRIDY,
    DiceBarAvatars.HUMAN,
    DiceBarAvatars.IDENTICON,
    DiceBarAvatars.INITIALS,
    DiceBarAvatars.JDENTICON,
    DiceBarAvatars.MALE
)
@Retention(AnnotationRetention.SOURCE)
annotation class DiceBarAvatars {
    companion object {

        const val AVATAAARS: String = "avataaars"

        const val BOTTTS: String = "bottts"

        const val FEMALE: String = "female"

        const val GRIDY: String = "gridy"

        const val HUMAN: String = "human"

        const val IDENTICON: String = "identicon"

        const val INITIALS: String = "initials"

        const val JDENTICON: String = "jdenticon"

        const val MALE: String = "male"

        val possibleValues: List<String> = listOf(
            AVATAAARS, BOTTTS, FEMALE, GRIDY, HUMAN, IDENTICON, INITIALS, JDENTICON, MALE
        )
    }
}
