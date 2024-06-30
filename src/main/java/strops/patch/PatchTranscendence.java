package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import strops.relics.Transcendence;

public class PatchTranscendence {
    @SpirePatch(
            clz= AbstractCard.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool1{
        public static SpireField<Boolean> inTranscendence=new SpireField<>(() -> false);
    }

    @SpirePatch(
            clz= AbstractCard.class,
            method="makeStatEquivalentCopy"
    )
    public static class PatchTool2 {
        @SpireInsertPatch(rloc=20,localvars = {"card"})
        public static void Insert(AbstractCard __inst, AbstractCard card) {
            PatchTool1.inTranscendence.set(card,PatchTool1.inTranscendence.get(__inst));
        }
    }

    @SpirePatch(
            clz= AbstractPlayer.class,
            method="bottledCardUpgradeCheck"
    )
    public static class PatchTool3 {
        @SpirePostfixPatch
        public static void Postfix(AbstractPlayer __instance, AbstractCard c) {
            if (PatchTool1.inTranscendence.get(c) && __instance.hasRelic(Transcendence.ID))
                ((Transcendence) __instance.getRelic(Transcendence.ID)).setDescriptionAfterLoading();
        }
    }
}
