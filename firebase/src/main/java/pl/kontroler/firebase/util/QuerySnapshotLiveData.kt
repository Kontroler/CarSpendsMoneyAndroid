package pl.kontroler.firebase.util

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.*


/**
 * @author Rafał Nowowieski
 */

class QuerySnapshotLiveData(private val query: Query) : LiveData<Resource<QuerySnapshot>>(),
    EventListener<QuerySnapshot> {

    private var registration: ListenerRegistration? = null

    override fun onEvent(snapshots: QuerySnapshot?, e: FirebaseFirestoreException?) {
        value = if (e != null) {
            Resource(e)
        } else {
            Resource(snapshots!!)
        }
    }

    override fun onActive() {
        super.onActive()
        registration = query.addSnapshotListener(this)
    }

    override fun onInactive() {
        super.onInactive()

        registration?.also {
            it.remove()
            registration = null
        }
    }
}