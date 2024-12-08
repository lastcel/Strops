//This file is largely copied from the Reliquary mod, credits to Cae!

package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.MonsterStartTurnAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.EnemyTurnEffect;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import strops.powers.SkipPlayerTurnPower;

public class PatchStropsSkipPlayerTurn {
    @SpirePatch(clz = AbstractRoom.class, method = "<class>")
    public static class SkipPlayerTurn {
        @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
        private static class PatchSkipPlayerTurnInternal {
            @SpireInsertPatch(locator = PatchStropsSkipPlayerTurn.SkipPlayerTurn.Locator.class)
            public static SpireReturn<Void> Insert(GameActionManager __instance) {
                if (__instance.monsterQueue.isEmpty() && AbstractDungeon.player.hasPower(SkipPlayerTurnPower.POWER_ID)) {
                    AbstractDungeon.actionManager.addToBottom(new WaitAction(1.2F));
                    AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                        public void update() {
                            (AbstractDungeon.getCurrRoom()).monsters.applyEndOfTurnPowers();
                            (AbstractDungeon.getCurrRoom()).monsters.queueMonsters();
                            AbstractDungeon.getMonsters().showIntent();
                            AbstractDungeon.topLevelEffects.add(new EnemyTurnEffect());
                            this.isDone = true;
                        }
                    });
                    AbstractDungeon.actionManager.addToBottom(new MonsterStartTurnAction());
                    //GameActionManager.damageReceivedThisTurn = 0;
                    if (AbstractDungeon.player.getPower(SkipPlayerTurnPower.POWER_ID).amount <= 1) {
                        AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, SkipPlayerTurnPower.POWER_ID));
                    } else {
                        AbstractDungeon.actionManager.addToTop(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, SkipPlayerTurnPower.POWER_ID, 1));
                    }
                    return SpireReturn.Return();
                }
                return SpireReturn.Continue();
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(GameActionManager.class, "monsterQueue");
                int[] matches = LineFinder.findAllInOrder(ctMethodToPatch, fieldAccessMatcher);
                return new int[] { matches[matches.length - 1] };
            }
        }
    }
}
