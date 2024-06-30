//This file is wholly copied from the Reliquary mod, credits to Cae!
package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.utility.HandCheckAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.CtBehavior;

public class PatchReturnToHandInUseCardAction {
    @SpirePatch(
            clz= UseCardAction.class,
            method="update"
    )
    public static class PatchTool {
        @SpireInsertPatch(
                locator= PatchReturnToHandInUseCardAction.PatchTool.Locator.class
        )
        public static SpireReturn Insert(UseCardAction __instance, AbstractCard ___targetCard) {
            if (__instance.returnToHand) {
                // For some godforsaken reason, only the card's returnToHand is checked, not the UseCardAction's.
                AbstractDungeon.player.hand.moveToHand(___targetCard);
                AbstractDungeon.player.onCardDrawOrDiscard();
                ___targetCard.exhaustOnUseOnce = false;
                ___targetCard.dontTriggerOnUseCard = false;
                AbstractDungeon.actionManager.addToBottom(new HandCheckAction());
                __instance.isDone = true;
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.FieldAccessMatcher(UseCardAction.class, "reboundCard");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }
}
