package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import strops.relics.NoNameForNow4;

public class PatchNoNameForNow4 {

    @SpirePatch(
            clz= AbstractCard.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool1{
        public static SpireField<Boolean> inNoNameForNow4=new SpireField<>(() -> false);
    }

    @SpirePatch(
            clz= AbstractCard.class,
            method="makeStatEquivalentCopy"
    )
    public static class PatchTool2 {
        @SpireInsertPatch(rloc=20,localvars = {"card"})
        public static void Insert(AbstractCard __inst, AbstractCard card) {
            PatchTool1.inNoNameForNow4.set(card, PatchTool1.inNoNameForNow4.get(__inst));
        }
    }

    @SpirePatch(
            clz = RemoveSpecificPowerAction.class,
            method = "update"
    )
    public static class PatchTool4 {
        @SpireInsertPatch(rloc = 22,localvars = {"removeMe"})
        public static void Insert(RemoveSpecificPowerAction __inst, AbstractPower removeMe) {
            if(__inst.target instanceof AbstractPlayer&&(removeMe.type==AbstractPower.PowerType.BUFF||removeMe.amount==0&&removeMe.canGoNegative)){
                AbstractPlayer p=AbstractDungeon.player;
                for(AbstractRelic r:p.relics){
                    if(r.relicId.equals(NoNameForNow4.ID)){
                        r.flash();
                        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(p,r));
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p,p,NoNameForNow4.BONUS.value));
                    }
                }
            }
        }
    }
}
