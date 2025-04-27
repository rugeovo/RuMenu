package rumenu.utility

import com.google.gson.Gson
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.platform.compat.replacePlaceholder
import taboolib.platform.util.*
import java.util.*


object ItemsUtility {

    //菜单物品的创建函数
    fun createMenuItem(player: Player,item: ItemStack,map: Map<String, Any?>): ItemStack {
        return buildItem(item){

            applyMapToBuilder(this,parseStringToMap(map,player))
            colored()
            
        }
    }
    //菜单物品的创建函数
    fun createMenuItem(player: Player,material: Material,map: Map<String, Any?>): ItemStack {
        return buildItem(material){
            applyMapToBuilder(this,parseStringToMap(map,player))
            colored()
        }
    }

    //菜单物品的构建函数
    @Suppress("UNCHECKED_CAST")
    private fun applyMapToBuilder(builder: ItemBuilder, map: Map<String, Any?>) {
        for ((key, value) in map) {
            when (key) {
                "material" -> builder.material = Material.valueOf((value as String ).uppercase(Locale.getDefault()))
                "name" -> builder.name = value as String
                "lore" -> setLore(builder, value as List<String>)
                "customModelData" -> builder.customModelData = value.toString().toInt()
                "unbreakable" -> builder.isUnbreakable = value as? Boolean ?: false
                "enchants" -> setEnchants(builder, value as Map<String, Any>)
                "texture" -> setTexture(builder,value as Map<String, Any>)
                "color" -> setColor(builder,value)
                "shiny" -> setShiny(builder,value)
            }
        }
    }

    private fun setLore(builder: ItemBuilder, itemlore: List<String>) {
        builder.lore.clear()  // 清空现有的lore
        builder.lore.addAll(itemlore)  // 添加新的lore
    }

    //附魔设置
    private fun setEnchants(builder: ItemBuilder,enchantments: Map<String, Any>){
        for ((enchantName, level) in enchantments) {
            val enchantment = Enchantment.getByName(enchantName)
            if (enchantment != null) {
                builder.enchants[enchantment] = level as Int
            }
        }
    }

    //头颅材质设置
    private fun setTexture(builder: ItemBuilder, head:Map<String, Any>) {
        for ((key, value) in head) {
            when (key) {
                "name" -> builder.skullOwner = value as? String ?: continue
                "value" -> builder.skullTexture = SkullTexture(value.toString(), UUID.randomUUID())
            }
        }
    }

    //颜色设置 @000000 16进制转rgb 用于设置皮革颜色
    private fun setColor(builder: ItemBuilder,colora: Any?) {
        val color = colora.toString()
        if (color.startsWith("#") && color.length == 7) {
            // 从第1位到第6位提取颜色值
            val r = color.substring(1, 3).toInt(16) // 红色通道
            val g = color.substring(3, 5).toInt(16) // 绿色通道
            val b = color.substring(5, 7).toInt(16) // 蓝色通道
            builder.color = Color.fromRGB(r, g, b)
        } else {
            throw IllegalArgumentException("Invalid hex color format. Expected format: #RRGGBB")
        }
    }

    //设置发光
    private fun setShiny(builder: ItemBuilder,selecta: Any?) {
        val select = selecta.toString()
        if (select.toBoolean()){
            builder.shiny()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun parseStringToMap(map: Map<String, Any?>, player: Player): Map<String, Any?> {
        val gson = Gson()
        val jsonString = gson.toJson(map).replacePlaceholder(player)
        return gson.fromJson(jsonString, Map::class.java) as Map<String, Any?>
    }

    //序列化
    fun ItemStack.itemstackToString(): String {
        return Base64.getEncoder().encodeToString(serializeToByteArray())
    }

    //反序列化
    fun String.stringToItemstack(): ItemStack {
        return Base64.getDecoder().decode(this).deserializeToItemStack()
    }
}