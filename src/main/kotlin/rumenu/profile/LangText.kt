package rumenu.profile

import rumenu.profile.FileConfig.lang

object LangText {
    var prefix = lang.getString("prefix")?:"&x&4&8&6&f&f&b [ RuMenu ]"
    var filereload = lang.getString("fileReload")?:"{prefix} 配置文件 {file} &7发生变动已重新加载!"
    var delete = lang.getString("delete") ?: "{prefix} 配置文件 {file} 被删除以重新加载!"
    var reload = lang.getString("reload") ?: "{prefix} 共 {num} 个菜单文件已重新加载!"
    var give = lang.getString("give") ?: "{prefix} 已给予绑定菜单物品"
}