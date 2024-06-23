package we.kotlinl0vers.event

import java.lang.reflect.InvocationTargetException

open class EventBase {
    fun call() {
        val methodBeans = EventManager.getEvent(this::class.java) ?: return

        methodBeans.forEach { event ->
            try {
                event.method.invoke(event.targetObject, this)
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }
        }
    }

}