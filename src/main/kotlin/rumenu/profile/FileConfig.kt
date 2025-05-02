package rumenu.profile

import org.bukkit.entity.Player
import rumenu.database.VariableTable
import rumenu.cahe.VariableCache
import rumenu.profile.File.menuFiles
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.console
import taboolib.module.chat.colored
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile

object FileConfig {

    @Config("config.yml", autoReload = true)
    lateinit var config: ConfigFile

    @Config("lang.yml", autoReload = true)
    lateinit var lang: ConfigFile

    lateinit var variableTable: VariableTable
    lateinit var variableCache: VariableCache

    @Awake(LifeCycle.ACTIVE)
    fun autoReload(){
        config.onReload {
            consoleMessage("config.yml",LangText.filereload)
        }
        lang.onReload{
            consoleMessage("lang.yml",LangText.filereload)
        }
    }

    fun consoleMessage(fileName: String,text: String) {
        console().sendMessage(text.
        replace("{prefix}", LangText.prefix).
        replace("{file}", fileName).
        replace("{num}", "${menuFiles.keys.size}").
        colored())
    }

    fun playerMessage(player: Player,text: String) {
        player.sendMessage(text.
        replace("{prefix}", LangText.prefix).
        replace("{num}", "${menuFiles.keys.size}").
        replace("{give}", player.name).
        colored())
    }


}