package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Lightning;

import static strops.cards.LightningStorm.LightningCountLastTurn;

public class PatchLightningStorm {

    @SpirePatch(
            clz= GameActionManager.class,
            method="getNextAction"
    )
    public static class PatchTool1 {
        @SpireInsertPatch(rloc = 224)
        public static void Insert(GameActionManager __instance) {
            LightningCountLastTurn=0;
            for(AbstractOrb o:__instance.orbsChanneledThisTurn){
                if(o instanceof Lightning){
                    LightningCountLastTurn++;
                }
            }
        }
    }

    @SpirePatch(
            clz= GameActionManager.class,
            method="clear"
    )
    public static class PatchTool2 {
        @SpirePrefixPatch
        public static void Prefix(GameActionManager __instance) {
            LightningCountLastTurn=0;
        }
    }
}
