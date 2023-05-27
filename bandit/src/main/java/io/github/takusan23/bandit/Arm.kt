package io.github.takusan23.bandit

import kotlin.random.Random


/**
 * バンディットマシーンの腕
 *
 * @param p 成功率が入っている
 * @param success 成功数
 * @param fail 失敗数
 */
data class Arm(
    private val p: Float,
    private var success: Int = 0,
    private var fail: Int = 0
) {

    /** 成功率を出す */
    val successPercent: Float
        get() = (success.toFloat() / totalPlayCount)

    /** アームを引いた回数を返す */
    val totalPlayCount: Int
        get() = success + fail

    /**
     * アームを引く
     *
     * @return アームを引いて成功したら true
     */
    fun play(): Boolean {
        val nextActionIsSuccess = p > Random.nextFloat()
        if (nextActionIsSuccess) {
            success += 1
        } else {
            fail += 1
        }
        return nextActionIsSuccess
    }
}