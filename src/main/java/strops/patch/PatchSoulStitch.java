package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.MarkOfTheBloom;
import strops.helpers.ModHelper;
import strops.relics.SoulStitch;

public class PatchSoulStitch {
    @SpirePatch(
            clz= AbstractPlayer.class,
            method="damage"
    )
    public static class PatchTool1 {
        @SpireInsertPatch(rloc = 126)
        public static SpireReturn<Void> Insert(AbstractPlayer __inst, DamageInfo info) {
            if ((!__inst.hasRelic(MarkOfTheBloom.ID)) &&
                    __inst.hasRelic(ModHelper.makePath(SoulStitch.class.getSimpleName()))) {
                for (AbstractRelic r: __inst.relics){
                    if((r.relicId.equals(ModHelper.makePath(SoulStitch.class.getSimpleName())))&&
                            (r.counter == -1))
                    {
                        __inst.currentHealth = 0;
                        __inst.getRelic(ModHelper.makePath(SoulStitch.class.getSimpleName())).onTrigger();
                        return SpireReturn.Return();
                    }
                }
            }
            return SpireReturn.Continue();
        }
    }
}
