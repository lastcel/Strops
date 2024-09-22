package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import strops.helpers.ModHelper;
import strops.relics.Lemonade;

public class PatchLemonade {

    /*
    @SpirePatch(
            clz= AbstractRoom.class,
            method="update"
    )
    public static class PatchTool1 {
        @SpireInsertPatch(rloc = 174)
        public static void Insert(AbstractRoom __inst) {
            for(AbstractRelic r: AbstractDungeon.player.relics){
                if(r.relicId.equals(ModHelper.makePath(Lemonade.class.getSimpleName())) &&
                        r.counter==0){
                    r.flash();
                    r.stopPulse();
                    for(int i = 0; i < Lemonade.POTION.value; i++){
                        __inst.addPotionToRewards(PotionHelper.getPotion("Energy Potion"));
                    }
                }
            }
        }
    }

     */

    @SpirePatch(
            clz= EnergyPanel.class,
            method="addEnergy"
    )
    public static class PatchTool2 {
        @SpirePostfixPatch
        public static void Postfix(int e) {
            if(EnergyPanel.totalCount>=Lemonade.ENERGY.value){
                for(AbstractRelic r: AbstractDungeon.player.relics){
                    if(r.relicId.equals(ModHelper.makePath(Lemonade.class.getSimpleName()))&&
                            !((Lemonade)r).isUsed){
                        r.flash();
                        AbstractDungeon.actionManager.addToBottom(
                                new DrawCardAction(Lemonade.DRAW.value));
                        ((Lemonade)r).isUsed=true;
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz= EnergyPanel.class,
            method="setEnergy"
    )
    public static class PatchTool3 {
        @SpirePostfixPatch
        public static void Postfix(int e) {
            if(EnergyPanel.totalCount>=Lemonade.ENERGY.value){
                for(AbstractRelic r: AbstractDungeon.player.relics){
                    if(r.relicId.equals(ModHelper.makePath(Lemonade.class.getSimpleName()))&&
                            !((Lemonade)r).isUsed){
                        r.flash();
                        AbstractDungeon.actionManager.addToBottom(
                                new DrawCardAction(Lemonade.DRAW.value));
                        ((Lemonade)r).isUsed=true;
                    }
                }
            }
        }
    }
}
