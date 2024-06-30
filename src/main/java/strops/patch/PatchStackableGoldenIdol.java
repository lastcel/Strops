//This file is copied from the More Stackable Relics mod, credits to dandylion1740!
package strops.patch;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SpirePatch2(clz = RewardItem.class, method = "applyGoldBonus")
public class PatchStackableGoldenIdol {
    public static int getGoldIdolBonusGold(int tmp) {
        int gold = 0;
        float mult = 0.0F;
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (relic.relicId.equals("Golden Idol")){
                mult += 0.25F;
            }
        }
        gold += MathUtils.round(tmp * mult);
        return gold;
    }

    @SpireInstrumentPatch
    public static ExprEditor Editor() {
        return new ExprEditor() {
            public void edit(MethodCall m) {
                if (m.getMethodName().equals("round") && m.getLineNumber() < 127)
                    try {
                        m.replace("{ $_ = strops.patch.PatchStackableGoldenIdol.getGoldIdolBonusGold(tmp); }");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        };
    }
}

