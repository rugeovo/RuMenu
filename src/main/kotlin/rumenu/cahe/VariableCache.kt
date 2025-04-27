package rumenu.cahe

import rumenu.profile.FileConfig.variableTable
import java.util.concurrent.ConcurrentHashMap

class VariableCache {

    private var nestedMap: ConcurrentHashMap<String, ConcurrentHashMap<String, String>> = ConcurrentHashMap()

    fun get(uuid: String,key:String): String {
        return nestedMap[uuid]?.get(key) ?: variableTable.get(uuid,key)
    }

    fun set(uuid: String,key: String, value: String) {
        nestedMap[uuid]?.put(key, value)
    }

    fun remove(uuid: String,key: String) {
        nestedMap[uuid]?.remove(key)
    }

}