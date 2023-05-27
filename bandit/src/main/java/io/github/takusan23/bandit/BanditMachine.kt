package io.github.takusan23.bandit

import kotlin.random.Random

/** 一番簡単なバンディット問題に挑戦してみる */
object BanditMachine {

    /**
     * バンディット問題を解く
     * エージェントは各 [Arm] の成功率を知らないので、知らないなりに頑張って多く報酬を受け取る。
     *
     * @param armList [Arm]
     * @param count 腕を引く回数
     * @param epsilon 腕を引く行動をする確率
     * @return スコア
     */
    fun start(
        armList: List<Arm>,
        epsilon: Float,
        count: Int
    ): Float {
        val rewardList = arrayListOf<Boolean>()
        // 引数の数だけ挑戦する
        repeat(count) { i ->
            // アームを探すか、過去の履歴から一番良いアームを引くか
            val nextActionIsFind = epsilon > Random.nextFloat()
            // 結果を配列に入れる
            rewardList += if (nextActionIsFind) {
                // 探す
                armList.random().play()
            } else {
                // 履歴から引く
                val highScoreArm = armList.maxBy { it.successPercent }
                highScoreArm.play()
            }
        }
        // 結果を入れる
        return rewardList.count { it }.toFloat() / count
    }

}