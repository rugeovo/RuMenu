package rumenu.menu.kether

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser
import taboolib.module.kether.player
import taboolib.platform.util.buildItem

object Item {

    @KetherParser(["get-item-amount"], shared = false)
    fun getItemAmount() = combinationParser {
        it.group(type<ItemStack>()).apply(it) { item ->
            now {
                item.amount
            }
        }
    }

    @KetherParser(["set-item-amount"], shared = false)
    fun setItemAmount() = combinationParser {
        it.group(type<ItemStack>(),type<Int>()).apply(it) { item,num ->
            now {
                item.amount = num
            }
        }
    }

    @KetherParser(["get-itemstack"], shared = false)
    fun getItemStack() = combinationParser {
        it.group(type<String>()).apply(it) { material ->
            now {
                buildItem(Material.valueOf(material.uppercase()))
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