package co.csedge.cubycare.data.repository


data class SmileCareInfo(
    val ageRange: String,
    val firstTooth: String,
    val brushing: String,
    val fluoride: String,
    val decayPrevention: String
)

object SmileCareProvider {

    val ageRanges = listOf(
        "0-6 Months",
        "6-12 Months",
        "1-5 Years"
    )

    fun getSmileInfoForAge(ageRange: String): SmileCareInfo {
        return allSmileData.find { it.ageRange == ageRange } ?: SmileCareInfo(
            ageRange = ageRange,
            firstTooth = "Data not available",
            brushing = "Data not available",
            fluoride = "Data not available",
            decayPrevention = "Data not available"
        )
    }

    private val allSmileData = listOf(
        SmileCareInfo(
            ageRange = "0-6 Months",
            firstTooth = "Signs of Teething (around 4-6 months):\n" +
                    "• Increased drooling and desire to chew on solid objects.\n" +
                    "• Mild fussiness and swollen, tender gums.\n\n" +
                    "Home Remedies:\n" +
                    "• Gently rub the gums with a clean finger or a moistened gauze pad.\n" +
                    "• Offer a chilled (not frozen) rubber teething ring.\n" +
                    "• Wipe drool frequently to prevent facial rashes.",
            brushing = "When & How:\n" +
                    "• Before teeth erupt: Gently wipe gums with a soft, clean, damp cloth twice a day (morning and night).\n" +
                    "• This removes bacteria and gets the baby used to the routine of daily oral care.",
            fluoride = "Fluoride toothpaste is generally NOT needed yet since teeth have not erupted. Stick to wiping gums with plain water.",
            decayPrevention = "How to Prevent It at Home:\n" +
                    "• Never put your baby to bed with a bottle of milk or formula. The natural sugars will pool in the mouth and encourage bacteria growth.\n" +
                    "• If a bottle is needed for comfort to sleep, use ONLY plain water.\n" +
                    "• Do not dip pacifiers in honey or sugar."
        ),
        SmileCareInfo(
            ageRange = "6-12 Months",
            firstTooth = "The first tooth typically erupts in this window!\n\n" +
                    "Home Remedies for Teething Pain:\n" +
                    "• Give chilled soft foods like unsweetened applesauce if the baby is already eating solids.\n" +
                    "• Continue offering chilled teething rings.\n" +
                    "• Massage the gums gently.",
            brushing = "When & How:\n" +
                    "• Once the first tooth appears, upgrade to a baby-sized, soft-bristled toothbrush.\n" +
                    "• Brush twice a day, especially right before bedtime.\n" +
                    "• Gently lift the lip to brush right at the gumline where plaque hides.",
            fluoride = "Fluoride Guidelines:\n" +
                    "• Start using a tiny 'Smear' or 'Rice-Grain' sized amount of fluoride toothpaste.\n" +
                    "• Since babies this age cannot spit, gently wipe away any excess toothpaste with a soft cloth after brushing.",
            decayPrevention = "How to Prevent It at Home:\n" +
                    "• Transition from a bottle to a sippy cup or an open cup by their first birthday.\n" +
                    "• Avoid introducing fruit juices; stick to breast milk, formula, or water.\n" +
                    "• Avoid sticky snacks that cling to the teeth."
        ),
        SmileCareInfo(
            ageRange = "1-5 Years",
            firstTooth = "A full set of 20 primary (baby) teeth usually comes in by age 3.\n" +
                    "Continue monitoring for signs of teething for the molars (which can be more painful). Keep using chilled teethers and gum massages.",
            brushing = "When & How:\n" +
                    "• Brush twice a day (morning and night).\n" +
                    "• Parents should continue to do the brushing until the child is around 7 or 8 years old (when they can tie their own shoes, they have the dexterity to brush well).\n" +
                    "• Use gentle circular motions on the front, back, and top of all teeth.",
            fluoride = "Fluoride Guidelines:\n" +
                    "• Ages 1 to 3: Continue using a 'Smear' or 'Rice-Grain' sized amount.\n" +
                    "• Ages 3 to 5: Upgrade to a 'Pea-Sized' amount.\n" +
                    "• Encourage your child to SPIT out the toothpaste, not swallow it. Do not have them rinse with water afterward, as the residual fluoride protects the teeth.",
            decayPrevention = "How to Prevent It at Home:\n" +
                    "• Limit sugary snacks and treats (especially gummy vitamins and raisins which stick in the grooves of teeth).\n" +
                    "• Ensure they only drink water between meals (save milk or juice for meal times).\n" +
                    "• Establish a firm 'no food after brushing at night' rule."
        )
    )
}
