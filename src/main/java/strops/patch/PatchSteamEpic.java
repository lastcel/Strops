package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import strops.helpers.ModHelper;
import strops.relics.SteamEpic;

public class PatchSteamEpic {

    private static boolean savedIsEthereal;

    @SpirePatch(
            clz= AbstractCard.class,
            method="triggerOnEndOfPlayerTurn"
    )
    public static class PatchTool1 {
        @SpirePrefixPatch
        public static void Prefix(AbstractCard __inst) {
            savedIsEthereal=__inst.isEthereal;
            if(AbstractDungeon.player.hasRelic(ModHelper.makePath(SteamEpic.class.getSimpleName()))){
                if(__inst.exhaust&&!(__inst.type== AbstractCard.CardType.STATUS)&&
                        !(__inst.type== AbstractCard.CardType.CURSE)){
                    __inst.isEthereal=false;
                }
            }
        }
    }

    @SpirePatch(
            clz= AbstractCard.class,
            method="triggerOnEndOfPlayerTurn"
    )
    public static class PatchTool2 {
        @SpirePostfixPatch
        public static void Postfix(AbstractCard __inst) {
            __inst.isEthereal=savedIsEthereal;
        }
    }
}
