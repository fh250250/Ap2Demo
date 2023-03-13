package com.github.fh250250.ap2demo

import java.util.LinkedList

class FixedQueue<T>(private var limit: Int) : LinkedList<T>() {
    override fun offer(e: T): Boolean {
        if (size >= limit) poll()
        return super.offer(e)
    }
}