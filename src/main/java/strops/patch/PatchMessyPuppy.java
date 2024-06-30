package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import strops.relics.Decanter;
import strops.relics.MessyPuppy;

public class PatchMessyPuppy {

    @SpirePatch(
            clz= GameActionManager.class,
            method="getNextAction"
    )
    public static class PatchTool1 {
        @SpireInsertPatch(rloc = 215)
        public static void Insert(GameActionManager __instance) {
            if(AbstractDungeon.player.hasRelic(MessyPuppy.ID)){
                if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                        ((Decanter)AbstractDungeon.player.getRelic(Decanter.ID))
                                .relicToDisenchant.equals(MessyPuppy.ID)){
                    AbstractDungeon.player.getRelic(Decanter.ID).flash();
                } else {
                    AbstractDungeon.player.getRelic(MessyPuppy.ID).onTrigger();
                }
            }
        }
    }

    @SpirePatch(
            clz= AbstractCreature.class,
            method="loadAnimation"
    )
    public static class PatchTool2 {
        @SpireInsertPatch(rloc = 5)
        public static void Insert(AbstractCreature __instance, String atlasUrl, String skeletonUrl, @ByRef float[] scale) {
            if(MessyPuppy.isSmallerMonster){
                scale[0]+=0.7f;
            }
        }
    }
}
