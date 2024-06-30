package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.chests.BossChest;
import strops.modcore.Strops;
import strops.relics.GenerosityCharm;

@SpirePatch(clz = BossChest.class, method = "<ctor>")
public class PatchExtraBossRelicExtraPatch {
    @SpirePostfixPatch
    public static void postfix(BossChest __instance) {
        if(!AbstractDungeon.player.hasRelic(GenerosityCharm.ID)||GenerosityCharm.BOSS_CHOICES.value<4){
            Strops.FourthBossRelicStorage = null;
            Strops.FifthBossRelicStorage = null;
        } else if(GenerosityCharm.BOSS_CHOICES.value == 4){
            Strops.FifthBossRelicStorage = null;
            if(__instance.relics.size() < 4){
                Strops.FourthBossRelicStorage = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.BOSS);
            } else {
                Strops.FourthBossRelicStorage = null;
            }
        } else {
            if(__instance.relics.size() < 4){
                Strops.FourthBossRelicStorage = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.BOSS);
                Strops.FifthBossRelicStorage = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.BOSS);
            } else if(__instance.relics.size() == 4) {
                Strops.FourthBossRelicStorage = null;
                Strops.FifthBossRelicStorage = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.BOSS);
            } else {
                Strops.FourthBossRelicStorage = null;
                Strops.FifthBossRelicStorage = null;
            }
        }
    }
}

