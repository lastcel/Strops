package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import strops.relics.BigBow;

public class PatchBigBow {

    @SpirePatch(
            clz= ScryAction.class,
            method="update"
    )
    public static class PatchTool1 {
        @SpireInsertPatch(rloc = 10)
        public static void Insert(ScryAction __inst) {
            for(AbstractRelic r:AbstractDungeon.player.relics){
                if(r.relicId.equals(BigBow.ID)){
                    r.onTrigger();
                }
            }
        }
    }

    @SpirePatch(
            clz= ScryAction.class,
            method="update"
    )
    public static class PatchTool2 {
        @SpireInsertPatch(rloc = 35)
        public static void Insert(ScryAction __inst) {
            if(BigBow.isEnabled){
                BigBow.isEnabled=false;
                for(AbstractRelic r:AbstractDungeon.player.relics){
                    if(r.relicId.equals(BigBow.ID)){
                        r.onTrigger();
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz= ScryAction.class,
            method="update"
    )
    public static class PatchTool3 {
        @SpireInsertPatch(rloc = 6)
        public static void Insert(ScryAction __inst) {
            BigBow.isEnabled=true;
        }
    }
}
