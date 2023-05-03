package me.darthwithap.android.cleanarchitecturenotes.business.domain.model

import me.darthwithap.android.cleanarchitecturenotes.business.domain.util.DateUtil
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteFactory @Inject constructor(
  private val dateUtil: DateUtil
) {

  fun createSingleNote(
    id: String? = null, title: String, body: String? = null
  ): Note {
    return Note(
      id = id ?: UUID.randomUUID().toString(),
      title = title,
      body = body ?: "",
      createdAt = dateUtil.getCurrentTimestamp(),
      updatedAt = dateUtil.getCurrentTimestamp()
    )
  }

  //For the purpose of testing
  fun createNoteList(num: Int): ArrayList<Note> {
    val noteList: ArrayList<Note> = ArrayList()
    for (i in 0 until num) {
      noteList.add(createSingleNote(title = "Note #${i}"))
    }
    return noteList
  }
}