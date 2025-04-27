package rumenu.menu

class Icons {
    /**
     * 菜单中的物品集合
     */
    val icons: MutableList<Menu.Item> = mutableListOf()

    /**
     * 开启物品刷新
     */
    fun updateItems(){
        icons.forEach{
            it.updateItem()
        }
    }

    /**
     * 关闭物品刷新
     */
    fun cancelItems(){
        icons.forEach { item ->
            item.cancel()
        }
        icons.clear()
    }
    fun addItem(item: Menu.Item){
        icons.add(item)
    }
}
data class ItemA(val char: String, val update: Long,val display: Map<String, Any?>,val slots: List<Int>)