package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.actions.FTLEnginesAction;
import strops.actions.ManiacalAction;
import strops.actions.WedgueRemoveRetainAction;
import strops.relics.Decanter;
import strops.relics.FTLEngines;
import strops.relics.Maniacal;
import strops.relics.Turbolens;

public class PatchFTLEngines {

    @SpirePatch(
            clz= AbstractRoom.class,
            method="endTurn"
    )
    public static class PatchTool1 {
        @SpireInsertPatch(rloc=6)
        public static void Insert(AbstractRoom __inst) {

            for(AbstractRelic r:AbstractDungeon.player.relics){
                if(r.relicId.equals(Maniacal.ID)){
                    if(!r.grayscale){
                        r.flash();
                        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player,r));
                        AbstractDungeon.actionManager.addToBottom(new ManiacalAction());
                    }
                    break;
                }
            }


            if (AbstractDungeon.player.hasRelic(FTLEngines.ID)) {
                if(AbstractDungeon.player.getRelic(FTLEngines.ID)
                        .counter==FTLEngines.THRESHOLD.value){
                    AbstractRelic r2;
                    if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                            ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                                    .relicToDisenchant.equals(FTLEngines.ID)){
                        r2.flash();
                    } else {
                        AbstractDungeon.actionManager.addToBottom(new FTLEnginesAction());
                    }
                }
            }

            AbstractDungeon.actionManager.addToBottom(new WedgueRemoveRetainAction());
        }
    }

    @SpirePatch(
            clz= AbstractRoom.class,
            method="endTurn"
    )
    public static class PatchTool2 {
        @SpirePrefixPatch
        public static void Prefix(AbstractRoom __inst) {
            for(AbstractRelic r:AbstractDungeon.player.relics){
                if(r.relicId.equals(Turbolens.ID)){
                    r.onTrigger();
                    break;
                }
            }
        }
    }

    @SpirePatch(
            clz= AbstractRelic.class,
            method="update"
    )
    public static class PatchTool3 {
        @SpirePostfixPatch
        public static void Postfix(AbstractRelic __inst) {
            if(__inst.relicId.equals(FTLEngines.ID)&&((FTLEngines)__inst).isScaleUp){
                if(__inst.hb.hovered){
                    __inst.scale=Settings.scale*2.5f;
                } else {
                    __inst.scale=Settings.scale*2.0f;
                }
            }
        }
    }
}
