package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import strops.helpers.ModHelper;
import strops.relics.Decanter;
import strops.relics.Key;

public class PatchKey {

    @SpirePatch(
            clz= AbstractImageEvent.class,
            method="update"
    )
    public static class PatchTool1{
        @SpirePrefixPatch
        public static void Prefix(AbstractImageEvent __inst){
            final String lockedMsg;
            Key k=new Key();
            lockedMsg=k.DESCRIPTIONS[5];
            /*
            switch(Settings.language){
                case ZHS: lockedMsg="[锁定] 因为：钥匙。"; break;
                case ZHT: lockedMsg="[鎖定] 因為：鑰匙。"; break;
                default: lockedMsg="[Locked] Due to: Key."; break;
            }

             */
            int availableOptionNumber=0;
            for(LargeDialogOptionButton o:__inst.imageEventText.optionList){
                if(!o.isDisabled){
                    availableOptionNumber++;
                }
            }
            if(availableOptionNumber>=2)
            {
                for(AbstractRelic r:AbstractDungeon.player.relics){
                    if(r.relicId.equals(ModHelper.makePath(Key.class.getSimpleName()))){
                        if(r.counter==0){
                            LargeDialogOptionButton firstEffectiveOption=null;
                            for(LargeDialogOptionButton o:__inst.imageEventText.optionList){
                                if(!o.isDisabled){
                                    firstEffectiveOption=o;
                                    break;
                                }
                            }
                            if(firstEffectiveOption!=null){
                                AbstractRelic r2;
                                if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                                        ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                                                .relicToDisenchant.equals(Key.ID)){
                                    r2.flash();
                                    ((Decanter) r2).decay();
                                } else {
                                    firstEffectiveOption.isDisabled=true;
                                    firstEffectiveOption.msg=lockedMsg;
                                }
                                r.counter++;
                            }
                        }
                        break;
                    }
                }
            }
        }
    }
}
