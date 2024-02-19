package com.kien.petclub.domain.model.entity

import com.google.firebase.database.DataSnapshot

data class InfoProduct(
    var id: String = "",
    var name: String = "",
    var parentId: String? = null,
    var child: ArrayList<InfoProduct>? = null
)

fun mapSnapshotToInfoProduct(snapshot: DataSnapshot): InfoProduct {
    val id = snapshot.child("id").getValue(String::class.java)
    val name = snapshot.child("name").getValue(String::class.java)
    val parentId = snapshot.child("parentId").getValue(String::class.java)
    val childrenSnapshot = snapshot.child("child")

    val children = childrenSnapshot.children.mapNotNull { childSnapshot ->
        mapSnapshotToInfoProduct(childSnapshot)
    }.let {
        if (it.isEmpty()) null else ArrayList(it)
    }

    return InfoProduct(id ?: "", name ?: "", parentId, children)
}