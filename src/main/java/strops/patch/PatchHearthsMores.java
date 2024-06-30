package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import strops.relics.HearthsMores;

public class PatchHearthsMores {

    //public static boolean forceTrueRandomSpot = false;
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("SingleCardViewPopup");

    public static final String[] TEXT = uiStrings.TEXT;

    @SpirePatch(
            clz= AbstractPlayer.class,
            method="clickAndDragCards"
    )
    public static class PatchTool1 {
        @SpireInsertPatch(rloc = 70)
        public static SpireReturn<Boolean> Insert(AbstractPlayer __inst, boolean ___isUsingClickDragControl) {
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                if (r.relicId.equals(HearthsMores.ID)) {
                    if ((InputHelper.mX >= HearthsMores.centerX) &&
                            (InputHelper.mX <= HearthsMores.centerX + HearthsMores.width) &&
                            (InputHelper.mY >= HearthsMores.centerY) &&
                            (InputHelper.mY <= HearthsMores.centerY + HearthsMores.height)){
                        if(r.counter==0){
                            r.flash();
                            r.counter++;
                            getAnotherCard(__inst);
                            ___isUsingClickDragControl=false;
                            return SpireReturn.Return(true);
                        }
                        if(r.counter>=HearthsMores.THRESHOLD.value-1&&EnergyPanel.totalCount>=1){
                            r.flash();
                            r.counter++;
                            __inst.energy.use(1);
                            getAnotherCard(__inst);
                            ___isUsingClickDragControl=false;
                            return SpireReturn.Return(true);
                        }
                        r.flash();
                        AbstractDungeon.effectList.add(new ThoughtBubble(__inst.dialogX, __inst.dialogY, 3.0F, TEXT[11], true));
                    }
                }
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz= AbstractPlayer.class,
            method="clickAndDragCards"
    )
    public static class PatchTool2 {
        @SpireInsertPatch(rloc = 144)
        public static SpireReturn<Boolean> Insert(AbstractPlayer __inst) {
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                if (r.relicId.equals(HearthsMores.ID)) {
                    if ((InputHelper.mX >= HearthsMores.centerX) &&
                            (InputHelper.mX <= HearthsMores.centerX + HearthsMores.width) &&
                            (InputHelper.mY >= HearthsMores.centerY) &&
                            (InputHelper.mY <= HearthsMores.centerY + HearthsMores.height)&&
                            __inst.isDraggingCard &&
                            InputHelper.justReleasedClickLeft &&
                            (!Settings.isTouchScreen || Settings.isControllerMode)){
                        if(r.counter==0){
                            r.flash();
                            r.counter++;
                            getAnotherCard(__inst);
                            return SpireReturn.Return(true);
                        }
                        if(r.counter>=HearthsMores.THRESHOLD.value-1&&EnergyPanel.totalCount>=1){
                            r.flash();
                            r.counter++;
                            __inst.energy.use(1);
                            getAnotherCard(__inst);
                            return SpireReturn.Return(true);
                        }
                        r.flash();
                        AbstractDungeon.effectList.add(new ThoughtBubble(__inst.dialogX, __inst.dialogY, 3.0F, TEXT[11], true));
                    }
                }
            }
            return SpireReturn.Continue();
        }
    }

    /*
    @SpirePatch(
            clz= CardGroup.class,
            method="addToRandomSpot"
    )
    public static class PatchTool3 {
        @SpirePrefixPatch
        public static SpireReturn<Void> Insert(CardGroup __inst, AbstractCard c) {
            if(forceTrueRandomSpot){
                if (__inst.group.size() == 0) {
                    __inst.group.add(c);
                } else {
                    __inst.group.add(AbstractDungeon.cardRandomRng.random(__inst.group.size()), c);
                    //内部注释开始
                    int randSpot=AbstractDungeon.cardRandomRng.random(__inst.group.size());
                    if(randSpot==__inst.group.size()){
                        __inst.group.add(c);
                    } else {
                        __inst.group.add(randSpot, c);
                    }

                     //内部注释结束
                }
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

     */



    @SpirePatch(
            clz= AbstractPlayer.class,
            method="clickAndDragCards"
    )
    public static class PatchTool4 {
        @SpireInsertPatch(rloc = 181)
        public static SpireReturn<Boolean> Insert(AbstractPlayer __inst) {
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                if (r.relicId.equals(HearthsMores.ID)) {
                    if ((InputHelper.mX >= HearthsMores.centerX) &&
                            (InputHelper.mX <= HearthsMores.centerX + HearthsMores.width) &&
                            (InputHelper.mY >= HearthsMores.centerY) &&
                            (InputHelper.mY <= HearthsMores.centerY + HearthsMores.height)){
                        if(r.counter==0){
                            r.flash();
                            r.counter++;
                            getAnotherCard(__inst);
                            return SpireReturn.Return(true);
                        }
                        if(r.counter>=HearthsMores.THRESHOLD.value-1&&EnergyPanel.totalCount>=1){
                            r.flash();
                            r.counter++;
                            __inst.energy.use(1);
                            getAnotherCard(__inst);
                            return SpireReturn.Return(true);
                        }
                        r.flash();
                        AbstractDungeon.effectList.add(new ThoughtBubble(__inst.dialogX, __inst.dialogY, 3.0F, TEXT[11], true));
                    }
                }
            }
            return SpireReturn.Continue();
        }
    }

    private static void getAnotherCard(AbstractPlayer p){
        AbstractCard c = p.hoveredCard;
        //logger.info("悬停卡为："+c);
        //forceTrueRandomSpot=true;
        p.hand.moveToDeck(c,true);
        //forceTrueRandomSpot=false;
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p,1));
    }
}
