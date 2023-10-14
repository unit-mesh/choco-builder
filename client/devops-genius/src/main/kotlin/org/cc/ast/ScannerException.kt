package org.cc.ast

class ScannerException(val node: Node, message: String) : Exception(message) {

}