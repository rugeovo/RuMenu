package rumenu.database

import taboolib.expansion.Length
import taboolib.expansion.persistentContainer
import rumenu.profile.FileConfig.variableCache

data class VariableEntity (
    val uuid: String,
    val key:String,
    @Length(-1)
    val value:String
)
object VariableTable {

    val tableName = "rumenu"
    private val container by lazy {
        persistentContainer { new<VariableEntity>(tableName) }
    }
    private val containerx = container[tableName]

    fun get(uuid: String, key: String): String {
        return containerx.getOne<VariableEntity>{
            "uuid" eq uuid
            "key" eq key
        }?.value ?: "0".also { entity ->
            entity.let { variableCache.set(uuid,key, it) }
        }
    }

    fun add(uuid: String, key: String, value: String) {
        containerx.insert(listOf(VariableEntity(uuid, key, value)))
    }

    fun set(uuid: String, key: String, value: String) {
        val tablex = containerx.table
        val dataSource = containerx.dataSource
        tablex.update(dataSource) {
            where( "uuid" eq uuid and ("key" eq key))
            set("value",value)
        }.also { num ->
            if (num == 0){
                add(uuid, key, value)
            }
        }

        variableCache.remove(uuid,key)
    }

    fun remove(uuid: String, key: String) {
        val tablex = containerx.table
        val dataSource = containerx.dataSource
        tablex.delete(dataSource) {
            "uuid" eq uuid
            "key" eq key
        }.also {
            variableCache.remove(uuid,key)
        }
    }
}
