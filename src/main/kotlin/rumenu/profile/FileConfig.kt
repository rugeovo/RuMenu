package rumenu.profile

import rumenu.database.VariableTable
import rumenu.cahe.VariableCache
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
            consoleMessage("config.yml",LangText.reload)
        }
        lang.onReload{
            consoleMessage("lang.yml",LangText.reload)
        }
    }

    fun consoleMessage(fileName: String,text: String) {
        console().sendMessage(text.
        replace("{prefix}", LangText.prefix).
        replace("{file}", fileName).
        colored())
    }

}