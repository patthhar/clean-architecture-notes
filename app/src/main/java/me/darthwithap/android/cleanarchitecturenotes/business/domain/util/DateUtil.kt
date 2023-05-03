package me.darthwithap.android.cleanarchitecturenotes.business.domain.util

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

//dateFormat : "2023-04-14<space>HH:mm:ss"

@Singleton
class DateUtil @Inject constructor(
  private val dateFormat: SimpleDateFormat
) {
  fun removeTimeFromStringDate(stringDate: String): String {
    return stringDate.substring(0, stringDate.indexOf(" "))
  }

  fun timestampToStringDate(timestamp: Timestamp): String {
    return dateFormat.format(timestamp.toDate())
  }

  fun stringDateToTimestamp(stringDate: String): Timestamp? {
    return dateFormat.parse(stringDate)?.let { Timestamp(it) }
  }

  fun getCurrentTimestamp(): String {
    return dateFormat.format(Date())
  }
}