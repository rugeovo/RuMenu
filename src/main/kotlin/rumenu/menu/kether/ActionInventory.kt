package rumenu.menu.kether

import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser

object ActionInventory {

    @KetherParser(["get-inventory"], shared = false)
    fun getInventory() = combinationParser {
        it.group(type<String>()).apply(it) { name ->
            val player = Bukkit.getPlayer(name)
            now {
                player?.inventory
            }
        }
    }

    @KetherParser(["get-inventory-item"], shared = false)
    fun getInventoryItem() = combinationParser {
        it.group(type<Inventory>(),type<Int>()).apply(it) { inv, slot ->
            now {
                inv.getItem(slot)
            }
        }
    }

    @KetherParser(["set-inventory-item"], shared = false)
    fun setInventoryItem() = combinationParser {
        it.group(type<Inventory>(),type<Int>(), type<ItemStack>()).apply(it) { inv, slot, item ->
            now {
                inv.setItem(slot, item)
            }
        }
    }

}