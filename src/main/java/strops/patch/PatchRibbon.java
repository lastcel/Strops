package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import strops.actions.RibbonAction;
import strops.helpers.ModHelper;
import strops.relics.Ribbon;

public class PatchRibbon {

    @SpirePatch(
            clz= ApplyPowerAction.class,
            method="update"
    )
    public static class PatchTool1{
        @SpireInsertPatch(rloc=16)
        public static void Insert(ApplyPowerAction __inst, AbstractPower ___powerToApply){
            if((__inst.target.isPlayer)&&
                    (___powerToApply.type==AbstractPower.PowerType.BUFF)){
                for(AbstractRelic r:AbstractDungeon.player.relics){
                    if (r.relicId.equals(ModHelper.makePath(Ribbon.class.getSimpleName()))){
                        r.counter=(++r.counter)%Ribbon.THRESHOLD.value;
                        //频闪需要onTrigger
                        if(r.counter==0){
                            r.flash();
                            AbstractDungeon.actionManager.addToTop(new RibbonAction(AbstractDungeon.player));
                            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, r));
                        }
                    }
                }
            }
        }
    }
}
