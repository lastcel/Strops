//原版游戏工具提示的浮现方式和时机仍有待研究

/*
package strops.patch;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.SingleRelicViewPopup;

import java.util.ArrayList;

public class PatchRelicPopup {
    @SpirePatch(
            clz= SingleRelicViewPopup.class,
            method="renderTips"
    )
    public static class PatchTool1 {
        @SpireInsertPatch(rloc=3,localvars = {"t"})
        public static void Insert(SingleRelicViewPopup __instance, SpriteBatch sb,
                                  AbstractRelic ___relic, ArrayList<PowerTip> t){
            t.add(___relic.tips.get(0));
        }
    }
}

 */
