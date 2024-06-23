package we.kotlinl0vers.module

import we.kotlinl0vers.module.elements.HUD

object ModuleManager {
    //use list to store modules
    val modules = mutableListOf<BaseModule>()

    fun addModules(){
        modules.add(HUD())
    }

    //get module by name
    fun getModule(name: String): BaseModule? {
        return modules.find { it.name == name }
    }

    //get module by class
    fun <T : BaseModule> getModule(clazz: Class<T>): T? {
        return modules.find { it.javaClass == clazz } as T?
    }

    //get all enabled modules
    fun getEnabledModules(): List<BaseModule> {
        return modules.filter { it.enabled }
    }
}