package com.renyun.service.bean

class Html(var head: String? = null, var body: String? = null) {
    override fun toString(): String {
        return """
            <!doctype html>
            <html>
            <head>
            <meta charset="utf-8">
            $head
            </head>
            <body>
            $body
            </body>
            </html>
        """
    }
}