package rumenu.command

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import rumenu.cahe.OpenMenuCache.openMenu
import rumenu.profile.File
import rumenu.profile.File.menuFiles
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

    @CommandBody(permission = "rumenu.reload")
    val reload = subCommand {
        execute<CommandSender>{ _, _, _ ->
            File.turntableFile()
        }
    }

}