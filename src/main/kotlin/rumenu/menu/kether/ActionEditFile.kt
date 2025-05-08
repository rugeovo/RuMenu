package rumenu.menu.kether

import org.bukkit.Bukkit
import rumenu.cahe.OpenMenuCache
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser
import taboolib.module.kether.player

object ActionEditFile {

    @KetherParser(["get-string"], shared = false)
    fun getString() = combinationParser {
        it.group(type<String>()).apply(it) { node ->
            now {
                val menu = OpenMenuCache.openCacheMenu[player().uniqueId]
                return@now menu?.menufile?.getString(node)
            }
        }
    }

    @KetherParser(["get-string-list"], shared = false)
    fun getStringList() = combinationParser {
        it.group(type<String>()).apply(it) { node ->
            now {
                val menu = OpenMenuCache.openCacheMenu[player().uniqueId]
                return@now menu?.menufile?.getStringList(node)
            }
        }
    }

}