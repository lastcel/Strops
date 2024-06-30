//通过仪式引用发动仪式的仪式，从而保证仪式的仪式一定是在仪式之后触发
/*
package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.RitualPower;
import strops.helpers.ModHelper;
import strops.powers.RitualOfRitualPower;

public class PatchTheCrow {
    @SpirePatch(
            clz= RitualPower.class,
            method="atEndOfTurn"
    )
    public static class PatchTool1 {
        @SpireInsertPatch(rloc = 3)
        public static void Insert(RitualPower __instance){
            for(AbstractPower p:AbstractDungeon.player.powers){
                if(p.ID.equals((ModHelper.makePath(RitualOfRitualPower.class.getSimpleName())))){
                    p.flash();
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p.owner, p.owner,
                            new RitualPower(p.owner, p.amount,true), p.amount));
                    break;
                }
            }
        }
    }
}

 */
