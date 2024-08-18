package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import strops.relics.StroopsTester;

public class PatchStroopsTester {

    @SpirePatch(
            clz = AbstractCard.class,
            method = "update"
    )
    public static class PatchTool4 {
        @SpireInsertPatch(rloc = 18)
        public static void Insert(AbstractCard __inst) {
            for(AbstractRelic r:AbstractDungeon.player.relics){
                if(r.relicId.equals(StroopsTester.ID)) {
                    if(!__inst.upgraded && r.checkTrigger()){
                        r.beginLongPulse();
                    } else {
                        r.stopPulse();
                    }
                }
            }

            /*
            if(!__inst.upgraded){
                for(AbstractRelic r:AbstractDungeon.player.relics){
                    if(r.relicId.equals(StroopsTester.ID) && r.checkTrigger()){
                        r.beginLongPulse();
                    } else {
                        r.stopPulse();
                    }
                }
            }

             */
        }
    }
}
