package rumenu.menu.kether

import org.bukkit.Bukkit
import taboolib.common.platform.function.adaptCommandSender
import taboolib.module.kether.*

object ActionKether {

    @KetherParser(["run-kether"], shared = false)
    fun runKether() = combinationParser {
        it.group(type<String>()).apply(it) { script ->
            var state = false
            now {
                KetherShell.eval(
                    script, options = ScriptOptions(
                        sender = Bukkit.getPlayer(player().uniqueId)?.let {
                            it1 -> adaptCommandSender(it1)
                        }
                    )
                ).thenAccept{ result ->
                    state = result.toString().toBoolean()
                }
                return@now state
            }
        }
    }



}