package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.PlayerTurnEffect;
import strops.actions.AtriAction;
import strops.relics.Atri;
import strops.relics.Decanter;
import strops.relics.DelayedGratification;
import strops.relics.Turbolens;

import static strops.relics.DelayedGratification.canBeDelayed;

public class PatchAtri {

    @SpirePatch(
            clz= AbstractPlayer.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool1{
        public static SpireField<Integer> pastGotEnergyThisturn= new SpireField<>(() -> 0);
    }

    @SpirePatch(
            clz= EnergyPanel.class,
            method="addEnergy"
    )
    public static class PatchTool2{
        @SpirePrefixPatch
        public static void Prefix(@ByRef int[] e){
            if((AbstractDungeon.player.hasRelic(DelayedGratification.ID))&&
                    canBeDelayed){
                AbstractRelic r2;
                if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                        ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                                .relicToDisenchant.equals(DelayedGratification.ID)){
                    r2.flash();
                } else {
                    int e1=e[0];
                    e[0]=0;
                    AbstractDungeon.player.getRelic(DelayedGratification.ID).counter+=e1;
                }
            }

            for (AbstractRelic r : AbstractDungeon.player.relics) {
                if(r.relicId.equals(Atri.ID)) {
                    int e2=e[0];
                    int pastGotE=PatchTool1.pastGotEnergyThisturn.get(AbstractDungeon.player);
                    e[0]=Math.min(e[0],Atri.THRESHOLD.value-pastGotE);
                    r.counter-=e[0];
                    PatchTool1.pastGotEnergyThisturn.set(AbstractDungeon.player,pastGotE+e[0]);
                    AbstractDungeon.actionManager.addToTop(new DrawCardAction(e2-e[0],new AtriAction()));
                    break;
                }
            }

            for (AbstractRelic r : AbstractDungeon.player.relics) {
                if(r.relicId.equals(Turbolens.ID)){
                    if(((Turbolens)r).isBoosted||((Turbolens)r).isFrozen){
                        break;
                    }
                    r.counter+=e[0];
                    if(r.counter>=Turbolens.GAIN_LINE.value){
                        ((Turbolens)r).boost();
                    }
                    break;
                }
            }
        }
    }

    @SpirePatch(
            clz= EnergyPanel.class,
            method="setEnergy"
    )
    public static class PatchTool3{
        @SpirePrefixPatch
        public static void Prefix(@ByRef int[] e){
            if((AbstractDungeon.player.hasRelic(DelayedGratification.ID))&&
                    canBeDelayed){
                AbstractRelic r2;
                if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                        ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                                .relicToDisenchant.equals(DelayedGratification.ID)){
                    r2.flash();
                } else {
                    int e1=e[0];
                    e[0]=0;
                    AbstractDungeon.player.getRelic(DelayedGratification.ID).counter+=e1;
                }
            }

            for (AbstractRelic r : AbstractDungeon.player.relics) {
                if(r.relicId.equals(Atri.ID)){
                    int e2=e[0];
                    int pastGotE=PatchTool1.pastGotEnergyThisturn.get(AbstractDungeon.player);
                    e[0]=Math.min(e[0],Atri.THRESHOLD.value-pastGotE);
                    r.counter-=e[0];
                    PatchTool1.pastGotEnergyThisturn.set(AbstractDungeon.player,pastGotE+e[0]);
                    AbstractDungeon.actionManager.addToTop(new DrawCardAction(e2-e[0],new AtriAction()));
                    break;
                }
            }

            for (AbstractRelic r : AbstractDungeon.player.relics) {
                if(r.relicId.equals(Turbolens.ID)){
                    if(((Turbolens)r).isBoosted||((Turbolens)r).isFrozen){
                        break;
                    }
                    r.counter+=e[0];
                    if(r.counter>=Turbolens.GAIN_LINE.value){
                        ((Turbolens)r).boost();
                    }
                    break;
                }
            }
        }
    }

    @SpirePatch(
            clz= AbstractRoom.class,
            method="update"
    )
    public static class PatchTool4 {
        @SpireInsertPatch(rloc = 41)
        public static void Insert(AbstractRoom __instance){
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                if(r.relicId.equals(Atri.ID)){
                    r.counter=Atri.THRESHOLD.value;
                    PatchAtri.PatchTool1.pastGotEnergyThisturn.set(AbstractDungeon.player,0);
                    break;
                }
            }
        }
    }

    @SpirePatch(
            clz= PlayerTurnEffect.class,
            method=SpirePatch.CONSTRUCTOR
    )
    public static class PatchTool5 {
        @SpireInsertPatch(rloc = 19)
        public static void Insert(PlayerTurnEffect __instance){
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                if(r.relicId.equals(Atri.ID)){
                    r.counter=Atri.THRESHOLD.value;
                    PatchAtri.PatchTool1.pastGotEnergyThisturn.set(AbstractDungeon.player,0);
                    break;
                }
            }
        }
    }
}
