package com.renyun.service.config

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize
import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement

//Spring boot方式
@MapperScan("com.**.mapper")
@EnableTransactionManagement
@Configuration
class MybatisPlusConfig {
    @Bean
    fun paginationInterceptor(): PaginationInterceptor {
        val paginationInterceptor = PaginationInterceptor()
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
// paginationInterceptor.setOverflow(false);
// 设置最大单页限制数量，默认 500 条，-1 不受限制
        paginationInterceptor.setLimit(-1)
        println("分页插件启动成功！")
        // 开启 count 的 join 优化,只针对部分 left join
        paginationInterceptor.setCountSqlParser(JsqlParserCountOptimize(true))
        return paginationInterceptor
    }
}