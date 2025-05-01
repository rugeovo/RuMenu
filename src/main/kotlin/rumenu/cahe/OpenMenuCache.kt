package rumenu.cahe

import org.bukkit.entity.Player
import rumenu.menu.Menu
import rumenu.profile.File.menuFiles
import taboolib.common.platform.function.info
import java.util.*

object OpenMenuCache {
    var cacheMap: MutableMap<UUID, MutableMap<String, Menu>> = mutableMapOf()

    //用来缓存执行kether的对象
    var openCacheMenu: MutableMap<UUID, Menu> = mutableMapOf()

    fun openMenu(player: Player, ymlname: String) {
        val uuid = player.uniqueId
        val menuConfig = menuFiles[ymlname]
        val menu = cacheMap[uuid]?.get(ymlname)

        if (menu != null) {
            menu.openInventory()
            openCacheMenu[uuid] = menu
            return
        }

        val newMenu = menuConfig?.let { Menu(player, it) }
        if (newMenu != null) {
            // 确保 cacheMap[uuid] 存在
            cacheMap.computeIfAbsent(uuid) { mutableMapOf() }[ymlname] = newMenu
            newMenu.openInventory()
            openCacheMenu[uuid] = newMenu
        }
    }

    fun byUUidremove(uuid: UUID) {
        cacheMap.remove(uuid)
    }

    fun deleteMenu() {
        cacheMap.clear()
    }
}