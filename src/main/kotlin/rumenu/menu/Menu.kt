package rumenu.menu

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import rumenu.utility.ItemsUtility.createMenuItem
import taboolib.common.platform.function.adaptCommandSender
import taboolib.common.platform.function.info
import taboolib.common.platform.function.submit
import taboolib.common.platform.function.submitAsync
import taboolib.common.platform.service.PlatformExecutor
import taboolib.module.chat.colored
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.util.getMap
import taboolib.module.kether.KetherShell
import taboolib.module.kether.ScriptOptions
import taboolib.module.ui.buildMenu
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Chest
import taboolib.platform.compat.replacePlaceholder
import taboolib.platform.util.buildItem

class Menu(val player: Player, val menufile: Configuration) {

    /**
     * 获取到的 titleText
     */
    private val titleText = menufile.getString("title")?.replacePlaceholder(player)?.colored() ?: "chest"

    /**
     * 菜单的布局结构
     */
    val layout = menufile.getStringList("layout")

    val openKether = menufile.getString("events.open") ?: ""

    val closeKether = menufile.getString("events.close") ?: ""

    val icons = Icons()

    var clickCancell = true

    /**
     * 构建的菜单
     */
    var inventory: Inventory = buildMenu<Chest>(titleText) {
        map(*layout.toTypedArray())
    }

    fun closeInventory() {
        player.closeInventory()
    }

    /**
     * 为玩家打开菜单
     */
    fun openInventory() {
        var items = mutableListOf<ItemA>()
        /**
         * 开启菜单
         */
        inventory = buildMenu<Chest>(titleText) {
            map(*layout.toTypedArray())
            onClose {
                /**
                 * 关闭物品的刷新
                 */
                icons.cancelItems()
                KetherShell.eval(
                    closeKether, options = ScriptOptions(
                        sender = adaptCommandSender(player)
                    )
                )
            }
            onBuild { _, _ ->
                //打开后执行 open 事件
                KetherShell.eval(
                    openKether, options = ScriptOptions(
                        sender = adaptCommandSender(player)
                    )
                )
            }
            onClick { event ->
                event.isCancelled = clickCancell
            }
            menufile.getConfigurationSection("icons")?.getKeys(false)?.forEach { char ->
                var update = menufile.getLong("icons.$char.update")
                var display = menufile.getConfigurationSection("icons.$char.display")?.toMap()
                var actions = menufile.getConfigurationSection("icons.$char.actions")
                val iconsSection = menufile.getConfigurationSection("icons.$char.icons")
                iconsSection?.getKeys(false)?.sorted()?.forEach { icona ->
                    val kethes = KetherShell.eval(
                        iconsSection.getString("$icona.condition") ?: "", options = ScriptOptions(
                            sender = adaptCommandSender(player)
                        )
                    )
                    kethes.thenAccept { result ->
                        if (result.toString().toBoolean()) {
                            update = iconsSection.getLong("$icona.update")
                            display = iconsSection.getMap("$icona.display")
                            actions = iconsSection.getConfigurationSection("$icona.actions")
                        }
                    }
                }
                display?.let {
                    val itemstack = createMenuItem(player, Material.STONE, it)
                    if (update > 0) {
                        items.add(ItemA(char, update, display!!, getSlots(char.toCharArray()[0])))
                    }
                    set(char.toCharArray()[0], buildItem(itemstack)) {
                        if (clickEvent().isLeftClick) {
                            KetherShell.eval(
                                actions?.getString("left") ?: "", options = ScriptOptions(
                                    sender = adaptCommandSender(player)
                                )
                            )
                        } else if (clickEvent().isRightClick) {
                            KetherShell.eval(
                                actions?.getString("right") ?: "", options = ScriptOptions(
                                    sender = adaptCommandSender(player)
                                )
                            )
                        } else if (clickEvent().isShiftClick) {
                            KetherShell.eval(
                                actions?.getString("shift-click") ?: "", options = ScriptOptions(
                                    sender = adaptCommandSender(player)
                                )
                            )
                        }
                    }
                }
            }
        }
        player.openMenu(inventory)

        items.forEach { item ->
            icons.addItem(Item(item.char, item.update, item.display, item.slots))
        }

        /**
         * 开启物品的刷新
         */
        icons.updateItems()
    }

    inner class Item(val char: String, val update: Long, val display: Map<String, Any?>, val slots: List<Int>) {

        private var itemUpdateTask: PlatformExecutor.PlatformTask? = null

        fun updateItem() {
            submitAsync {
                itemUpdateTask = submit(period = update, async = false) {
                    slots.forEach { slot ->
                        val item = createMenuItem(player, Material.STONE, display)
                        inventory.setItem(slot, item)
                    }
                }
            }
        }

        fun cancel() {
            itemUpdateTask?.cancel()
            itemUpdateTask = null
        }
    }
}