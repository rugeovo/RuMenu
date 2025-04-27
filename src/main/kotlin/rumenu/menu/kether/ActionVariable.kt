package rumenu.menu.kether

import rumenu.profile.FileConfig.variableCache
import rumenu.profile.FileConfig.variableTable
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser
import taboolib.module.kether.player

object ActionVariable {

    @KetherParser(["rm-get"], shared = false)
    fun get() = combinationParser {
        it.group(type<String>()).apply(it) { key ->
            now {
                variableCache.get(player().uniqueId.toString(),key)
            }
        }
    }

    @KetherParser(["rm-set"], shared = false)
    fun set() = combinationParser {
        it.group(type<String>(),type<String>()).apply(it) { key,value ->
            now {
                 variableTable.set(player().uniqueId.toString(),key,value)
            }
        }
    }

    @KetherParser(["rm-add"], shared = false)
    fun add() = combinationParser {
        it.group(type<String>(),type<String>()).apply(it) { key,value ->
            now {
                variableTable.add(player().uniqueId.toString(),key,value)
            }
        }
    }

}