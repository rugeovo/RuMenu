package rumenu.menu

import taboolib.common.platform.function.submit
import taboolib.common.platform.function.submitAsync
import taboolib.common.platform.service.PlatformExecutor

class Titles(private val titles: List<String>, private val period: Long) {

    private var titleUpdateTask: PlatformExecutor.PlatformTask? = null
    private var currentIndex = 0

    /**
     * 开始轮换更新标题
     * 需要更新标题的菜单对象
     */
    fun startTitleRotation(updateTitle: (String) -> Unit) {
        submitAsync {
            titleUpdateTask = submit(period = period, async = false) {
                updateTitle(titles[currentIndex])
                currentIndex = (currentIndex + 1) % titles.size // 循环更新标题
            }
        }
    }

    /**
     * 取消标题轮换任务
     */
    fun cancel() {
        titleUpdateTask?.cancel()
        titleUpdateTask = null
    }
}