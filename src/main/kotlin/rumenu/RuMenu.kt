package rumenu

import rumenu.cahe.VariableCache
import rumenu.database.VariableTable
import rumenu.profile.File.turntableFile
import rumenu.profile.FileConfig.variableCache
import rumenu.profile.FileConfig.variableTable
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.info

object RuMenu : Plugin() {

    // 项目使用TabooLib Start Jar 创建!
    override fun onActive() {
        variableTable = VariableTable
        variableCache = VariableCache()
        turntableFile()
    }

}