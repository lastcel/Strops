package strops.patch;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.ui.panels.DiscardPilePanel;
import com.megacrit.cardcrawl.ui.panels.DrawPilePanel;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import strops.modcore.Strops;

public class PatchNeuronSight {

    @SpirePatch(clz = AbstractDungeon.class, method = "<class>")
    public static class PatchTool1 {
        @SpireEnum
        public static AbstractDungeon.CurrentScreen NS_FORETELL;
    }

    @SpirePatch(
            clz= DrawPilePanel.class,
            method="updatePositions"
    )
    public static class PatchTool2 {
        @SpireInsertPatch(locator= Locator.class)
        public static void Insert(DrawPilePanel __inst, Hitbox ___hb) {
            if (___hb.hovered && AbstractDungeon.screen == PatchTool1.NS_FORETELL) {
                AbstractDungeon.overlayMenu.hoveredTip = true;
                if (InputHelper.justClickedLeft){
                    ___hb.clickStarted=true;
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
                Matcher.FieldAccessMatcher fieldAccessMatcher=new Matcher.FieldAccessMatcher(DrawPilePanel.class,"hb");
                int[] matches= LineFinder.findAllInOrder(ctBehavior,fieldAccessMatcher);
                return new int[]{
                        matches[1]
                };
            }
        }
    }

    @SpirePatch(
            clz= DiscardPilePanel.class,
            method="updatePositions"
    )
    public static class PatchTool3 {
        @SpireInsertPatch(locator= Locator.class)
        public static void Insert(DiscardPilePanel __inst, Hitbox ___hb) {
            if (___hb.hovered && AbstractDungeon.screen == PatchTool1.NS_FORETELL) {
                AbstractDungeon.overlayMenu.hoveredTip = true;
                if (InputHelper.justClickedLeft){
                    ___hb.clickStarted=true;
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
                Matcher.FieldAccessMatcher fieldAccessMatcher=new Matcher.FieldAccessMatcher(DiscardPilePanel.class,"hb");
                int[] matches= LineFinder.findAllInOrder(ctBehavior,fieldAccessMatcher);
                return new int[]{
                        matches[1]
                };
            }
        }
    }

    /*
    @SpirePatch(
            clz= MonsterGroup.class,
            method="update"
    )
    public static class PatchTool4 {
        @SpirePrefixPatch
        public static void Prefix(MonsterGroup __inst) {
            Strops.logger.info("进入更新怪物组，当前屏幕="+AbstractDungeon.screen.toString());
        }
    }

     */
}
