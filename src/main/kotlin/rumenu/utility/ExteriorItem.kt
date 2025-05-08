package rumenu.utility

import dev.lone.itemsadder.api.CustomStack
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem.getById
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.platform.util.buildItem

object ExteriorItem {

    fun getIntroducedItem(str: String): ItemStack? {
        val array = str.split(":")
        return when (array[0]) {
            "IA" -> {
                CustomStack.getInstance(array[1])?.itemStack
            }
            "SF" -> {
                getById(array[1])?.item
            }
            else -> buildItem(Material.STONE)
        }
    }

}