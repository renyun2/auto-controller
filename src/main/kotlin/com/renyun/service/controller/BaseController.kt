package com.renyun.service.controller

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper
import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.core.metadata.OrderItem
import com.baomidou.mybatisplus.core.toolkit.Wrappers
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.IService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.renyun.service.bean.BaseEntity
import com.renyun.service.bean.WrappersBean
import com.renyun.service.config.InfoException
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import java.util.*
import kotlin.collections.ArrayList

open class BaseController<T : IService<K>, K : BaseEntity>(var update: Boolean = true,
                                                           var updateKey: Boolean = true,
                                                           var list: Boolean = true,
                                                           var page: Boolean = true,
                                                           var add: Boolean = true,
                                                           var remove: Boolean = true,
                                                           var get: Boolean = true) {

    open lateinit var service: T

    /**
     * 获取全部
     */
    @RequestMapping("list")
    open fun list(): MutableList<K>? {
        return service.list()
    }


    @RequestMapping("page")
    open fun pageList(offset: Long?, limit: Long?, orders: String? = null, search: String? = null, searchKey: String? = null, where: String? = null): IPage<K>? {
        if (!page)
            throw InfoException("越权操作")


        val page = Page<K>(((offset ?: 0) / (limit ?: 10)) + 1, limit ?: 10)
        getOrderToMap(orders)?.forEach {
            page.addOrder(OrderItem().setColumn(it.key).setAsc(it.value))
        }
        val searchWrap = getSearch(QueryWrapper(), search, searchKey)
        val wrapper = getWhere(searchWrap, where)
        return service.page(page, wrapper ?: Wrappers.emptyWrapper())
    }

    /**
     * 获取搜索内容
     */
    protected fun getSearch(wrapper: QueryWrapper<K>?, search: String?, searchKey: String?): QueryWrapper<K>? {
        if (search == null || search.isEmpty() || searchKey == null || searchKey.isEmpty())
            return wrapper
        val wrappers = wrapper ?: QueryWrapper()
        val split = searchKey.split(",")
        if (split.isNotEmpty()) {
            wrappers.and {
                split.forEachIndexed { index, s ->
                    if (index < split.size - 1) {
                        it.like(s, "%$search%").or()
                    } else {
                        it.like(s, "%$search%")
                    }
                }
            }
        }
        return wrappers
    }


    /**
     * 转换排序数组
     */
    protected fun getOrderToMap(orders: String?): Map<String, Boolean>? {
        if (orders == null)
            return null
        val split = orders.split(",")
        val map = hashMapOf<String, Boolean>()
        split.forEach {
            val item = it.split(":")
            if (item.size != 2)
                throw InfoException("排序格式错误！")
            map[item[0]] = item[1].toLowerCase() != "desc"
        }
        return map
    }

    /**
     * 获取where条件
     */
    open fun getWhere(wrapper: QueryWrapper<K>?, where: String?): QueryWrapper<K>? {
        val wheres = Gson().fromJson<MutableList<WrappersBean>>(where, object : TypeToken<MutableList<WrappersBean>>() {}.type)
        val lambdaQuery = wrapper ?: QueryWrapper<K>()
        /**
         * 判别条件
         * 参数命名说明:
         * eq : =
         * ne : <>
         * gt : >
         * ge : >=
         * lt : <
         * le <=
         */
        wheres?.forEach {
            when (it.where) {
                "=" -> {
                    lambdaQuery.eq(it.getKey(), it.getValue())
                }
                "<>" -> {
                    lambdaQuery.ne(it.getKey(), it.getValue())
                }
                ">" -> {
                    lambdaQuery.gt(it.getKey(), it.getValue())
                }
                ">=" -> {
                    lambdaQuery.ge(it.getKey(), it.getValue())
                }
                "<" -> {
                    lambdaQuery.lt(it.getKey(), it.getValue())
                }
                "<=" -> {
                    lambdaQuery.le(it.getKey(), it.getValue())
                }
                "in" -> {
                    lambdaQuery.`in`(it.getKey(), it.getValues())
                }
                else -> {

                }
            }
        }
        return lambdaQuery
    }

    /**
     * 添加
     */
    @RequestMapping("add")
    open fun add(@RequestBody bean: K) {
        if (!add)
            throw InfoException("越权操作")
        bean.createTime = Date().time
        bean.lastTime = bean.createTime
        service.save(bean)

    }

    /**
     * 添加多个
     */
    @RequestMapping("adds")
    open fun adds(@RequestBody bean: ArrayList<K>) {
        if (!add)
            throw InfoException("越权操作")
        val time = Date().time
        bean.forEach {
            it.createTime = time
            it.lastTime = time
        }
        service.saveBatch(bean)
    }

    /**
     * 删除多个
     */
    @RequestMapping("remove")
    open fun remove(@RequestBody id: ArrayList<Long>) {
        if (!remove)
            throw InfoException("越权操作")
        service.removeByIds(id)
    }

    /**
     * 更新，根据id
     */
    @RequestMapping("update")
    open fun update(@RequestBody bean: K) {
        if (!update)
            throw InfoException("越权操作")
        bean.lastTime = Date().time
        service.updateById(bean)
    }

    /**
     * 更新，根据id
     */
    @RequestMapping("updates")
    open fun updates(@RequestBody bean: ArrayList<K>) {
        if (!update)
            throw InfoException("越权操作")
        val time = Date().time
        bean.forEach {
            it.lastTime = time
        }
        service.updateBatchById(bean)
    }

    /**
     * 查询单个
     */
    @RequestMapping("get")
    open fun findById(id: Long): K? {
        if (!get)
            throw InfoException("越权操作")
        return service.getById(id)
    }

    /**
     * 更新某个字段得值
     */
    @RequestMapping("updateKey")
    open fun update(key: String, value: String, id: Int) {
        if (!updateKey)
            throw InfoException("越权操作")
        val updateWrapper = UpdateWrapper<K>(null)
        val eq = updateWrapper.set(key, value).eq("id", id)
        service.update(eq)
    }

    /**
     *更新多个项目某个字段得值
     */
    @RequestMapping("updateKeys")
    open fun update(key: String, value: String, id: Array<Long>) {
        if (!updateKey)
            throw InfoException("越权操作")
        var updateWrapper = UpdateWrapper<K>()
        var ids = id.toMutableList()
        updateWrapper = updateWrapper.set(key, value).`in`("id", ids)
        service.update(updateWrapper)
    }
}
