package strops.patch;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import strops.helpers.ModHelper;
import strops.relics.RestrainedHeart;

public class PatchRestrainedHeart {

    private static boolean toStore;

    /*
    @SpirePatch(
            clz= AbstractCard.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool1{
        public static SpireField<Boolean> isToRestrain= new SpireField<>(() -> false);
    }

     */

    /*
    //处理转化的全弃
    @SpirePatch(
            clz= DiscardAction.class,
            method="update"
    )
    public static class PatchTool2 {
        @SpireInsertPatch(rloc = 6)
        public static SpireReturn<Void> Insert(DiscardAction __inst, AbstractPlayer ___p,
                                  boolean ___endTurn, boolean ___isRandom,
                                  float ___duration) {
            logger.info("进入补丁2");
            if(!___endTurn&&___p.hand.size() > __inst.amount-1&&
                    AbstractDungeon.player.hasRelic(
                    ModHelper.makePath(RestrainedHeart.class.getSimpleName()))) {
                AbstractRelic r = AbstractDungeon.player.getRelic(
                        ModHelper.makePath(RestrainedHeart.class.getSimpleName()));
                if (true) {
                    if(___isRandom&&!r.grayscale){
                        __inst.amount--;
                        for (int i = 0; i < __inst.amount; i++) {
                            AbstractCard c = ___p.hand.getRandomCard(AbstractDungeon.cardRandomRng);
                            ___p.hand.moveToDiscardPile(c);
                            c.triggerOnManualDiscard();
                            GameActionManager.incrementDiscard(false);
                        }
                    } else {
                        if (__inst.amount < 0) {
                            AbstractDungeon.handCardSelectScreen.open(DiscardAction.TEXT[0], 99, true, true);
                            AbstractDungeon.player.hand.applyPowers();
                            ___duration -= Gdx.graphics.getDeltaTime();
                            if (___duration < 0.0F) {
                                __inst.isDone = true;
                            }
                            return SpireReturn.Return();
                        }
                        DiscardAction.numDiscarded = __inst.amount;
                        PatchTool6.canChooseOneLess.set(AbstractDungeon.handCardSelectScreen, true);
                        AbstractDungeon.handCardSelectScreen.open(DiscardAction.TEXT[0], __inst.amount, false);
                        AbstractDungeon.player.hand.applyPowers();
                        ___duration -= Gdx.graphics.getDeltaTime();
                        if (___duration < 0.0F) {
                            __inst.isDone = true;
                        }
                        return SpireReturn.Return();
                    }
                }
            }
            return SpireReturn.Continue();
        }
    }

     */

    //全弃(包括“原生的”全弃和转化的全弃），可以选一张不弃牌
    @SpirePatch(
            clz= DiscardAction.class,
            method="update"
    )
    public static class PatchTool3 {
        @SpireInsertPatch(rloc = 9,localvars = {"tmp"})
        public static void Insert(DiscardAction __inst, boolean ___endTurn, @ByRef int[] tmp) {
            //logger.info("进入补丁3");
            if(!___endTurn&&tmp[0]>=1&&AbstractDungeon.player.hasRelic(
                    ModHelper.makePath(RestrainedHeart.class.getSimpleName()))){
                AbstractRelic r= AbstractDungeon.player.getRelic(
                        ModHelper.makePath(RestrainedHeart.class.getSimpleName()));
                AbstractDungeon.handCardSelectScreen.open(r.DESCRIPTIONS[5], 1, true, true);
                tmp[0]=0;
            }
        }
    }

    //“选不弃牌”界面及弃其余牌处理
    @SpirePatch(
            clz= DiscardAction.class,
            method="update"
    )
    public static class PatchTool4 {
        @SpireInsertPatch(rloc = 50)
        public static void Insert(DiscardAction __inst,AbstractPlayer ___p) {
            //logger.info("进入补丁4");
            /*
            if(AbstractDungeon.handCardSelectScreen==null){
                logger.info("AbstractDungeon.handCardSelectScreen为空！");
            }
            if(AbstractDungeon.handCardSelectScreen.selectionReason==null){
                logger.info("AbstractDungeon.handCardSelectScreen.selectionReason为空！");
            }

             */
            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved&&
                    AbstractDungeon.handCardSelectScreen.selectionReason!=null&&
                    !AbstractDungeon.handCardSelectScreen.selectionReason.equals(
                            DiscardAction.TEXT[0])) {
                AbstractCard cRestrained=null;
                for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                    //PatchTool1.isToRestrain.set(c, true);
                    cRestrained=c;
                    //logger.info("设置"+c.name+"阻止丢弃为"+PatchTool1.isToRestrain.get(c));
                    //AbstractDungeon.player.hand.addToTop(c);
                    //AbstractDungeon.player.limbo.addToTop(c);
                }
                int tmp=___p.hand.size();
                for (int i = 0; i < tmp; i++) {
                    AbstractCard c2 = ___p.hand.getTopCard();
                    ___p.hand.moveToDiscardPile(c2);
                    c2.triggerOnManualDiscard();
                    /*
                    if (!PatchTool1.isToRestrain.get(c)) {
                        logger.info("读取"+c.name+"阻止丢弃为"+PatchTool1.isToRestrain.get(c));
                        ___p.hand.moveToDiscardPile(c);
                        c.triggerOnManualDiscard();
                    } else {
                        logger.info("读取"+c.name+"阻止丢弃为"+PatchTool1.isToRestrain.get(c));
                        PatchTool1.isToRestrain.set(c, false);
                    }

                     */
                    GameActionManager.incrementDiscard(false);
                }
                if(cRestrained!=null){
                    AbstractDungeon.player.hand.addToTop(cRestrained);
                }
                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            }
        }
    }

    //随机弃，自动少弃一张
    @SpirePatch(
            clz= DiscardAction.class,
            method="update"
    )
    public static class PatchTool5 {
        @SpireInsertPatch(rloc = 25)
        public static void Insert(DiscardAction __inst,
                                  boolean ___endTurn) {
            //logger.info("进入补丁5");
            if(!___endTurn&&AbstractDungeon.player.hasRelic(
                            ModHelper.makePath(RestrainedHeart.class.getSimpleName()))) {
                AbstractRelic r = AbstractDungeon.player.getRelic(
                        ModHelper.makePath(RestrainedHeart.class.getSimpleName()));
                if (!r.grayscale) {
                    __inst.amount--;
                }
            }
        }
    }

    //选弃，可以少选一张待弃牌
    @SpirePatch(
            clz= DiscardAction.class,
            method="update"
    )
    public static class PatchTool11 {
        @SpireInsertPatch(rloc = 39)
        public static void Insert(DiscardAction __inst,
                                  boolean ___endTurn) {
            //logger.info("进入补丁11");
            if(!___endTurn&&AbstractDungeon.player.hasRelic(
                    ModHelper.makePath(RestrainedHeart.class.getSimpleName()))) {
                PatchTool6.canChooseOneLess.set(AbstractDungeon.handCardSelectScreen,true);
            }
        }
    }

    //重置允许少选一张为假
    @SpirePatch(
            clz= DiscardAction.class,
            method="update"
    )
    public static class PatchTool12 {
        @SpireInsertPatch(rloc = 58)
        public static void Insert(DiscardAction __inst) {
            //logger.info("进入补丁12");
            PatchTool6.canChooseOneLess.set(AbstractDungeon.handCardSelectScreen,false);
        }
    }

    @SpirePatch(
            clz= HandCardSelectScreen.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool6{
        public static SpireField<Boolean> canChooseOneLess= new SpireField<>(() -> false);
        public static SpireField<Boolean> canPickZeroBackup= new SpireField<>(() -> false);
    }

    //点击确认按钮后处理
    @SpirePatch(
            clz= HandCardSelectScreen.class,
            method="update"
    )
    public static class PatchTool7 {
        @SpireInsertPatch(rloc = 50)
        public static SpireReturn<Void> Insert(HandCardSelectScreen __inst, boolean ___forTransform) {
            //logger.info("进入补丁7");
            if (PatchTool6.canChooseOneLess.get(__inst)&&
                    __inst.selectedCards.size() == __inst.numCardsToSelect-1) {
                InputHelper.justClickedLeft = false;
                AbstractDungeon.closeCurrentScreen();
                if (___forTransform && __inst.selectedCards.size() == 1) {
                    if ((AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT) {
                        AbstractDungeon.srcTransformCard(__inst.selectedCards.getBottomCard());
                    } else {
                        AbstractDungeon.transformCard(__inst.selectedCards.getBottomCard());
                    }
                    __inst.selectedCards.group.clear();
                }
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    //激活确认按钮
    @SpirePatch(
            clz= HandCardSelectScreen.class,
            method="refreshSelectedCards"
    )
    public static class PatchTool8 {
        @SpirePostfixPatch
        public static void Postfix(HandCardSelectScreen __inst) {
            //logger.info("进入补丁8,canChooseOneLess="+PatchTool6.canChooseOneLess.get(__inst));
            if (PatchTool6.canChooseOneLess.get(__inst)&&
                    __inst.selectedCards.size() == __inst.numCardsToSelect-1){
                __inst.button.enable();
            }
        }
    }

    //避免失活确认按钮
    @SpirePatch(
            clz= HandCardSelectScreen.class,
            method="updateSelectedCards"
    )
    public static class PatchTool9 {
        @SpireInsertPatch(rloc=41)
        public static void Insert(HandCardSelectScreen __inst) {
            //logger.info("进入补丁9,canChooseOneLess="+PatchTool6.canChooseOneLess.get(__inst));
            PatchTool6.canPickZeroBackup.set(__inst,__inst.canPickZero);
            if (PatchTool6.canChooseOneLess.get(__inst)&&
                    __inst.selectedCards.size() == __inst.numCardsToSelect-1){
                __inst.canPickZero=true;
            }
        }
    }

    //避免失活确认按钮
    @SpirePatch(
            clz= HandCardSelectScreen.class,
            method="updateSelectedCards"
    )
    public static class PatchTool19 {
        @SpirePostfixPatch
        public static void Postfix(HandCardSelectScreen __inst) {
            //logger.info("进入补丁19");
            __inst.canPickZero=PatchTool6.canPickZeroBackup.get(__inst);
        }
    }

    //激活确认按钮
    @SpirePatch(
            clz= HandCardSelectScreen.class,
            method="open",
            paramtypez = {String.class,int.class,boolean.class,boolean.class}
    )
    public static class PatchTool20 {
        @SpireInsertPatch(rloc = 15)
        public static void Insert(HandCardSelectScreen __inst, String msg, int amount,
                                  boolean anyNumber, boolean canPickZero) {
            if (PatchTool6.canChooseOneLess.get(__inst)&&
                    __inst.numCardsToSelect==1){
                __inst.button.isDisabled = true;
                __inst.button.enable();
            }
        }
    }

    //暂时屏蔽快速选牌
    @SpirePatch(
            clz= HandCardSelectScreen.class,
            method="update"
    )
    public static class PatchTool21 {
        @SpireInsertPatch(rloc = 24)
        public static void Insert(HandCardSelectScreen __inst) {
            //logger.info("进入补丁21");
            toStore=Settings.FAST_HAND_CONF;
            if(AbstractDungeon.player.hasRelic(RestrainedHeart.ID)&&__inst.selectionReason.equals(DiscardAction.TEXT[0])){
                Settings.FAST_HAND_CONF = false;
            }
        }
    }

    //暂时屏蔽快速选牌
    @SpirePatch(
            clz= HandCardSelectScreen.class,
            method="update"
    )
    public static class PatchTool22 {
        @SpireInsertPatch(rloc = 33)
        public static void Insert(HandCardSelectScreen __inst) {
            //logger.info("进入补丁22");
            Settings.FAST_HAND_CONF = toStore;
        }
    }

    //暂时屏蔽快速选牌
    @SpirePatch(
            clz= HandCardSelectScreen.class,
            method="startDraggingCardCheck"
    )
    public static class PatchTool23 {
        @SpirePrefixPatch
        public static void Prefix(HandCardSelectScreen __inst) {
            //logger.info("进入补丁23");
            toStore=Settings.FAST_HAND_CONF;
            if(AbstractDungeon.player.hasRelic(RestrainedHeart.ID)&&
                    (__inst.selectionReason.equals(DiscardAction.TEXT[0])||
                            __inst.selectionReason.equals(new RestrainedHeart().DESCRIPTIONS[5]))){
                Settings.FAST_HAND_CONF = false;
            }
        }
    }

    //暂时屏蔽快速选牌
    @SpirePatch(
            clz= HandCardSelectScreen.class,
            method="startDraggingCardCheck"
    )
    public static class PatchTool24 {
        @SpirePostfixPatch
        public static void Postfix(HandCardSelectScreen __inst) {
            //logger.info("进入补丁24");
            Settings.FAST_HAND_CONF = toStore;
        }
    }

    //暂时屏蔽快速选牌
    @SpirePatch(
            clz= HandCardSelectScreen.class,
            method="render"
    )
    public static class PatchTool25 {
        @SpireInsertPatch(rloc = 15)
        public static void Insert(HandCardSelectScreen __inst, SpriteBatch sb) {
            //logger.info("进入补丁25");
            toStore=Settings.FAST_HAND_CONF;
            if(AbstractDungeon.player.hasRelic(RestrainedHeart.ID)&&__inst.selectionReason.equals(DiscardAction.TEXT[0])){
                Settings.FAST_HAND_CONF = false;
            }
        }
    }

    //暂时屏蔽快速选牌
    @SpirePatch(
            clz= HandCardSelectScreen.class,
            method="render"
    )
    public static class PatchTool26 {
        @SpireInsertPatch(rloc=22)
        public static void Insert(HandCardSelectScreen __inst, SpriteBatch sb) {
            //logger.info("进入补丁26");
            Settings.FAST_HAND_CONF = toStore;
        }
    }

    /*
    @SpirePatch(
            clz= HandCardSelectScreen.class,
            method="render"
    )
    public static class PatchTool10 {
        @SpireInsertPatch(rloc = 22)
        public static void Insert(HandCardSelectScreen __inst) {
            if (PatchTool6.canChooseOneLess.get(__inst)&&
                    __inst.selectedCards.size() == __inst.numCardsToSelect-1){
                __inst.button.enable();
            }
        }
    }

     */
}
