package com.fourteen06.emseesquare.controller

interface DrawerController {
    fun lockDrawer() //Locks the Drawer at appropriate position
    fun unlockDrawer() //unlock the drawer at appropriate position
    fun openDrawer()
    fun closeDrawer()
}