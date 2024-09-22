package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import strops.relics.Decanter;
import strops.relics.DepartmentStore;
import strops.relics.Turbolens;

public class PatchDepartmentStore {

    @SpirePatch(
            clz= AbstractPlayer.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool1{
        public static SpireField<Boolean> isInRecharge= new SpireField<>(() -> false);
    }

    @SpirePatch(
            clz= EnergyManager.class,
            method="recharge"
    )
    public static class PatchTool2{
        @SpirePrefixPatch
        public static void Prefix(EnergyManager __instance){
            PatchTool1.isInRecharge.set(AbstractDungeon.player,true);
        }
    }

    @SpirePatch(
            clz= EnergyManager.class,
            method="recharge"
    )
    public static class PatchTool3{
        @SpirePostfixPatch
        public static void Postfix(EnergyManager __instance){
            PatchTool1.isInRecharge.set(AbstractDungeon.player,false);
        }
    }

    @SpirePatch(
            clz= EnergyPanel.class,
            method="useEnergy"
    )
    public static class PatchTool4{
        @SpirePostfixPatch
        public static void Postfix(int e){
            boolean isTired=false;
            if(!PatchTool1.isInRecharge.get(AbstractDungeon.player)&&(EnergyPanel.totalCount==0)){
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    if (r.relicId.equals(DepartmentStore.ID)) {
                        r.flash();
                        isTired=true;
                        break;
                    }
                }
            }
            if(isTired){
                AbstractRelic r2;
                if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                        ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                                .relicToDisenchant.equals(DepartmentStore.ID)){
                    r2.flash();
                } else {
                    AbstractDungeon.actionManager.addToBottom(new PressEndTurnButtonAction());
                }
            }
        }
    }

    @SpirePatch(
            clz= EnergyPanel.class,
            method="setEnergy"
    )
    public static class PatchTool5{
        @SpirePostfixPatch
        public static void Postfix(int e){
            boolean isTired=false;
            if(!PatchTool1.isInRecharge.get(AbstractDungeon.player)&&(EnergyPanel.totalCount==0)){
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    if (r.relicId.equals(DepartmentStore.ID)) {
                        r.flash();
                        isTired=true;
                        break;
                    }
                }
            }
            if(isTired){
                AbstractRelic r2;
                if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                        ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                                .relicToDisenchant.equals(DepartmentStore.ID)){
                    r2.flash();
                } else {
                    AbstractDungeon.actionManager.addToBottom(new PressEndTurnButtonAction());
                }
            }
        }
    }

    @SpirePatch(
            clz= EnergyPanel.class,
            method="useEnergy"
    )
    public static class PatchTool6{
        @SpirePostfixPatch
        public static void Prefix(int e){
            for(AbstractRelic r:AbstractDungeon.player.relics){
                if(r.relicId.equals(Turbolens.ID)){
                    if(((Turbolens)r).isBoosted||((Turbolens)r).isFrozen){
                        break;
                    }
                    ((Turbolens)r).secondCounter+=Math.min(e,EnergyPanel.totalCount);
                    if(((Turbolens)r).secondCounter>Turbolens.LOSS_MAX.value){
                        ((Turbolens)r).freeze();
                    }
                    break;
                }
            }
        }
    }
}
