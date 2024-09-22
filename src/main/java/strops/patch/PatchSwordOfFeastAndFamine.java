package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import strops.potions.FrugalPotion;
import strops.potions.GreedyPotion;
import strops.relics.SwordOfFeastAndFamine;

public class PatchSwordOfFeastAndFamine {

    @SpirePatch(
            clz= ApplyPowerAction.class,
            method=SpirePatch.CONSTRUCTOR,
            paramtypez = {AbstractCreature.class,AbstractCreature.class,AbstractPower.class,int.class,boolean.class,AbstractGameAction.AttackEffect.class}
    )
    public static class PatchTool1 {
        @SpirePrefixPatch
        public static void Prefix(ApplyPowerAction __inst, AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect) {
            if(powerToApply.ID.equals(ArtifactPower.POWER_ID)){
                for(AbstractPotion p: AbstractDungeon.player.potions){
                    if(p.ID.equals(FrugalPotion.POTION_ID)){
                        ((FrugalPotion)p).artifactPart+=powerToApply.amount;
                        p.initializeData();
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz= CombatRewardScreen.class,
            method="rewardViewUpdate"
    )
    public static class PatchTool2 {
        @SpireInsertPatch(rloc = 33)
        public static void Insert(CombatRewardScreen __inst) {
            if(SwordOfFeastAndFamine.isSwordReady){
                for(int i=0;i<SwordOfFeastAndFamine.BOTTLE_FRUGAL.value;i++){
                    __inst.rewards.add(new RewardItem(new FrugalPotion()));
                }
                for(int i=0;i<SwordOfFeastAndFamine.BOTTLE_GREEDY.value;i++){
                    __inst.rewards.add(new RewardItem(new GreedyPotion()));
                }
                SwordOfFeastAndFamine.isSwordReady=false;
            }
        }
    }
}
