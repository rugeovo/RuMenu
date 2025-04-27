package rumenu.menu.kether

import org.bukkit.Bukkit
import rumenu.cahe.OpenMenuCache
import rumenu.cahe.OpenMenuCache.openCacheMenu
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser
import taboolib.module.kether.player

object ActionMenu {

    @KetherParser(["open-menu"], shared = false)
    fun openMenu() = combinationParser {
        it.group(type<String>()).apply(it) { menu ->
            now {
                val player = Bukkit.getPlayer(player().uniqueId)
                player?.let { it1 ->
                    OpenMenuCache.openMenu(it1,menu)
                }
            }
        }
    }

    @KetherParser(["close-menu"], shared = false)
    fun closeMenu() = combinationParser {
        it.group(type<String>()).apply(it) { menu ->
            now {
                val uuid = player().uniqueId
                openCacheMenu[uuid]?.closeInventory()
            }
        }
    }
}