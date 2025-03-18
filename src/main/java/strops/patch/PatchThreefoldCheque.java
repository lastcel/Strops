package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class PatchThreefoldCheque {

    @SpirePatch(
            clz= AbstractCard.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool1{
        public static SpireField<Boolean> isReadyToUnfold=new SpireField<>(() -> false);
    }

    @SpirePatch(
            clz= AbstractCard.class,
            method="makeStatEquivalentCopy"
    )
    public static class PatchTool2 {
        @SpireInsertPatch(rloc=20,localvars = {"card"})
        public static void Insert(AbstractCard __inst, AbstractCard card) {
            PatchTool1.isReadyToUnfold.set(card, PatchTool1.isReadyToUnfold.get(__inst));
        }
    }
}
