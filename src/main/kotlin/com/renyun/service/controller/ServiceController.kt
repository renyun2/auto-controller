package com.renyun.service.controller

import com.baomidou.mybatisplus.generator.AutoGenerator
import com.baomidou.mybatisplus.generator.InjectionConfig
import com.baomidou.mybatisplus.generator.config.*
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine
import com.renyun.service.bean.Html
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest


@RequestMapping("auto")
@RestController
class ServiceController {
    @Autowired
    private val environment: Environment? = null


    @RequestMapping("/out")
    fun out(pack: String? = null, dataName: String? = null): Html {
        if (pack == null || pack.isEmpty())
            return Html("包名不能为空")
        if (dataName == null || dataName.isEmpty())
            return Html("表名不能为空")
        // 代码生成器
        val mpg = AutoGenerator()
        // 全局配置
        val gc = GlobalConfig()
        val projectPath = System.getProperty("user.dir")
        gc.outputDir = "$projectPath/src/main/kotlin"
        gc.author = "renyun"
        gc.isOpen = false
        gc.isKotlin = true//开启空调林
        gc.isSwagger2 = false//开启swgger2
        mpg.globalConfig = gc
        // 数据源配置
        val dsc = DataSourceConfig()
        dsc.url = environment?.getProperty("spring.datasource.url")
        // dsc.setSchemaName("public")
        dsc.driverName = environment?.getProperty("spring.datasource.driver-class-name")
        dsc.username = environment?.getProperty("spring.datasource.username")
        dsc.password = environment?.getProperty("spring.datasource.password")
        mpg.dataSource = dsc
        // 包配置
        val pc = PackageConfig()
        //        pc.setModuleName(scanner("模块名"))
        pc.parent = pack
        pc.xml = "../../../../resources/mapper/"
        mpg.packageInfo = pc

        // 自定义配置
        val cfg = object : InjectionConfig() {
            override fun initMap() {
                // to do nothing
            }
        }
        mpg.cfg = cfg
//         配置模板
        val templateConfig = TemplateConfig()
//         配置自定义输出模板
//        指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        templateConfig.controller = "/templates/meController.kt"
//
//         配置自定义输出模板
//        指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        templateConfig.controller = "/templates/meController.kt"
        templateConfig.entityKt = "/templates/entity.kt"
//        templateConfig.xml = null
        mpg.template = templateConfig
        // 策略配置r
        val strategy = StrategyConfig()
        strategy.naming = NamingStrategy.underline_to_camel
        strategy.columnNaming = NamingStrategy.underline_to_camel
        //        strategy.setSuperEntityClass("com.renyun.media.BaseEntity")
        strategy.isEntityLombokModel = true
        strategy.isRestControllerStyle = true
        //        strategy.setSuperControllerClass("com.renyun.media.BaseController")
        strategy.setInclude(dataName)
//        strategy.setSuperEntityColumns("id")
        strategy.isControllerMappingHyphenStyle = true
        strategy.setTablePrefix(pc.moduleName + "_")
        mpg.strategy = strategy
        mpg.templateEngine = VelocityTemplateEngine()
        mpg.execute()
        return Html("完成")
    }

    @RequestMapping("/")
    fun index(request: HttpServletRequest): String {
        request.setAttribute("stringOut", true)
        return Html("""
            <title>代码自动生成器</title>
        """, """
             <form action="out">
            		包名： <input name="pack"></input>
            		 表名： <input name="dataName"></input>
            		<button type="submit">生成代码</button>	 
            	</form>
        """).toString()
    }

}