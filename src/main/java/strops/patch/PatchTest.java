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