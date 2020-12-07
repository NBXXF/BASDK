package com.bkt.basdk

/**
 * @Description: TODO @XGode
 * @Author: XGod
 * @CreateDate: 2020/12/4 16:09
 */
fun MutableList<Int>.swap(index: Int, index2: Int) {
    val any = this[index];
    this[index] = this[index2];
    this[index2] = any;
}