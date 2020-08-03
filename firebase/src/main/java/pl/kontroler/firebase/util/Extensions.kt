package pl.kontroler.firebase.util

import com.google.firebase.firestore.Query


/**
 * @author Rafał Nowowieski
 */

fun Query.asSnapshotLiveData(): QuerySnapshotLiveData {
    return QuerySnapshotLiveData(this)
}