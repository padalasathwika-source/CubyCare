package co.csedge.cubycare.data.repository

import co.csedge.cubycare.data.model.DevelopmentalMilestone
import java.util.UUID

object MilestoneProvider {

    fun generateDefaultMilestones(): List<DevelopmentalMilestone> {
        val milestones = mutableListOf<DevelopmentalMilestone>()

        fun add(domain: String, title: String, ageRange: String, ageMonths: Double) {
            milestones.add(
                DevelopmentalMilestone(
                    id = UUID.randomUUID().toString(),
                    domain = domain,
                    title = title,
                    ageMonths = ageMonths,
                    ageRange = ageRange
                )
            )
        }

        val grossMotor = "Gross Motor"
        val fineMotor = "Fine Motor"
        val language = "Language & Communication"
        val social = "Social & Emotional"
        val cognitive = "Cognitive & Learning"

        // Birth (0 Month)
        val birth = "Birth (0 Month)"
        add(grossMotor, "Turns head side to side while lying on back", birth, 0.0)
        add(grossMotor, "Lifts head briefly during tummy time", birth, 0.0)
        add(fineMotor, "Strong involuntary reflex grasp when palm is touched", birth, 0.0)
        add(language, "Reacts to sudden loud sounds with startle reflex (Moro)", birth, 0.0)
        add(language, "Makes small throaty gurgling sounds", birth, 0.0)
        add(social, "Focuses gaze on parents' faces within 8–12 inches", birth, 0.0)
        add(social, "Calms down when held or spoken to in a soothing voice", birth, 0.0)
        add(cognitive, "Roots and turns toward nipple when cheek is stroked", birth, 0.0)

        // 1 Month
        val m1 = "1 Month"
        add(grossMotor, "Holds head up momentarily while lying on chest", m1, 1.0)
        add(fineMotor, "Brings hands near eyes and mouth", m1, 1.0)
        add(language, "Makes soft cooing sounds (ooh, aah)", m1, 1.0)
        add(social, "Briefly gazes at caregivers and makes eye contact", m1, 1.0)
        add(cognitive, "Tracks slowly moving objects across field of vision", m1, 1.0)

        // 2 Months
        val m2 = "2 Months"
        add(grossMotor, "Pushes up on arms during tummy time with smooth movements", m2, 2.0)
        add(fineMotor, "Opens hands and holds objects briefly", m2, 2.0)
        add(language, "Turns head toward the direction of voices", m2, 2.0)
        add(social, "Smiles responsively at parents (social smile)", m2, 2.0)
        add(cognitive, "Follows objects with eyes in a 180-degree arc", m2, 2.0)

        // 3 Months
        val m3 = "3 Months"
        add(grossMotor, "Holds head and chest steady while sitting with support", m3, 3.0)
        add(fineMotor, "Reaches for and swipes at dangling toys", m3, 3.0)
        add(language, "Laughs out loud and squeals in delight", m3, 3.0)
        add(social, "Enjoys playing with people and cries when play stops", m3, 3.0)
        add(cognitive, "Recognizes familiar faces and objects at a distance", m3, 3.0)

        // 4 Months
        val m4 = "4 Months"
        add(grossMotor, "Rolls over from tummy to back independently", m4, 4.0)
        add(fineMotor, "Grasps a rattle tightly and shakes it", m4, 4.0)
        add(language, "Babbles with expression and copies sounds heard", m4, 4.0)
        add(social, "Smiles spontaneously at other children and mirrors expressions", m4, 4.0)
        add(cognitive, "Reaches for toy with one hand and brings it to mouth", m4, 4.0)

        // 6 Months
        val m6 = "6 Months"
        add(grossMotor, "Sits without support for several seconds", m6, 6.0)
        add(grossMotor, "Rolls over in both directions (back to tummy & tummy to back)", m6, 6.0)
        add(fineMotor, "Transfers a toy from one hand to the other hand", m6, 6.0)
        add(language, "Responds to own name by turning head", m6, 6.0)
        add(social, "Recognizes familiar versus stranger faces", m6, 6.0)
        add(cognitive, "Looks for objects that fall out of sight (Object Permanence)", m6, 6.0)

        // 9 Months
        val m9 = "9 Months"
        add(grossMotor, "Pulls self up to standing position holding furniture", m9, 9.0)
        add(grossMotor, "Crawls efficiently on hands and knees", m9, 9.0)
        add(fineMotor, "Uses pincer grasp (thumb and index finger) to pick up small food", m9, 9.0)
        add(language, "Understands 'No' and babbles repetitive syllables (ba-ba, ma-ma)", m9, 9.0)
        add(social, "Plays peek-a-boo and pat-a-cake with excitement", m9, 9.0)
        add(cognitive, "Explores objects in many ways (shaking, banging, throwing)", m9, 9.0)

        // 12 Months (1 Year)
        val m12 = "12 Months (1 Year)"
        add(grossMotor, "Takes first independent steps or walks holding one hand", m12, 12.0)
        add(fineMotor, "Puts objects into a container and takes them out", m12, 12.0)
        add(language, "Says 1 to 3 words with meaning ('mama', 'dada', 'bye')", m12, 12.0)
        add(social, "Waves 'bye-bye' and gestures for wants", m12, 12.0)
        add(cognitive, "Points at objects or pictures when asked", m12, 12.0)

        // 2 Years (24 Months)
        val m24 = "2 Years (24 Months)"
        add(grossMotor, "Runs smoothly and kicks a ball forward without losing balance", m24, 24.0)
        add(fineMotor, "Builds a tower of 4 to 6 blocks", m24, 24.0)
        add(language, "Speaks 50+ words and forms 2-word phrases ('want milk')", m24, 24.0)
        add(social, "Shows parallel play alongside other children", m24, 24.0)
        add(cognitive, "Sorts shapes and colors correctly", m24, 24.0)

        // 3 Years
        val m36 = "3 Years"
        add(grossMotor, "Pedals a tricycle and climbs stairs alternating feet", m36, 36.0)
        add(fineMotor, "Copies a circle with a crayon and uses safety scissors", m36, 36.0)
        add(language, "Speaks in full 3 to 4 word sentences", m36, 36.0)
        add(social, "Takes turns in games and shows empathy for upset friends", m36, 36.0)
        add(cognitive, "Understands concepts of 'same' and 'different'", m36, 36.0)

        // 4 Years
        val m48 = "4 Years"
        add(grossMotor, "Hops on one foot and catches a bounced ball reliably", m48, 48.0)
        add(fineMotor, "Draws a person with 3 to 4 body parts", m48, 48.0)
        add(language, "Tells stories and recites simple nursery rhymes", m48, 48.0)
        add(social, "Enjoys cooperative pretend play with playmates", m48, 48.0)
        add(cognitive, "Names at least 4 colors and counts 5+ items", m48, 48.0)

        // 5 Years
        val m60 = "5 Years"
        add(grossMotor, "Swings independently, skips, and stands on one foot for 10 seconds", m60, 60.0)
        add(fineMotor, "Prints own first name and draws a person with 6+ body parts", m60, 60.0)
        add(language, "Uses future tense and speaks clearly in complex sentences", m60, 60.0)
        add(social, "Wants to please friends and agrees to rules", m60, 60.0)
        add(cognitive, "Counts 10 or more objects and understands daily time routines", m60, 60.0)

        return milestones
    }
}
