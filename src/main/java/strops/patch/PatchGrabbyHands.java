package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import com.megacrit.cardcrawl.ui.buttons.SingingBowlButton;
import strops.modcore.Strops;
import strops.relics.GrabbyHands;

import java.lang.reflect.Field;

public class PatchGrabbyHands {

    /*
    @SpirePatch(
            clz= ProceedButton.class,
            method="update"
    )
    public static class PatchTool1 {
        @SpireInsertPatch(rloc = 20)
        public static void Insert(ProceedButton __inst, Hitbox ___hb) {
            if ((___hb.clicked || CInputActionSet.proceed.isJustPressed())&&
                    AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
                AbstractRelic r;
                if(AbstractDungeon.player.hasRelic(GrabbyHands.ID)&&
                        !((GrabbyHands)(r=AbstractDungeon.player.getRelic(GrabbyHands.ID))).isUsed){
                    r.flash();
                    ((GrabbyHands)r).isUsed=true;
                    ___hb.clicked=false;
                    r.onTrigger();
                }
            }
        }
    }

    @SpirePatch(
            clz= AbstractCard.class,
            method="onChoseThisOption"
    )
    public static class PatchTool2 {
        @SpirePrefixPatch
        public static void Insert(AbstractCard __inst) {
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(__inst,
                    Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        }
    }

    @SpirePatch(
            clz=RewardItem.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool3 {
        @SpireEnum
        public static RewardItem.RewardType GRABBY;
    }

     */

    @SpirePatch(
            clz= CombatRewardScreen.class,
            method="setupItemReward"
    )
    public static class PatchTool1 {
        @SpireInsertPatch(rloc = 17)
        public static void Insert(CombatRewardScreen __inst) {
            if(AbstractDungeon.getCurrRoom() instanceof MonsterRoom &&
                    !(AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) &&
                    AbstractDungeon.player.hasRelic(GrabbyHands.ID)){
                AbstractDungeon.player.getRelic(GrabbyHands.ID).flash();
                for(int i=0;i<GrabbyHands.SIZE.value;i++) {
                    RewardItem cardReward2 = new RewardItem();
                    PatchTool5.isGrabby.set(cardReward2, true);
                    cardReward2.text = (new GrabbyHands()).DESCRIPTIONS[5];
                    for (AbstractCard c : cardReward2.cards) {
                        PatchTool6.isGrabbed.set(c, true);
                        //GrabbyHands.cards.add(c);
                    }

                    if (!cardReward2.cards.isEmpty()) {
                        __inst.rewards.add(cardReward2);
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz= RewardItem.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool5{
        public static SpireField<Boolean> isGrabby = new SpireField<>(() -> false);
    }

    @SpirePatch(
            clz= CombatRewardScreen.class,
            method="setupItemReward"
    )
    public static class PatchTool2 {
        @SpireInsertPatch(rloc = 34)
        public static void Insert(CombatRewardScreen __inst) {
            for(RewardItem r:__inst.rewards){
                if(r.type==RewardItem.RewardType.CARD&&PatchTool5.isGrabby.get(r)){
                    AbstractDungeon.overlayMenu.proceedButton.hide();
                    AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
                    break;
                }
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            }
        }
    }

    @SpirePatch(
            clz= CombatRewardScreen.class,
            method="reopen"
    )
    public static class PatchTool3 {
        @SpirePostfixPatch
        public static void Postfix(CombatRewardScreen __inst) {
            if(__inst.rewards.isEmpty()){
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            } else {
                for(RewardItem r:__inst.rewards){
                    if(r.type==RewardItem.RewardType.CARD&&PatchTool5.isGrabby.get(r)){
                        AbstractDungeon.overlayMenu.proceedButton.hide();
                        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
                        break;
                    }
                    AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                }
            }
        }
    }

    //如何屏蔽其他跳牌按钮有待研究
    @SpirePatch(
            clz= RewardItem.class,
            method="claimReward"
    )
    public static class PatchTool4 {
        @SpireInsertPatch(rloc = 64)
        public static void Insert(RewardItem __inst) {
            if(PatchTool5.isGrabby.get(__inst)){
                //AbstractDungeon.cardRewardScreen.bowlButton.hide();
                try {
                    Field f = CardRewardScreen.class.getDeclaredField("bowlButton");
                    f.setAccessible(true);
                    SingingBowlButton bowl = (SingingBowlButton) f.get(AbstractDungeon.cardRewardScreen);
                    bowl.hide();
                } catch (IllegalAccessException|NoSuchFieldException e) {
                    Strops.logger.info("An exception happened while Grabby Hand's disabling Singing Bowl button!");
                }
            }
        }
    }

    @SpirePatch(
            clz= AbstractCard.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool6{
        public static SpireField<Boolean> isGrabbed=new SpireField<>(() -> false);
    }

    @SpirePatch(
            clz= AbstractCard.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool7{
        public static SpireField<Integer> ages=new SpireField<>(() -> 0);
    }

    @SpirePatch(
            clz= AbstractCard.class,
            method="makeStatEquivalentCopy"
    )
    public static class PatchTool8 {
        @SpireInsertPatch(rloc=20,localvars = {"card"})
        public static void Insert(AbstractCard __inst, AbstractCard card) {
            PatchTool6.isGrabbed.set(card, PatchTool6.isGrabbed.get(__inst));
            PatchTool7.ages.set(card, PatchTool7.ages.get(__inst));
        }
    }

    @SpirePatch(
            clz= CombatRewardScreen.class,
            method="update"
    )
    public static class PatchTool9 {
        @SpirePostfixPatch
        public static void Postfix(CombatRewardScreen __inst) {
            for(RewardItem r:__inst.rewards){
                if(r.type==RewardItem.RewardType.CARD&&PatchTool5.isGrabby.get(r)){
                    AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
                    break;
                }
            }
        }
    }

    @SpirePatch(
            clz= CardRewardScreen.class,
            method="reopen"
    )
    public static class PatchTool10 {
        @SpirePostfixPatch
        public static void Postfix(CardRewardScreen __inst) {
            for(AbstractCard c:__inst.rewardGroup){
                if(PatchTool6.isGrabbed.get(c)){
                    try {
                        Field f = CardRewardScreen.class.getDeclaredField("bowlButton");
                        f.setAccessible(true);
                        SingingBowlButton bowl = (SingingBowlButton) f.get(AbstractDungeon.cardRewardScreen);
                        bowl.hide();
                    } catch (IllegalAccessException|NoSuchFieldException e) {
                        Strops.logger.info("An exception happened while Grabby Hand's redisabling Singing Bowl button!");
                    }
                    break;
                }
            }
        }
    }

    /*
    @SpirePatch(
            clz= CombatRewardScreen.class,
            method="setupItemReward"
    )
    public static class PatchTool8 {
        @SpirePrefixPatch
        public static void Prefix(CombatRewardScreen __inst) {
            logger.info("进入方法setupItemReward");
        }
    }

    @SpirePatch(
            clz= CombatRewardScreen.class,
            method="reopen"
    )
    public static class PatchTool9 {
        @SpirePrefixPatch
        public static void Prefix(CombatRewardScreen __inst) {
            logger.info("进入方法reopen");
        }
    }

    @SpirePatch(
            clz= CombatRewardScreen.class,
            method="open",
            paramtypez = {}
    )
    public static class PatchTool10 {
        @SpirePrefixPatch
        public static void Prefix(CombatRewardScreen __inst) {
            logger.info("进入方法open");
        }
    }

     */
}
