package rumenu.events

import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack
import rumenu.cahe.OpenMenuCache.byUUidremove
import rumenu.cahe.OpenMenuCache.openMenu
import rumenu.profile.File.menuFiles
import rumenu.profile.FileConfig.config
import rumenu.utility.ItemsUtility.createMenuItem
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.adaptCommandSender
import taboolib.module.kether.KetherShell
import taboolib.module.kether.ScriptOptions


object PlayerEvents {

    @SubscribeEvent
    fun playerQuitEvent(event: PlayerQuitEvent) {
        byUUidremove(event.player.uniqueId)
    }

    @SubscribeEvent
    fun playerSwapHandItemsEvent(event: PlayerSwapHandItemsEvent) {
        val player = event.player
        if (player.isSneaking) {
            KetherShell.eval(
                config.getString("Shortcuts.Sneaking-Offhand") ?:"",options = ScriptOptions(
                    sender = adaptCommandSender(player)
                )
            )
        }
    }

}