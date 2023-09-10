package cc.unitmesh.dsl.design

import cc.unitmesh.dsl.DesignBaseListener

class DesignAppListener: DesignBaseListener() {

}

fun removeQuote(value: String): String {
    return value.replace("\"", "")
}