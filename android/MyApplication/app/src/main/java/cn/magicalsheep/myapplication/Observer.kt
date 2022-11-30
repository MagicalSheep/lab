package cn.magicalsheep.myapplication

import android.widget.TextView
import androidx.lifecycle.*

class Observer(
    private val name: String,
    private val methodList: TextView,
    private val status: TextView
) : LifecycleEventObserver {

    private val repository = Repository

    private fun update(format: String, status: String) {
        repository.methodList.insert(
            0,
            String.format(
                format,
                name
            )
        )
        repository.status[name] = status
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> update("%s.onCreate()\n", "Created")
            Lifecycle.Event.ON_START -> update("%s.onStart()\n", "Started")
            Lifecycle.Event.ON_RESUME -> update("%s.onResume()\n", "Resumed")
            Lifecycle.Event.ON_PAUSE -> update("%s.onPause()\n", "Paused")
            Lifecycle.Event.ON_STOP -> update("%s.onStop()\n", "Stopped")
            Lifecycle.Event.ON_DESTROY -> update("%s.onDestroy()\n", "Destroyed")
            Lifecycle.Event.ON_ANY -> {}
        }

        methodList.text = repository.methodList
        val statusBuilder = StringBuilder()
        repository.status.forEach { (k, v) ->
            statusBuilder.append(String.format("%s: %s\n", k, v))
        }
        status.text = statusBuilder
    }

}