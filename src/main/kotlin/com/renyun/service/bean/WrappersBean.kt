package com.renyun.service.bean

/**
 * 万能条件查询
 * where 表示sql中where 用于表示的条件符号 如 < > <= >= 等.也可以表示in group by 等多种条件
 */
class WrappersBean {

    /**
     * 条件
     */
    var where: String = ""

    /**
     * 判别值
     */
    var list: ArrayList<Any> = arrayListOf()

    /**
     * 获取条件判断字段名
     */
    fun getKey(): String {
        if (list.size > 0)
            return list[0].toString()
        else {
            throw  IllegalArgumentException("条件判断字符串未设置")
        }
    }

    /**
     * 获取单个条件判断字段值
     */
    fun getValue(): Any {
        if (list.size > 1) {
            return list[1]
        } else {
            throw  IllegalArgumentException("条件判断值未设置")
        }
    }

    /**
     * 获取多个条件判断字段值
     */
    fun getValues(): MutableList<Any> {
        if (list.size > 1) {
            return list.subList(1, list.size)
        } else {
            throw  IllegalArgumentException("条件判断值未设置")
        }
    }


    /**
     * 设置字段名
     */
    fun setKey(key: String) {
        if (list.size == 0)
            list.add(key)
        else
            list[0] = key
    }

    /**
     * 设置值
     */
    fun setValue(value: Any) {
        if (list.size == 2)
            list[1] = value
        else if (list.size <= 0)
            throw IllegalArgumentException("请先设置键")
        else {
            list.add(value)
        }
    }

    /**
     * 设置多个值
     */
    fun setValues(values: ArrayList<Any>) {
        list.addAll(1, values)
    }


}