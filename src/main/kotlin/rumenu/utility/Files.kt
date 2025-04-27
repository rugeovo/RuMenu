package rumenu.utility

import taboolib.common5.FileWatcher
import java.io.File

/**
 * 嵌套读取文件夹内的所有指定后缀名的文件
 */
fun File.deepRead(extension: String): List<File> {
    val files = mutableListOf<File>()
    listFiles()?.forEach {
        if (it.isDirectory) {
            files.addAll(it.deepRead(extension))
        } else if (it.extension == extension) {
            files.add(it)
        }
    }
    return files
}

object Files {
    private val fileWatcher = FileWatcher.INSTANCE
    private val fileListeners = LinkedHashSet<File>()
    private val watching = LinkedHashSet<File>()
    /**
     * 监听文件改动
     */
    fun File.watch(callback: (File) -> Unit) {
        if (!hasListener) {
            fileWatcher.addSimpleListener(this) {
                callback(this)
            }
            hasListener = true
        }
    }
    /**
     * 取消监听
     */
    fun File.unwatch() {
        if (hasListener) {
            fileWatcher.removeListener(this)
            hasListener = false
        }
    }
    /**
     * 检测文件是否正在被监听器处理
     */
    var File.isProcessingByWatcher: Boolean
        get() = this in watching && hasListener
        set(value) {
            if (value) watching += this else watching -= this
        }

    private var File.hasListener: Boolean
        get() = this in fileListeners
        set(value) {
            if (value) fileListeners += this else fileListeners -= this
        }
}