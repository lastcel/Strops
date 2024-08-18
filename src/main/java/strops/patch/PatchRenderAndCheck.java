package strops.patch;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import strops.relics.StropsAbstractRelic;

public class PatchRenderAndCheck {

    @SpirePatch(
            clz= TopPanel.class,
            method="render"
    )
    public static class PatchTool1 {
        @SpirePostfixPatch
        public static void Postfix(TopPanel __inst, SpriteBatch sb) {
            /*
            StrikersVeil sv=(StrikersVeil)AbstractDungeon.player.getRelic(StrikersVeil.ID);
            if(sv!=null&&sv.card!=null){
                sv.card.render(sb);
            }

             */

            for(AbstractRelic r:AbstractDungeon.player.relics){
                if(r instanceof StropsAbstractRelic){
                    ((StropsAbstractRelic) r).renderAndCheck(sb);
                }
            }
        }
    }
}
