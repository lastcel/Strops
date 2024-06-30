/*
package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import strops.relics.StackedPlate;

public class PatchStackedPlate {

    @SpirePatch(
            clz = PlatedArmorPower.class,
            method = "wasHPLost"
    )
    public static class PatchTool1 {
        @SpireInsertPatch(rloc = 3)
        public static SpireReturn<Void> Insert(PlatedArmorPower __inst,
                                               DamageInfo info, int damageAmount) {
            if(__inst.owner==AbstractDungeon.player){
                boolean atLeast1Triggered=false;
                for(AbstractRelic r: AbstractDungeon.player.relics){
                    if(r.relicId.equals(StackedPlate.ID)){
                        r.counter++;
                        if(r.counter<=StackedPlate.THRESHOLD.value){
                            r.flash();
                            atLeast1Triggered=true;
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(__inst.owner, __inst.owner, new PlatedArmorPower(__inst.owner, 1), 1));
                        }
                    }
                }
                if(atLeast1Triggered){
                    return SpireReturn.Return();
                }
            }
            return SpireReturn.Continue();
        }
    }
}

 */
