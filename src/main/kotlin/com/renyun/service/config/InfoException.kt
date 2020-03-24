package com.renyun.service.config

import java.lang.RuntimeException

class InfoException(info: String, var code: Int? = null) : RuntimeException(info)