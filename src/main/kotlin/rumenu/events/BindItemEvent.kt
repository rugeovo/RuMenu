package rumenu.events

import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import rumenu.cahe.OpenMenuCache.openMenu
import rumenu.profile.File.menuFiles
import rumenu.utility.ItemsUtility.createMenuItem
import taboolib.common.platform.event.SubscribeEvent

object BindItemEvent {

    private val menuItemCache = mutableMapOf<ItemStack, String>()

    @SubscribeEvent
    fun openMenuBindItem(event: PlayerInteractEvent) {
        if (!event.action.toString().contains("RIGHT")) {
            return
        }
        val player = event.player
        val itemInHand: ItemStack = player.inventory.itemInMainHand
        val menuname = menuItemCache[itemInHand]
        if (menuname != null) {
            openMenu(player,menuname)
        } else {
            menuFiles.forEach { (yname,file) ->
                file.getConfigurationSection("bind.display")?.let {
                    val item = createMenuItem(player, Material.STONE, it.toMap())
                    if (item.isSimilar(itemInHand)){
                        openMenu(player,yname)
                        menuItemCache[item] = yname
                    }
                }
            }
        }
    }

    fun clearCache() {
        menuItemCache.clear()
    }
}