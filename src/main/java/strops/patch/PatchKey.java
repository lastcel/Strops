package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import strops.relics.Decanter;
import strops.relics.Key;

public class PatchKey {

    public static String savedMsg;
    public static final String LOCKED_MSG = CardCrawlGame.languagePack.getRelicStrings(Key.ID).DESCRIPTIONS[5];

    @SpirePatch(
            clz= AbstractImageEvent.class,
            method="update"
    )
    public static class PatchTool1{
        @SpirePrefixPatch
        public static void Prefix(AbstractImageEvent __inst){
            int availableOptionNumber=0;
            for(LargeDialogOptionButton o:__inst.imageEventText.optionList){
                if(!o.isDisabled){
                    availableOptionNumber++;
                }
            }
            if(availableOptionNumber>=2&&AbstractDungeon.player.hasRelic(Key.ID)&&
                    AbstractDungeon.player.getRelic(Key.ID).counter==0)
            {
                LargeDialogOptionButton firstEffectiveOption=null;
                for(LargeDialogOptionButton o:__inst.imageEventText.optionList){
                    if(!o.isDisabled){
                        firstEffectiveOption=o;
                        break;
                    }
                }
                if(firstEffectiveOption!=null){
                    firstEffectiveOption.isDisabled=true;
                    savedMsg=firstEffectiveOption.msg;
                    firstEffectiveOption.msg=LOCKED_MSG;
                    AbstractDungeon.player.getRelic(Key.ID).counter++;
                }
            }

            AbstractRelic r2;
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                            .relicToDisenchant.equals(Key.ID)){
                for(LargeDialogOptionButton o:__inst.imageEventText.optionList){
                    if(o.isDisabled&&o.msg.equals(LOCKED_MSG)){
                        r2.flash();
                        ((Decanter) r2).decay();
                        o.isDisabled=false;
                        o.msg=savedMsg;
                        break;
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz= AbstractEvent.class,
            method="update"
    )
    public static class PatchTool2{
        @SpirePrefixPatch
        public static void Prefix(AbstractEvent __inst){
            int availableOptionNumber=0;
            for(LargeDialogOptionButton o:RoomEventDialog.optionList){
                if(!o.isDisabled){
                    availableOptionNumber++;
                }
            }
            if(availableOptionNumber>=2&&AbstractDungeon.player.hasRelic(Key.ID)&&
                    AbstractDungeon.player.getRelic(Key.ID).counter==0)
            {
                LargeDialogOptionButton firstEffectiveOption=null;
                for(LargeDialogOptionButton o:RoomEventDialog.optionList){
                    if(!o.isDisabled){
                        firstEffectiveOption=o;
                        break;
                    }
                }
                if(firstEffectiveOption!=null){
                    firstEffectiveOption.isDisabled=true;
                    savedMsg=firstEffectiveOption.msg;
                    firstEffectiveOption.msg=LOCKED_MSG;
                    AbstractDungeon.player.getRelic(Key.ID).counter++;
                }
            }

            AbstractRelic r2;
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                            .relicToDisenchant.equals(Key.ID)){
                for(LargeDialogOptionButton o:RoomEventDialog.optionList){
                    if(o.isDisabled&&o.msg.equals(LOCKED_MSG)){
                        r2.flash();
                        ((Decanter) r2).decay();
                        o.isDisabled=false;
                        o.msg=savedMsg;
                        break;
                    }
                }
            }
        }
    }
}
