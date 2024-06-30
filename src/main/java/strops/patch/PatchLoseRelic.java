package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;

public class PatchLoseRelic {

    @SpirePatch(
            clz= AbstractPlayer.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool1{
        public static StaticSpireField<Boolean> hasLostRelic= new StaticSpireField<>(() -> false);
    }

    @SpirePatch(
            clz= AbstractPlayer.class,
            method="loseRelic"
    )
    public static class PatchTool2 {
        @SpirePrefixPatch
        public static void Prefix(AbstractPlayer __inst, String targetID) {
            PatchTool1.hasLostRelic.set(false);
        }
    }

    @SpirePatch(
            clz= AbstractPlayer.class,
            method="loseRelic"
    )
    public static class PatchTool3 {
        @SpireInsertPatch(rloc=9)
        public static void Insert(AbstractPlayer __inst, String targetID) {
            PatchTool1.hasLostRelic.set(true);
        }
    }

    @SpirePatch(
            clz= AbstractPlayer.class,
            method="loseRelic"
    )
    public static class PatchTool4 {
        @SpireInsertPatch(rloc=7,localvars = {"r"})
        public static void Insert(AbstractPlayer __inst, String targetID, @ByRef AbstractRelic[] r) {
            if(PatchTool1.hasLostRelic.get()){
                r[0]=new Circlet();
            }
        }
    }
}
