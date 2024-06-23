package we.kotlinl0vers.event

import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.collections.HashMap


object EventManager {
    private val events: HashMap<Class<out EventBase?>, CopyOnWriteArrayList<MethodData>> = HashMap()

    fun register(o: Any) {
        val type: Class<*> = o.javaClass

        for (method in type.declaredMethods) {
            if (method.parameterTypes.size == 1 && method.isAnnotationPresent(EventTarget::class.java)) {
                method.isAccessible = true
                @Suppress("UNCHECKED_CAST")
                val listener = method.parameterTypes[0] as Class<out EventBase>

                val methodBean = MethodData(o, method)

                if (events.containsKey(listener)) {
                    if (!events[listener]!!.contains(methodBean)) {
                        events[listener]!!.add(methodBean)
                    }
                } else {
                    events[listener] = CopyOnWriteArrayList(Collections.singletonList(methodBean))
                }
            }
        }
    }

    fun unregister(o: Any) {
        events.values.forEach { methodData ->
            methodData.removeIf { method -> method.targetObject == o }
        }
        events.entries.removeIf { event -> event.value.isEmpty() }
    }

    fun getEvent(type: Class<out EventBase>): CopyOnWriteArrayList<MethodData>? {
        return events[type]
    }


}