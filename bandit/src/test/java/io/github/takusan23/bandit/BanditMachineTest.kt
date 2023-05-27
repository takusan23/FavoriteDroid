package io.github.takusan23

import io.github.takusan23.bandit.Arm
import io.github.takusan23.bandit.BanditMachine
import org.junit.Assert
import org.junit.Test

/**
 * バンディットマシーンの一番簡単なアルゴリズムを実装してみる。
 * アームを引くエージェントはこの確率を知らない。
 */
class BanditMachineTest {

    private val armList = listOf(
        Arm(p = 0.2f),
        Arm(p = 0.3f),
        Arm(p = 0.2f),
        Arm(p = 0.3f),
        Arm(p = 0.4f)
    )

    @Test
    fun start_とりあえず() {
        // アームを探す行動をする確率
        // これが低いと、未知のアームの成功確率を見つけることが出来ず、
        // 逆に高すぎると、成績の良いアームを引く行動が減り、報酬が減ってしまう
        val epsilon = 0.3f
        // 何回アームを引くか
        val count = 100
        // アームのリストです
        // エージェント（アームを引く人）はココに書かれている成功確率 [Arm.p] を知りません
        // 知らないので、エージェントはより多くの報酬を得るため、
        // ひたすらアームを引く（それぞれのアームの確率を探す）と 引いた結果の中から一番良いアーム引く（報酬を得る） をいい感じの割合で繰り返し、一番多い報酬を得るのがこの目的らしい。
        val armList = listOf(
            Arm(p = 0.2f),
            Arm(p = 0.3f),
            Arm(p = 0.2f),
            Arm(p = 0.3f),
            Arm(p = 0.8f)
        )
        val reword = BanditMachine.start(armList, epsilon, count)
        // 少なくとも 0 以上であること
        Assert.assertTrue(0.0 <= reword)
    }

    @Test
    fun start_アームの捜索を1割と9割で比較して1割が多くなること_100回() {
        // 9割捜索
        val a = let {
            val epsilon = 0.9f
            val count = 100
            BanditMachine.start(armList, epsilon, count)
        }
        // 1割捜索
        val b = let {
            val epsilon = 0.1f
            val count = 100
            BanditMachine.start(armList, epsilon, count)
        }
        // 1割捜索（9割活用）が大きくなること
        // 確実ではないが過半数で true になるはず
        Assert.assertTrue(a < b)
    }

}