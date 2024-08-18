package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.relics.PainsReward;

public class PatchPainsReward {

    @SpirePatch(
            clz= AbstractRoom.class,
            method="endBattle"
    )
    public static class PatchTool1 {
        @SpireInsertPatch(rloc=7)
        public static void Insert(AbstractRoom __inst) {
            for(AbstractRelic r: AbstractDungeon.player.relics){
                if(r.relicId.equals(PainsReward.ID)){
                    r.onTrigger();
                }
            }
        }
    }
}
