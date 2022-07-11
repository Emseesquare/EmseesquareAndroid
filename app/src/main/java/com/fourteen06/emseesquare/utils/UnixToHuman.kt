package com.fourteen06.emseesquare.utils

import java.util.*

object UnixToHuman {
    private const val SECOND_MILLIS = 1000
    private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private const val DAY_MILLIS = 24 * HOUR_MILLIS
    private const val WEEK_MILLIS = 7 * DAY_MILLIS
    fun getTimeAgo(time: Long): String {
        var time = time
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000
        }
        val now = System.currentTimeMillis()
        var diff = now - time
        return if (diff > 0) {
            if (diff < MINUTE_MILLIS) {
                "just now"
            } else if (diff < 2 * MINUTE_MILLIS) {
                "a minute ago"
            } else if (diff < 50 * MINUTE_MILLIS) {
                (diff / MINUTE_MILLIS).toString() + " minutes ago"
            } else if (diff < 90 * MINUTE_MILLIS) {
                "an hour ago"
            } else if (diff < 24 * HOUR_MILLIS) {
                (diff / HOUR_MILLIS).toString() + " hours ago"
            } else if (diff < 48 * HOUR_MILLIS) {
                "yesterday"
            } else if (diff < 7 * DAY_MILLIS) {
                (diff / DAY_MILLIS).toString() + " days ago"
            } else if (diff < 2 * WEEK_MILLIS) {
                "a week ago"
            } else if (diff < WEEK_MILLIS * 3) {
                (diff / WEEK_MILLIS).toString() + " weeks ago"
            } else {
                val date = Date(time)
                date.toString()
            }
        } else {
            diff = time - now
            if (diff < MINUTE_MILLIS) {
                "this minute"
            } else if (diff < 2 * MINUTE_MILLIS) {
                "a minute later"
            } else if (diff < 50 * MINUTE_MILLIS) {
                (diff / MINUTE_MILLIS).toString() + " minutes later"
            } else if (diff < 90 * MINUTE_MILLIS) {
                "an hour later"
            } else if (diff < 24 * HOUR_MILLIS) {
                (diff / HOUR_MILLIS).toString() + " hours later"
            } else if (diff < 48 * HOUR_MILLIS) {
                "tomorrow"
            } else if (diff < 7 * DAY_MILLIS) {
                (diff / DAY_MILLIS).toString() + " days later"
            } else if (diff < 2 * WEEK_MILLIS) {
                "a week later"
            } else if (diff < WEEK_MILLIS * 3) {
                (diff / WEEK_MILLIS).toString() + " weeks later"
            } else {
                val date = Date(time)
                date.toString()
            }
        }
    }
}