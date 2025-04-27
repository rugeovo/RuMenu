package rumenu.menu

object MenuUtiliy {

    fun findIndicesOfString(args: String, layout: List<String>): MutableList<Int> {
        val indices = mutableListOf<Int>()
        var currentIndex = 0
        for (str in layout) {
            var index = str.indexOf(args)
            while (index != -1) {
                indices.add(currentIndex + index)
                index = str.indexOf(args, index + 1)
            }
            currentIndex += str.length
        }
        return indices
    }

}