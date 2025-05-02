package rumenu.command

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import rumenu.cahe.OpenMenuCache.openMenu
import rumenu.profile.File
import rumenu.profile.File.menuFiles
import rumenu.profile.FileConfig
import rumenu.profile.FileConfig.consoleMessage
import rumenu.profile.FileConfig.playerMessage
import rumenu.profile.LangText
import rumenu.utility.ItemsUtility.createMenuItem
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.player
import taboolib.common.platform.command.subCommand

@CommandHeader(name = "rumenu", aliases = ["rm"], permission = "rumenu.main")
object MainCommand {

    @CommandBody(permission = "rumenu.open")
    val open = subCommand {
        dynamic(optional = true, comment = "menu") {
            suggestion<CommandSender> { _, _ ->
                menuFiles.keys.toList()
            }
            player("player"){
                execute<CommandSender> { _, context, _ ->
                    val play = Bukkit.getPlayer(context["player"])
                    play?.let { openMenu(it,context["menu"]) }
                }
            }
            execute<Player>{ sender, context, _ ->
                openMenu(sender, context["menu"])
            }
        }
    }

    @CommandBody(permission = "rumenu.give")
    val give = subCommand {
        dynamic(optional = true, comment = "menu") {
            suggestion<CommandSender> { _, _ ->
                menuFiles.keys.toList()
            }
            player("player"){
                execute<CommandSender> { _, context, _ ->
                    val play = Bukkit.getPlayer(context["player"])
                    if (play != null) {
                        val inventory = play.inventory
                        val menufile = menuFiles[context["menu"]]
                        if (menufile != null) {
                            menufile.getConfigurationSection("bind.display")?.let {
                                inventory.addItem(createMenuItem(play,Material.STONE, it.toMap()))
                            }
                        }
                    }
                }
            }
            execute<Player>{ sender, context, _ ->
                val menufile = menuFiles[context["menu"]]
                if (menufile != null) {
                    sender.inventory.addItem(menufile.getConfigurationSection("bind.display")?.let {
                        createMenuItem(sender,Material.STONE, it.toMap())
                    })
                }
            }
        }
    }

    @CommandBody(permission = "rumenu.reload")
    val reload = subCommand {
        execute<CommandSender>{ _, _, _ ->
            File.turntableFile()
            File.createOpenMenuCmd()
            consoleMessage("",LangText.reload)
        }
        execute<Player>{ sender, _, _ ->
            File.turntableFile()
            File.createOpenMenuCmd()
            playerMessage(sender,LangText.reload)
        }
    }

}