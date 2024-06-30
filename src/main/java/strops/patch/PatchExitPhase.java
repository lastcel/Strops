package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import strops.relics.ExitPhase;

public class PatchExitPhase {

    @SpirePatch(
            clz= AbstractPlayer.class,
            method="updateEscapeAnimation"
    )
    public static class PatchTool1 {
        @SpireInsertPatch(rloc = 9)
        public static void Insert(AbstractPlayer __inst) {

            for(AbstractRelic r:AbstractDungeon.player.relics){
                if(r.relicId.equals(ExitPhase.ID)){
                    if(ExitPhase.BONUS.value>0){
                        AbstractDungeon.player.gainGold(ExitPhase.BONUS.value);
                    }
                }
            }


        }
    }
}
