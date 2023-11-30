package site.pnpl.mira.ui.home.recycler_view

interface SelectedItemsListener {
    fun notify(isHaveSelected: Boolean)
}


interface ChangeExpandedListener {
    fun expandAll(value: Boolean)
}

interface ItemClickListener {
    /**
     * @return номер позиции в списке адаптера
     */
    fun onItemClick(position: Int)
}