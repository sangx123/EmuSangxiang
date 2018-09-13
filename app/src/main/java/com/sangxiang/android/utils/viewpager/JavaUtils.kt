package com.sangxiang.android.utils.viewpager

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * java 深拷贝
 */
fun <T> deepClone(obj:T):T{
    // 序列化
    val bos = ByteArrayOutputStream()
    val oos = ObjectOutputStream(bos)
    oos.writeObject(obj)
    // 反序列化
    val bis = ByteArrayInputStream(bos.toByteArray())
    val ois = ObjectInputStream(bis)
    return ois.readObject() as T
}