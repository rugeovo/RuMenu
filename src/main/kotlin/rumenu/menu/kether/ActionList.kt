package rumenu.menu.kether

import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser

object ActionList {

    //创建list 并传入一个值
    @KetherParser(["creat-list"], shared = false)
    fun createList() = combinationParser {
        it.group(originList()).apply(it) {
            val mlist = mutableListOf<Any>()
            now {
                mlist
            }
        }
    }
    //添加元素
    @KetherParser(["add-list"], shared = false)
    fun add1() = combinationParser {
        it.group(type<MutableList<Any>>(),type<Any>()).apply(it) { a1,a2 ->
            now {
                a1.add(a2)
            }
        }
    }

    //根据下标删除
    @KetherParser(["removeAt-list"], shared = false)
    fun removeAt() = combinationParser {
        it.group(type<MutableList<Any>>(),type<Int>()).apply(it) { a1,a2 ->
            now {
                a1.removeAt(a2)
            }
        }
    }

    //根据元素删除下标
    @KetherParser(["remove-list"], shared = false)
    fun remove() = combinationParser {
        it.group(type<MutableList<Any>>(),type<Any>()).apply(it) { a1,a2 ->
            now {
                a1.remove(a2)
            }
        }
    }
}