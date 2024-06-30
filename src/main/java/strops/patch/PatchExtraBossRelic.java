//Adapted from the Neat the Spire mod, credits to Diamsword!
package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.select.BossRelicSelectScreen;
import java.util.ArrayList;
import javassist.CtBehavior;
import strops.modcore.Strops;
import strops.relics.GenerosityCharm;

public class PatchExtraBossRelic {
    @SpirePatch(clz = BossRelicSelectScreen.class, method = "open")
    public static class Open {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(BossRelicSelectScreen __instance, ArrayList<AbstractRelic> chosenRelics) {
            if (AbstractDungeon.player.hasRelic(GenerosityCharm.ID) && GenerosityCharm.BOSS_CHOICES.value >= 4) {

                float SLOT_2_X = (__instance.relics.get(1)).currentX;
                float SLOT_3_X = (__instance.relics.get(2)).currentX;
                float SLOT_1_Y = (__instance.relics.get(0)).currentY;
                float SLOT_2_Y = (__instance.relics.get(1)).currentY;

                if(Strops.FourthBossRelicStorage != null){
                    Strops.FourthBossRelicStorage.spawn(SLOT_3_X, SLOT_1_Y);
                    Strops.FourthBossRelicStorage.hb.move(Strops.FourthBossRelicStorage.currentX, Strops.FourthBossRelicStorage.currentY);
                    __instance.relics.add(Strops.FourthBossRelicStorage);
                    AbstractRelic first = __instance.relics.get(0);
                    first.currentX = SLOT_2_X;
                    first.hb.move(first.currentX, first.currentY);
                    if (__instance.relics.size() > 4) {
                        Strops.FourthBossRelicStorage.currentX = SLOT_2_X;
                        Strops.FourthBossRelicStorage.currentY = (SLOT_1_Y + SLOT_2_Y) / 2.0F;
                        Strops.FourthBossRelicStorage.hb.move(Strops.FourthBossRelicStorage.currentX, Strops.FourthBossRelicStorage.currentY);
                    }
                }

                if (GenerosityCharm.BOSS_CHOICES.value == 5 && Strops.FifthBossRelicStorage != null) {
                    Strops.FifthBossRelicStorage.spawn((SLOT_2_X + SLOT_3_X) / 2.0F, (SLOT_1_Y + SLOT_2_Y) / 2.0F);
                    Strops.FifthBossRelicStorage.hb.move(Strops.FifthBossRelicStorage.currentX, Strops.FifthBossRelicStorage.currentY);
                    __instance.relics.add(Strops.FifthBossRelicStorage);
                    if (__instance.relics.size() > 5) {
                        Strops.FifthBossRelicStorage.currentX = SLOT_3_X;
                        Strops.FifthBossRelicStorage.currentY = (SLOT_1_Y + SLOT_2_Y) / 2.0F;
                        Strops.FifthBossRelicStorage.hb.move(Strops.FifthBossRelicStorage.currentX, Strops.FifthBossRelicStorage.currentY);
                    }
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(BossRelicSelectScreen.class, "relics");
                int[] found = LineFinder.findAllInOrder(ctMethodToPatch, fieldAccessMatcher);
                return new int[] { found[found.length - 1] };
            }
        }
    }
}

