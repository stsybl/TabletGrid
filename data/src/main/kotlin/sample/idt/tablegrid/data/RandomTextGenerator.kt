package sample.idt.tablegrid.data
import kotlin.random.Random

class RandomTextGenerator(
    private val random: Random = Random.Default,
) {

    fun generate(): String {
        val wordsCount = random.nextInt(
            from = MIN_WORDS,
            until = MAX_WORDS + 1,
        )

        return buildString {
            repeat(wordsCount) { index ->
                if (index > 0) {
                    append(' ')
                }

                append(
                    WORDS[
                        random.nextInt(
                            from = 0,
                            until = WORDS.size,
                        )
                    ],
                )
            }
        }
    }

    private companion object {

        const val MIN_WORDS = 1
        const val MAX_WORDS = 5

        val WORDS = listOf(
            "apple",
            "table",
            "green",
            "water",
            "house",
            "cloud",
            "stone",
            "light",
            "river",
            "plant",
            "dream",
            "world",
            "chair",
            "paper",
            "mouse",
            "phone",
            "glass",
            "bread",
            "field",
            "music",
            "beach",
            "train",
            "heart",
            "space",
            "earth",
            "ocean",
            "night",
            "sweet",
            "grass",
            "clock",
            "forest",
            "garden",
            "coffee",
            "window",
            "summer",
            "winter",
            "spring",
            "orange",
            "yellow",
            "silver",
            "flower",
            "bridge",
            "street",
            "market",
            "travel",
            "planet",
            "camera",
            "system",
            "folder",
            "memory",
        )
    }
}