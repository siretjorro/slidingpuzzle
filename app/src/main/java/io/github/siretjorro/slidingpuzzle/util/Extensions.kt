package io.github.siretjorro.slidingpuzzle.util

import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun Long.getMinutes(): Long {
    val duration = this.toDuration(DurationUnit.MILLISECONDS)
    return duration.inWholeMinutes
}

fun Long.getSeconds(): Long {
    val duration = this.toDuration(DurationUnit.MILLISECONDS)
    return duration.inWholeSeconds % 60
}
