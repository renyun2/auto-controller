package com.renyun.service.bean

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId

open class BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    open var id: Long? = null
    @TableId(value = "lastTime")
    open var lastTime: Long? = null
    @TableId(value = "createTime")
    open var createTime: Long? = null

}