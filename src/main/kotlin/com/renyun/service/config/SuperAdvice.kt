package com.renyun.service.config

import com.fasterxml.jackson.databind.util.JSONPObject
import com.google.gson.Gson
import com.renyun.service.bean.Html
import com.renyun.service.bean.ResponseBean
import org.apache.ibatis.exceptions.PersistenceException
import org.springframework.core.MethodParameter
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJacksonValue
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice


/**
 * 拦截器，处理jsonp跨域问题,处理回调，处理自定义异常
 */
@ControllerAdvice
class SuperAdvice : ResponseBodyAdvice<Any> {

    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return true
    }

    override fun beforeBodyWrite(body: Any?, returnType: MethodParameter, selectedContentType: MediaType, selectedConverterType: Class<out HttpMessageConverter<*>>, request: ServerHttpRequest, response: ServerHttpResponse): Any? {
        val servletServerHttpRequest = request as ServletServerHttpRequest
        val any = servletServerHttpRequest.servletRequest.getAttribute("stringOut")
        if (any != null) {
            return body
        }

        var data = body
        if (data == null)
            data = "ok"
        val value = data as? MappingJacksonValue ?: MappingJacksonValue(data)
        data = value.value
        response.headers["Content-Type"] = "application/json"
        if (data is ResponseBean)
            return value
        //处理返回
        data = ResponseBean(code = 200, data = data)
        //处理JSONP跨域传输
        try {
            val callback = servletServerHttpRequest.servletRequest.getParameter("callback")
            if (callback != null)
                data = JSONPObject(callback, data)
        } catch (ignored: Exception) {
        }
        value.value = data!!
        return if (body is String)
            Gson().toJson(data)
        else {
            value
        }
    }


    /**
     * 全局异常捕捉处理
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = [Exception::class])
    fun errorHandler(ex: Exception): Any {
        ex.printStackTrace()
        when (ex) {
            is InfoException -> return ResponseBean(code = ex.code ?: 417, message = ex.message)
            is DuplicateKeyException -> return ResponseBean(code = 417, message = "内容重复,请不要添加相同内容!")
            is PersistenceException -> return ResponseBean(code = 417, message = "内容已存在！")
            else -> ex.printStackTrace()
        }
        return ResponseBean(code = 500, message = ex.message)
    }
}