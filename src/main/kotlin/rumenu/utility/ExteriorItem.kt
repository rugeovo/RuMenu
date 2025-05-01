package rumenu.utility

import dev.lone.itemsadder.api.CustomStack
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.platform.util.buildItem

object ExteriorItem {

    fun getIntroducedItem(str: String): ItemStack {
        val array = str.split(":")
        return when (array[0]) {
            "IA" -> {
                CustomStack.getInstance(array[1])?.itemStack
            }
            else -> buildItem(Material.STONE)
        } ?: buildItem(Material.STONE)
    }

}