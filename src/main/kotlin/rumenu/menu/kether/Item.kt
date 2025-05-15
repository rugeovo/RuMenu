package rumenu.menu.kether

import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import rumenu.cahe.OpenMenuCache.openCacheMenu
import rumenu.profile.File.menuFiles
import rumenu.utility.ItemsUtility.createMenuItem
import taboolib.common.platform.function.info
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

    @KetherParser(["remove-item-amount"], shared = false)
    fun removeItemAmount() = combinationParser {
        it.group(type<ItemStack>(),type<Int>()).apply(it) { item,num ->
            now {
                item.amount -= num
            }
        }
    }

    @KetherParser(["add-item-amount"], shared = false)
    fun addItemAmount() = combinationParser {
        it.group(type<ItemStack>(),type<Int>()).apply(it) { item,num ->
            now {
                item.amount += num
            }
        }
    }

    @KetherParser(["get-item-cmd"], shared = false)
    fun getItemCustomModelData() = combinationParser {
        it.group(type<ItemStack>()).apply(it) { item ->
            now {
                item.itemMeta?.takeIf { meta -> meta.hasCustomModelData() }?.customModelData ?: "null"
            }
        }
    }

    @KetherParser(["get-itemstack"], shared = false)
    fun getItemStack() = combinationParser {
        it.group(type<String>()).apply(it) { node ->
            now {
                val uuid = player().uniqueId
                val player = Bukkit.getPlayer(uuid) ?: return@now null
                val menuFile = openCacheMenu[uuid]?.menufile ?: return@now null
                val display = menuFile.getConfigurationSection(node) ?: return@now null
                val item = createMenuItem(player, Material.STONE,display.toMap())
                return@now item
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

    @KetherParser(["clone-item"], shared = false)
    fun cloneItem() = combinationParser {
        it.group(type<ItemStack>()).apply(it) { item ->
            now {
                item.clone()
            }
        }
    }

    @KetherParser(["get-item-meta"], shared = false)
    fun getItemMeta() = combinationParser {
        it.group(type<ItemStack>()).apply(it) { item ->
            now {
                item.itemMeta
            }
        }
    }

    @KetherParser(["set-item-meta"], shared = false)
    fun setItemMeta() = combinationParser {
        it.group(type<ItemStack>(), type<ItemMeta>()).apply(it) { item, meta ->
            now {
                item.setItemMeta(meta)
            }
        }
    }

    @KetherParser(["set-meta-cmd"], shared = false)
    fun setItemCustomModelData() = combinationParser {
        it.group(type<ItemMeta>(),type<Int>()).apply(it) { item,num ->
            now {
                item?.setCustomModelData(num)
            }
        }
    }

    @KetherParser(["set-meta-color"], shared = false)
    fun setItemColor() = combinationParser {
        it.group(type<ItemMeta>(),type<String>()).apply(it) { item,color ->
            now {
                item as LeatherArmorMeta
                if (color.startsWith("#") && color.length == 7) {
                    // 从第1位到第6位提取颜色值
                    val r = color.substring(1, 3).toInt(16) // 红色通道
                    val g = color.substring(3, 5).toInt(16) // 绿色通道
                    val b = color.substring(5, 7).toInt(16) // 蓝色通道
                    item.setColor(Color.fromRGB(r, g, b))
                    info("aaa")
                } else {
                    throw IllegalArgumentException("Invalid hex color format. Expected format: #RRGGBB")
                }
            }
        }
    }

    @KetherParser(["clear-meta-cmd"], shared = false)
    fun clearItemCustomModelData() = combinationParser {
        it.group(type<ItemMeta>()).apply(it) { item ->
            now {
                item?.setCustomModelData(null)
            }
        }
    }

    @KetherParser(["clone-meta"], shared = false)
    fun cloneMeta() = combinationParser {
        it.group(type<ItemMeta>()).apply(it) { meta ->
            now {
                meta.clone()
            }
        }
    }

}