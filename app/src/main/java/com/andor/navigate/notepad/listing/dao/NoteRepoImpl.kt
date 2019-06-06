package com.andor.navigate.notepad.listing.dao

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.andor.navigate.notepad.auth.UserAuthentication.Companion.TAG
import com.andor.navigate.notepad.core.AppState
import com.google.firebase.firestore.FirebaseFirestore


class NoteRepoImpl(private val appStateRelay: MutableLiveData<AppState>) {
    private val db = FirebaseFirestore.getInstance()

    @WorkerThread
    fun insert(
        noteModel: NoteModel,
        uid: String
    ) {
        if (noteModel.id == NoteModel.DEFAULT_ID) {
            db.collection("Users").document(uid).collection("Notes")
                .document()
                .set(noteModel).addOnSuccessListener {
                    appStateRelay.postValue(appStateRelay.value!!.copy(selectedNote = noteModel))
                }
        } else {
            db.collection("Users").document(uid).collection("Notes")
                .document(noteModel.id)
                .set(noteModel).addOnSuccessListener {
                    appStateRelay.postValue(appStateRelay.value!!.copy(selectedNote = noteModel))
                }
        }

    }

    fun delete(selectedNotes: HashSet<NoteModel>, uid: String) {
        selectedNotes.forEach {
            db.collection("Users").document(uid).collection("Notes")
                .document(it.id).delete()
        }
    }

    @WorkerThread
    fun getAllNotes(uid: String) {
        db.collection("Users").document(uid).collection("Notes")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val list = ArrayList<NoteModel>()
                    for (doc in snapshot) {
                        val noteObject = doc.toObject(NoteModel::class.java)
                        noteObject.id = doc.id
                        list.add(noteObject)
                    }
                    appStateRelay.postValue(appStateRelay.value!!.copy(listOfAllNotes = list))
                } else {
                    Log.d(TAG, "Current data: null")
                }
            }
    }
}
