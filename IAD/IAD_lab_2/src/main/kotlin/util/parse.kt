package util

import java.io.File

fun String.getFromResources(): File = File(Thread.currentThread().contextClassLoader.getResource(this).file)

