package rumenu.profile

import rumenu.profile.FileConfig.lang

object LangText {
    var prefix = lang.getString("prefix")?:"&x&4&8&6&f&f&b [ RuMenu ]"
    var reload = lang.getString("fileReload")?:"{prefix} 配置文件 {file} &7发生变动已重新加载!"
    var delete = lang.getString("delete") ?: "{prefix} 配置文件 {file} 被删除以重新加载!"
}