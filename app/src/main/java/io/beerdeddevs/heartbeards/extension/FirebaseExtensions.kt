package io.beerdeddevs.heartbeards.extension

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

val timelineReference: DatabaseReference by lazy { persistenceDatabaseReference("timeline") }

fun persistenceDatabaseReference(referenceKey: String): DatabaseReference
        = FirebaseDatabase.getInstance().apply { setPersistenceEnabled(true) }.reference.child(referenceKey)

fun databaseReference(referenceKey: String): DatabaseReference
        = FirebaseDatabase.getInstance().reference.child(referenceKey)
