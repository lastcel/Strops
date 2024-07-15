//package strops.patch;
//
//import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
//import com.megacrit.cardcrawl.actions.AbstractGameAction;
//import com.megacrit.cardcrawl.actions.GameActionManager;
//import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import com.megacrit.cardcrawl.powers.AbstractPower;
//
//import static strops.modcore.Strops.logger;
//
//public class PatchTest {
//
//
//    @SpirePatch(
//            clz = AbstractMonster.class,
//            method = "updateDeathAnimation"
//    )
//    public static class PatchTool1 {
//        @SpirePrefixPatch
//        public static void Prefix(AbstractMonster __inst) {
//            logger.info("进入更新死亡动画，怪物={}，deathTimer={}",__inst.name,__inst.deathTimer);
//        }
//    }
//
//
//    /*
//    @SpirePatch(
//            clz = AbstractMonster.class,
//            method = "updateDeathAnimation"
//    )
//    public static class PatchTool2 {
//        @SpireInsertPatch(rloc = 9)
//        public static void Insert(AbstractMonster __inst) {
//            logger.info("确定isDead，怪物={}", __inst.name);
//        }
//    }
//
//     */
//
//
//    @SpirePatch(
//            clz = AbstractMonster.class,
//            method = "updateDeathAnimation"
//    )
//    public static class PatchTool3 {
//        @SpireInsertPatch(rloc = 12)
//        public static void Insert(AbstractMonster __inst) {
//            logger.info("！！！准备endBattle()，怪物={}", __inst.name);
//        }
//    }
//
//    @SpirePatch(
//            clz = AbstractMonster.class,
//            method = "updateDeathAnimation"
//    )
//    public static class PatchTool4 {
//        @SpireInsertPatch(rloc = 10)
//        public static void Insert(AbstractMonster __inst) {
//            logger.info("@@@检测是否结束战斗，怪物="+__inst.name+"，allDead="+AbstractDungeon.getMonsters().areMonstersDead()+"，isOver="+AbstractDungeon.getCurrRoom().isBattleOver+"，cannotLose="+AbstractDungeon.getCurrRoom().cannotLose);
//        }
//    }
//
//    @SpirePatch(
//            clz = GameActionManager.class,
//            method = "update"
//    )
//    public static class PatchTool5 {
//        @SpirePrefixPatch
//        public static void Prefix(GameActionManager __inst) {
//            logger.info("------动作队列↓↓↓------");
//            for(AbstractGameAction a:__inst.actions){
//                logger.info(a.getClass().getSimpleName());
//            }
//            logger.info("------动作队列↑↑↑------");
//
//            if(__inst.currentAction==null){
//                logger.info("当前动作：【空】");
//            } else {
//                logger.info("当前动作："+__inst.currentAction.getClass().getSimpleName());
//            }
//        }
//    }
//
//    @SpirePatch(
//            clz = ApplyPowerAction.class,
//            method = "update"
//    )
//    public static class PatchTool6 {
//        @SpireInsertPatch(rloc = 1)
//        public static void Insert(ApplyPowerAction __inst) {
//            logger.info("第一种提前结束");
//        }
//    }
//
//    @SpirePatch(
//            clz = ApplyPowerAction.class,
//            method = "update"
//    )
//    public static class PatchTool7 {
//        @SpireInsertPatch(rloc = 28)
//        public static void Insert(ApplyPowerAction __inst) {
//            logger.info("第二种提前结束");
//        }
//    }
//
//    @SpirePatch(
//            clz = ApplyPowerAction.class,
//            method = "update"
//    )
//    public static class PatchTool8 {
//        @SpireInsertPatch(rloc = 125)
//        public static void Insert(ApplyPowerAction __inst, AbstractPower ___powerToApply) {
//            logger.info("进入增加新能力板块，powerToApply="+___powerToApply);
//        }
//    }
//
//    @SpirePatch(
//            clz = ApplyPowerAction.class,
//            method = "update"
//    )
//    public static class PatchTool9 {
//        @SpirePrefixPatch
//        public static void Prefix(ApplyPowerAction __inst, AbstractPower ___powerToApply) {
//            logger.info("进入给予能力动作，powerToApply="+___powerToApply);
//        }
//    }
//}

/*
package strops.patch;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.runHistory.RunHistoryScreen;
import com.megacrit.cardcrawl.screens.stats.RunData;

import static basemod.BaseMod.logger;

public class PatchTest {

    @SpirePatch(
            clz = RunHistoryScreen.class,
            method = "reloadRelics"
    )
    public static class PatchTool1 {
        @SpireInsertPatch(rloc = 10,localvars = {"relicName","relic"})
        public static void Insert(RunHistoryScreen __instance, RunData runData, String relicName, AbstractRelic relic) {
            logger.info("读取到存档遗物："+relicName);
            logger.info("恢复遗物："+relic.relicId);
        }
    }
}

 */

/*
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MayhemPower;

import static basemod.BaseMod.logger;

public class PatchTest {
    @SpirePatch(
            clz= AbstractPlayer.class,
            method="useCard"
    )
    public static class PatchTool1 {
        @SpirePrefixPatch
        public static void Prefix(AbstractPlayer __instance, AbstractCard c, AbstractMonster monster,
                                  int energyOnUse){
            System.out.println("当前回合数："+ GameActionManager.turn);
        }
    }

    @SpirePatch(
            clz= MayhemPower.class,
            method="atStartOfTurn"
    )
    public static class PatchTool2 {
        @SpirePrefixPatch
        public static void Prefix(MayhemPower __instance){
            logger.info("乱战 发动");
        }
    }

    @SpirePatch(
            clz= GameActionManager.class,
            method="getNextAction"
    )
    public static class PatchTool3 {
        @SpireInsertPatch(rloc=258)
        public static void Insert(GameActionManager __instance){
            logger.info("回合开始时抽牌 发动");
        }
    }

    @SpirePatch(
            clz= PlayTopCardAction.class,
            method="update"
    )
    public static class PatchTool4 {
        @SpirePrefixPatch
        public static void Prefix(PlayTopCardAction __instance){
            logger.info("PlayTopCardAction 动作开始");
        }
    }

    @SpirePatch(
            clz= PlayTopCardAction.class,
            method="update"
    )
    public static class PatchTool5 {
        @SpireInsertPatch(rloc=3)
        public static void Insert(PlayTopCardAction __instance){
            logger.info("PlayTopCardAction 动作结束（空抽）");
        }
    }
}

@SpirePatch(
            clz= AbstractDungeon.class,
            method="resetPlayer"
    )
    public static class PatchTool6 {
        @SpirePostfixPatch
        public static void Postfix(){
            logger.info("AbstractDungeon.resetPlayer 发动");
            PatchTool1.earliestTurnCount.set(AbstractDungeon.player, 1);
        }
    }

    @SpirePatch(
            clz= GameActionManager.class,
            method="clear"
    )
    public static class PatchTool7 {
        @SpireInsertPatch(rloc = 20)
        public static void Insert(GameActionManager __instance){
            PatchTool1.earliestTurnCount.set(AbstractDungeon.player, 1);
        }
    }
*/