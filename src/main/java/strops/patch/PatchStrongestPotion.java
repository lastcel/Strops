package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.CardRewardScreen;

public class PatchStrongestPotion {

    @SpirePatch(
            clz= CardRewardScreen.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool1{
        public static SpireField<AbstractRelic> whichCallThis= new SpireField<>(() -> null);
    }
}
