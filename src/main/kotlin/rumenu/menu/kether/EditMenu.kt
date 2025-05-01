package rumenu.menu.kether

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import rumenu.cahe.OpenMenuCache.openCacheMenu
import rumenu.menu.MenuUtiliy.findIndicesOfString
import taboolib.common.platform.function.info
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser
import taboolib.module.kether.player
import taboolib.platform.util.buildItem

object EditMenu {

    @KetherParser(["set-item"], shared = false)
    fun setItem() = combinationParser {
        it.group(type<Int>(), type<ItemStack>()).apply(it) { slot,item ->
            now {
                val uuid = player().uniqueId
                openCacheMenu[uuid]?.inventory?.also { item1 ->
                    item1.setItem(slot,item)
                }
            }
        }
    }

    @KetherParser(["clear-item"], shared = false)
    fun clearItem() = combinationParser {
        it.group(type<Int>()).apply(it) { slot ->
            now {
                val uuid = player().uniqueId
                openCacheMenu[uuid]?.inventory?.also { item1 ->
                    item1.setItem(slot, buildItem(Material.AIR))
                }
            }
        }
    }

    @KetherParser(["get-item"], shared = false)
    fun getItem() = combinationParser {
        it.group(type<Int>()).apply(it) { slot ->
            now {
                val uuid = player().uniqueId
                val inventory = openCacheMenu[uuid]?.inventory
                if (inventory != null) {
                    return@now inventory.getItem(slot)
                } else {
                    return@now null
                }
            }
        }
    }

    @KetherParser(["get-slots"], shared = false)
    fun getSlots() = combinationParser {
        it.group(type<String>()).apply(it) { char ->
            now {
                val uuid = player().uniqueId
                return@now openCacheMenu[uuid]?.layout?.let { it1 ->
                    findIndicesOfString(char, it1)
                }
            }
        }
    }

    @KetherParser(["set-click-cancel"], shared = false)
    fun setClickCancell() = combinationParser {
        it.group(type<Boolean>()).apply(it) { state ->
            now {
                val uuid = player().uniqueId
                openCacheMenu[uuid]?.clickCancell = state
            }
        }
    }

}