package co.csedge.cubycare.utils

import co.csedge.cubycare.data.model.Child

val Child.currentAgeRange: String
    get() {
        val months = this.ageInMonths
        return when {
            months <= 0 -> "Birth (0 Month)"
            months == 1 -> "1 Month"
            months == 2 -> "2 Months"
            months == 3 -> "3 Months"
            months == 4 -> "4 Months"
            months == 5 -> "5 Months"
            months == 6 -> "6 Months"
            months == 7 -> "7 Months"
            months == 8 -> "8 Months"
            months == 9 -> "9 Months"
            months == 10 -> "10 Months"
            months == 11 -> "11 Months"
            months in 12..23 -> "12 Months (1 Year)"
            months in 24..35 -> "2 Years (24 Months)"
            months in 36..47 -> "3 Years"
            months in 48..59 -> "4 Years"
            else -> "5 Years"
        }
    }

val Child.currentVitalsAgeRange: String
    get() {
        val months = this.ageInMonths
        return when {
            months <= 6 -> "0-6 Months"
            months <= 12 -> "6-12 Months"
            else -> "1-5 Years"
        }
    }
