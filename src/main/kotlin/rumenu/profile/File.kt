package rumenu.profile

import rumenu.cahe.OpenMenuCache
import rumenu.profile.FileConfig.consoleMessage
import rumenu.utility.Files.unwatch
import rumenu.utility.Files.watch
import rumenu.utility.deepRead
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.releaseResourceFile
import taboolib.module.configuration.Configuration
import java.io.File
import java.util.concurrent.ConcurrentHashMap

object File {

    // 使用 lazy 初始化 menuFiles
    val menuFiles: ConcurrentHashMap<String, Configuration> = ConcurrentHashMap()

    init {
        turntableFile()
    }

    fun turntableFile() {
        val dataFolder = getDataFolder()
        val targetDir = File(dataFolder, "menus").apply {
            if (!exists()) {
                mkdirs()
                // 如果目录不存在，释放默认的 menu.yml 文件
                menuFiles["menu"] = loadConfiguration(releaseResourceFile("menus/menu.yml"))
            }
        }

        targetDir.deepRead("yml").forEach { file ->
            file.unwatch()
            val name = file.nameWithoutExtension
            menuFiles[name] = loadConfiguration(file)
            file.watch {
                if (file.exists()) { // 检查文件是否存在
                    menuFiles[name] = loadConfiguration(file)
                    OpenMenuCache.deleteMenu()
                    consoleMessage(name,LangText.reload)
                } else {
                    // 如果文件不存在，移除监听器
                    file.unwatch()
                    menuFiles.remove(name)
                    consoleMessage(name,LangText.delete)
                }
            }
        }
    }

    // 提取重复的配置加载逻辑
    private fun loadConfiguration(file: File): Configuration {
        return Configuration.loadFromFile(file)
    }
}