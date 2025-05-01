package rumenu.menu

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import rumenu.utility.ItemsUtility.createMenuItem
import taboolib.common.platform.function.adaptCommandSender
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
     * 菜单的标题文本（支持颜色代码和占位符替换）。
     * - 从配置文件 `menufile` 中读取 `title` 字段并替换占位符。
     * - 如果未配置或解析失败，默认返回 `"chest"`。
     */
    val titleText = menufile.getString("title")?.replacePlaceholder(player)?.colored() ?: "chest"
    /**
     * 菜单的布局结构（二维字符数组形式）。
     * - 从配置文件 `menufile` 中读取 `layout` 字段（每行字符串代表一行菜单布局）。
     * - 示例：`["abc", "def"]` 表示 2 行 3 列的菜单。
     */
    val layout = menufile.getStringList("layout")

    /**
     * 菜单打开时执行的 Kether 脚本（可选）。
     * - 从配置文件 `menufile` 中读取 `events.open` 字段。
     * - 如果未配置，默认为空字符串（不执行任何脚本）。
     */
    val openKether = menufile.getString("events.open") ?: ""

    /**
     * 菜单关闭时执行的 Kether 脚本（可选）。
     * - 从配置文件 `menufile` 中读取 `events.close` 字段。
     * - 如果未配置，默认为空字符串（不执行任何脚本）。
     */
    val closeKether = menufile.getString("events.close") ?: ""

    /**
     * 菜单图标配置容器。
     * - 用于存储和管理菜单中每个槽位（Slot）对应的图标（ItemStack）。
     * - 具体图标数据通常通过其他方法（如 `icons.set(slot, item)`）动态加载。
     */
    val icons = Icons()

    /**
     * 是否取消菜单点击事件的默认行为（如物品拖拽、移动等）。
     * - 默认为 `true`（禁止玩家操作菜单物品）。
     * - 设置为 `false` 时允许玩家与菜单交互（需谨慎使用）。
     */
    var clickCancell = true

    val tasksKether = menufile.getString("tasks.script") ?: ""
    val tasksUpdate = menufile.getLong("tasks.update")

    var tasks: PlatformExecutor.PlatformTask? = null


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
                /**
                 * 关闭菜单的 kether
                 */
                KetherShell.eval(
                    closeKether, options = ScriptOptions(
                        sender = adaptCommandSender(player)
                    )
                )
                /**
                 * 关闭周期任务
                 */
                tasks?.cancel()
                tasks = null
            }
            onBuild { _, _ ->

                //打开菜单后的 kether
                KetherShell.eval(
                    openKether, options = ScriptOptions(
                        sender = adaptCommandSender(player)
                    )
                )

                tasks = submit(period = tasksUpdate, async = false, delay = 20) {
                    if (player.isOnline) {
                        KetherShell.eval(
                            tasksKether, options = ScriptOptions(
                                sender = adaptCommandSender(player)
                            )
                        )
                    } else{
                        cancel()
                    }
                }
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
                    if(player.isOnline) {
                        slots.forEach { slot ->
                            val item = createMenuItem(player, Material.STONE, display)
                            inventory.setItem(slot, item)
                        }
                    }else {
                        cancel()
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