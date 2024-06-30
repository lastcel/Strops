//为提高兼容性，本patch为玩家类定义了一个标记是否负截断的字段。在loseGold()方法中，
//检测玩家是否将过程中的负金币最终截断为0
package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public class PatchUpgrade {

    @SpirePatch(
            clz= AbstractPlayer.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool1{
        public static SpireField<Boolean> isNegativelyCutoff= new SpireField<>(() -> false);
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "loseGold"
    )
    public static class PatchTool2 {
        @SpirePrefixPatch
        public static void Prefix(AbstractPlayer __instance,int goldAmount) {
            PatchTool1.isNegativelyCutoff.set(__instance,false);
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "loseGold"
    )
    public static class PatchTool3 {
        @SpireInsertPatch(rloc = 12)
        public static void Insert(AbstractPlayer __instance,int goldAmount) {
            PatchTool1.isNegativelyCutoff.set(__instance,true);
        }
    }
}