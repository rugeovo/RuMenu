package rumenu.menu.kether

import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser
import taboolib.module.kether.player

object Item {

    @KetherParser(["get-item-amount"], shared = false)
    fun getItemAmount() = combinationParser {
        it.group(type<ItemStack>()).apply(it) { item ->
            now {
                item.amount
            }
        }
    }

    @KetherParser(["give-item"], shared = false)
    fun giveItem() = combinationParser {
        it.group(type<ItemStack>()).apply(it) { item ->
            now {
                val player = Bukkit.getPlayer(player().uniqueId)
                player?.inventory?.addItem(item)
            }
        }
    }

}