package strops.patch;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.curses.AscendersBane;
import com.megacrit.cardcrawl.cards.curses.CurseOfTheBell;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.KnowingSkull;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.potions.EntropicBrew;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.ui.campfire.RestOption;
import com.megacrit.cardcrawl.ui.campfire.SmithOption;
import com.megacrit.cardcrawl.vfx.ObtainPotionEffect;
import strops.relics.Decanter;
import strops.relics.GlowFeather;
import strops.relics.Wedgue;

import java.util.ArrayList;
import java.util.Iterator;

public class PatchDecanter {

    public static boolean isPreventHasRelic=false;

    //诅咒钥匙
    @SpirePatch(
            clz= CursedKey.class,
            method="onChestOpen"
    )
    public static class PatchTool1 {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(CursedKey __inst, boolean bossChest) {
            if(AbstractDungeon.player.hasRelic(Decanter.ID)){
                AbstractRelic r = AbstractDungeon.player.getRelic(Decanter.ID);
                if(((Decanter)r).relicToDisenchant.equals(CursedKey.ID)){
                    r.flash();
                    ((Decanter) r).decay();
                    return SpireReturn.Return();
                }
            }
            return SpireReturn.Continue();
        }
    }

    //灵体外质
    @SpirePatch(
            clz= AbstractPlayer.class,
            method="gainGold"
    )
    public static class PatchTool2 {
        @SpirePrefixPatch
        public static void Prefix(AbstractPlayer __inst, int amount) {
            if(AbstractDungeon.player.hasRelic(Decanter.ID)){
                AbstractRelic r = AbstractDungeon.player.getRelic(Decanter.ID);
                if(((Decanter)r).relicToDisenchant.equals(Ectoplasm.ID)){
                    r.flash();
                    ((Decanter) r).decay();
                    isPreventHasRelic=true;
                }
            }
        }
    }

    //灵体外质+发光羽毛
    @SpirePatch(
            clz= AbstractPlayer.class,
            method="gainGold"
    )
    public static class PatchTool3 {
        @SpireInsertPatch(rloc=5)
        public static SpireReturn<Void> Insert(AbstractPlayer __inst, int amount) {
            isPreventHasRelic=false;
            if (__inst.hasRelic(GlowFeather.ID)) {
                __inst.getRelic(GlowFeather.ID).flash();
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    //传入伪参数，阻止hasRelic()即检测玩家是否拥有特定遗物的方法判断成功
    @SpirePatch(
            clz= AbstractPlayer.class,
            method="hasRelic"
    )
    public static class PatchTool4 {
        @SpirePrefixPatch
        public static void Prefix(AbstractPlayer __inst, @ByRef String[] targetID) {
            if(isPreventHasRelic){
                targetID[0]="";
            }
        }
    }

    //添水
    @SpirePatch(
            clz= ObtainPotionAction.class,
            method="update"
    )
    public static class PatchTool5 {
        @SpirePrefixPatch
        public static void Prefix(ObtainPotionAction __inst) {
            AbstractRelic r2;
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                            .relicToDisenchant.equals(Sozu.ID)){
                r2.flash();
                ((Decanter) r2).decay();
                isPreventHasRelic=true;
            }
        }
    }

    //添水
    @SpirePatch(
            clz= ObtainPotionAction.class,
            method="update"
    )
    public static class PatchTool6 {
        @SpirePostfixPatch
        public static void Postfix(ObtainPotionAction __inst) {
            isPreventHasRelic=false;
        }
    }

    //添水
    @SpirePatch(
            clz= KnowingSkull.class,
            method="obtainReward"
    )
    public static class PatchTool7 {
        @SpireInsertPatch(rloc=10)
        public static void Insert(KnowingSkull __inst, int slot) {
            AbstractRelic r2;
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                            .relicToDisenchant.equals(Sozu.ID)){
                r2.flash();
                ((Decanter) r2).decay();
                isPreventHasRelic=true;
            }
        }
    }

    //添水
    @SpirePatch(
            clz= KnowingSkull.class,
            method="obtainReward"
    )
    public static class PatchTool8 {
        @SpireInsertPatch(rloc=13)
        public static void Insert(KnowingSkull __inst, int slot) {
            isPreventHasRelic=false;
        }
    }

    //添水
    @SpirePatch(
            clz= EntropicBrew.class,
            method="use"
    )
    public static class PatchTool9 {
        @SpirePrefixPatch
        public static void Prefix(EntropicBrew __inst, AbstractCreature target) {
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)AbstractDungeon.player.getRelic(Decanter.ID))
                            .relicToDisenchant.equals(Sozu.ID)){
                isPreventHasRelic=true;
            }
        }
    }

    //添水
    @SpirePatch(
            clz= EntropicBrew.class,
            method="use"
    )
    public static class PatchTool10 {
        @SpirePostfixPatch
        public static void Postfix(EntropicBrew __inst, AbstractCreature target) {
            isPreventHasRelic=false;
        }
    }

    //添水
    @SpirePatch(
            clz= RewardItem.class,
            method="claimReward"
    )
    public static class PatchTool11 {
        @SpireInsertPatch(rloc=18)
        public static void Insert(RewardItem __inst) {
            AbstractRelic r2;
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                            .relicToDisenchant.equals(Sozu.ID)){
                r2.flash();
                ((Decanter) r2).decay();
                isPreventHasRelic=true;
            }
        }
    }

    //添水
    @SpirePatch(
            clz= RewardItem.class,
            method="claimReward"
    )
    public static class PatchTool12 {
        @SpireInsertPatch(rloc=22)
        public static void Insert(RewardItem __inst) {
            isPreventHasRelic=false;
        }
    }

    //添水
    @SpirePatch(
            clz= StorePotion.class,
            method="purchasePotion"
    )
    public static class PatchTool13 {
        @SpirePrefixPatch
        public static void Prefix(StorePotion __inst) {
            AbstractRelic r2;
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                            .relicToDisenchant.equals(Sozu.ID)){
                r2.flash();
                ((Decanter) r2).decay();
                isPreventHasRelic=true;
            }
        }
    }

    //添水
    @SpirePatch(
            clz= StorePotion.class,
            method="purchasePotion"
    )
    public static class PatchTool14 {
        @SpireInsertPatch(rloc=5)
        public static void Insert(StorePotion __inst) {
            isPreventHasRelic=false;
        }
    }

    //添水
    @SpirePatch(
            clz= ObtainPotionEffect.class,
            method="update"
    )
    public static class PatchTool15 {
        @SpirePrefixPatch
        public static void Prefix(ObtainPotionEffect __inst) {
            AbstractRelic r2;
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                            .relicToDisenchant.equals(Sozu.ID)){
                r2.flash();
                ((Decanter) r2).decay();
                isPreventHasRelic=true;
            }
        }
    }

    //添水
    @SpirePatch(
            clz= ObtainPotionEffect.class,
            method="update"
    )
    public static class PatchTool16 {
        @SpirePostfixPatch
        public static void Postfix(ObtainPotionEffect __inst) {
            isPreventHasRelic=false;
        }
    }

    //破碎金冠
    @SpirePatch(
            clz= BustedCrown.class,
            method="changeNumberOfCardsInReward"
    )
    public static class PatchTool17 {
        @SpirePrefixPatch
        public static SpireReturn<Integer> Prefix(BustedCrown __inst, int numberOfCards) {
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&!AbstractDungeon.player.getRelic(Decanter.ID).grayscale){
                return SpireReturn.Return(numberOfCards);
            }
            return SpireReturn.Continue();
        }
    }

    //融合之锤
    @SpirePatch(
            clz= FusionHammer.class,
            method="canUseCampfireOption"
    )
    public static class PatchTool18 {
        @SpirePrefixPatch
        public static SpireReturn<Boolean> Prefix(FusionHammer __inst, AbstractCampfireOption option) {
            AbstractRelic r2;
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                            .relicToDisenchant.equals(FusionHammer.ID)&&Decanter.isSmithSuppressed()){
                if (option instanceof SmithOption && option.getClass().getName().equals(SmithOption.class.getName())) {
                    ((SmithOption) option).updateUsability(true);
                    r2.flash();
                    //((Decanter) r2).decay();
                    return SpireReturn.Return(true);
                }
            }
            return SpireReturn.Continue();
        }
    }

    //融合之锤
    @SpirePatch(
            clz= SmithOption.class,
            method="useOption"
    )
    public static class PatchTool18_a1 {
        @SpirePostfixPatch
        public static void Postfix(SmithOption __inst) {
            AbstractRelic r2;
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                            .relicToDisenchant.equals(FusionHammer.ID)){
                ((Decanter) r2).decay();
            }
        }
    }

    //咖啡滤杯
    @SpirePatch(
            clz= CoffeeDripper.class,
            method="canUseCampfireOption"
    )
    public static class PatchTool19 {
        @SpirePrefixPatch
        public static SpireReturn<Boolean> Prefix(CoffeeDripper __inst, AbstractCampfireOption option) {
            AbstractRelic r2;
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                            .relicToDisenchant.equals(CoffeeDripper.ID)){
                if (option instanceof RestOption && option.getClass().getName().equals(RestOption.class.getName())) {
                    ((RestOption) option).updateUsability(true);
                    r2.flash();
                    //((Decanter) r2).decay();
                    return SpireReturn.Return(true);
                }
            }
            return SpireReturn.Continue();
        }
    }

    //融合之锤
    @SpirePatch(
            clz= RestOption.class,
            method="useOption"
    )
    public static class PatchTool19_a1 {
        @SpirePostfixPatch
        public static void Postfix(RestOption __inst) {
            AbstractRelic r2;
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                            .relicToDisenchant.equals(CoffeeDripper.ID)){
                ((Decanter) r2).decay();
            }
        }
    }

    //符文圆顶
    @SpirePatch(
            clz= AbstractMonster.class,
            method="renderTip"
    )
    public static class PatchTool20 {
        @SpirePrefixPatch
        public static void Prefix(AbstractMonster __inst, SpriteBatch sb) {
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)AbstractDungeon.player.getRelic(Decanter.ID))
                            .relicToDisenchant.equals(RunicDome.ID)){
                isPreventHasRelic=true;
            }
        }
    }

    //符文圆顶
    @SpirePatch(
            clz= AbstractMonster.class,
            method="renderTip"
    )
    public static class PatchTool21 {
        @SpireInsertPatch(rloc = 6)
        public static void Insert(AbstractMonster __inst, SpriteBatch sb) {
            isPreventHasRelic=false;
        }
    }

    //符文圆顶
    @SpirePatch(
            clz= AbstractMonster.class,
            method="render"
    )
    public static class PatchTool22 {
        @SpireInsertPatch(rloc = 54)
        public static void Insert(AbstractMonster __inst, SpriteBatch sb) {
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)AbstractDungeon.player.getRelic(Decanter.ID))
                            .relicToDisenchant.equals(RunicDome.ID)){
                isPreventHasRelic=true;
            }
        }
    }

    //符文圆顶
    @SpirePatch(
            clz= AbstractMonster.class,
            method="render"
    )
    public static class PatchTool23 {
        @SpireInsertPatch(rloc = 65)
        public static void Insert(AbstractMonster __inst, SpriteBatch sb) {
            isPreventHasRelic=false;
        }
    }

    //贤者之石
    @SpirePatch(
            clz= PhilosopherStone.class,
            method="atBattleStart"
    )
    public static class PatchTool24 {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(PhilosopherStone __inst) {
            AbstractRelic r2;
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                            .relicToDisenchant.equals(PhilosopherStone.ID)){
                r2.flash();
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    //贤者之石
    @SpirePatch(
            clz= PhilosopherStone.class,
            method="onSpawnMonster"
    )
    public static class PatchTool25 {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(PhilosopherStone __inst, AbstractMonster monster) {
            AbstractRelic r2;
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                            .relicToDisenchant.equals(PhilosopherStone.ID)){
                r2.flash();
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    //天鹅绒颈圈
    @SpirePatch(
            clz= VelvetChoker.class,
            method="canPlay"
    )
    public static class PatchTool26 {
        @SpirePrefixPatch
        public static SpireReturn<Boolean> Prefix(VelvetChoker __inst, AbstractCard card) {
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)AbstractDungeon.player.getRelic(Decanter.ID))
                            .relicToDisenchant.equals(VelvetChoker.ID)){
                return SpireReturn.Return(true);
            }
            return SpireReturn.Continue();
        }
    }

    //悬浮风筝
    @SpirePatch(
            clz= HoveringKite.class,
            method="atTurnStart"
    )
    public static class PatchTool27 {
        @SpirePostfixPatch
        public static void Postfix(HoveringKite __inst, @ByRef boolean[] ___triggeredThisTurn) {
            AbstractRelic r2;
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                            .relicToDisenchant.equals(HoveringKite.ID)){
                r2.flash();
                ___triggeredThisTurn[0]=true;
                AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
            }
        }
    }

    //奴隶贩子颈环
    @SpirePatch(
            clz= SlaversCollar.class,
            method="beforeEnergyPrep"
    )
    public static class PatchTool28 {
        @SpireInsertPatch(rloc=7,localvars = {"isEliteOrBoss"})
        public static void Insert(SlaversCollar __inst, @ByRef boolean[] isEliteOrBoss) {
            if(!isEliteOrBoss[0]){
                AbstractRelic r2;
                if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                        ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                                .relicToDisenchant.equals(SlaversCollar.ID)){
                    r2.beginLongPulse();
                    isEliteOrBoss[0]=true;
                }
            }
        }
    }

    //痛楚印记
    @SpirePatch(
            clz= MarkOfPain.class,
            method="atBattleStart"
    )
    public static class PatchTool29 {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(MarkOfPain __inst) {
            AbstractRelic r2;
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                            .relicToDisenchant.equals(MarkOfPain.ID)){
                r2.flash();
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz=AbstractDungeon.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool30 {
        @SpireEnum
        public static AbstractDungeon.CurrentScreen DECANTER_SELECT;
    }

    //冻结核心
    @SpirePatch(
            clz= FrozenCore.class,
            method="onPlayerEndTurn"
    )
    public static class PatchTool31 {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(FrozenCore __inst) {
            AbstractRelic r2;
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                            .relicToDisenchant.equals(FrozenCore.ID)){
                r2.flash();
                __inst.flash();
                AbstractDungeon.player.channelOrb(new Frost());
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    //异蛇之眼
    @SpirePatch(
            clz= SneckoEye.class,
            method="atPreBattle"
    )
    public static class PatchTool32 {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(SneckoEye __inst) {
            AbstractRelic r2;
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                            .relicToDisenchant.equals(SneckoEye.ID)){
                r2.flash();
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    //召唤铃铛
    @SpirePatch(
            clz= CardGroup.class,
            method="initializeDeck"
    )
    public static class PatchTool33 {
        @SpirePostfixPatch
        public static void Postfix(CardGroup __inst, CardGroup masterDeck) {
            Decanter r2;
            if(AbstractDungeon.player.hasRelic(Decanter.ID)){
                r2 = (Decanter)(AbstractDungeon.player.getRelic(Decanter.ID));
                if(r2.relicToDisenchant.equals(CallingBell.ID)){
                    r2.flash();

                    Iterator<AbstractCard> iterator = __inst.group.iterator();
                    while (iterator.hasNext()){
                        AbstractCard c=iterator.next();
                        if(c.cardID.equals(CurseOfTheBell.ID)){
                            iterator.remove();
                            break;
                        }
                    }
                } else if(r2.relicToDisenchant.equals(Wedgue.ID)){
                    r2.flash();

                    Iterator<AbstractCard> iterator = __inst.group.iterator();
                    while (iterator.hasNext()){
                        AbstractCard c=iterator.next();
                        if(c.cardID.equals(AscendersBane.ID)){
                            iterator.remove();
                            break;
                        }
                    }
                }
            }
        }
    }

    //破碎金冠
    @SpirePatch(
            clz=AbstractCard.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool34{
        public static SpireField<Boolean> isBusted=new SpireField<>(() -> false);
    }

    /*
    //破碎金冠
    @SpirePatch(
            clz= AbstractDungeon.class,
            method="getRewardCards"
    )
    public static class PatchTool35 {
        @SpireInsertPatch(rloc=55,localvars = {"numCards","i","card"})
        public static void Insert(int numCards, int i, AbstractCard card) {
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&i>=numCards-2*getNumOfCrowns()){
                Strops.logger.info("第一次破碎卡牌："+card.name);
                PatchTool34.isBusted.set(card,true);
            }
        }
    }

     */

    //破碎金冠
    @SpirePatch(
            clz= AbstractDungeon.class,
            method="getRewardCards"
    )
    public static class PatchTool36 {
        @SpireInsertPatch(rloc=64,localvars = {"numCards","retVal2"})
        public static void Insert(int numCards, ArrayList<AbstractCard> retVal2) {
            if(!AbstractDungeon.player.hasRelic(Decanter.ID)||AbstractDungeon.player.getRelic(Decanter.ID).grayscale){
                return;
            }
            for(AbstractCard c:retVal2){
                //Strops.logger.info("第二次待破碎卡牌："+c.name+"，对应第一次的"+retVal.get(retVal2.indexOf(c))+"，确认破碎="+PatchTool34.isBusted.get(retVal.get(retVal2.indexOf(c))));
                //PatchTool34.isBusted.set(c,PatchTool34.isBusted.get(retVal.get(retVal2.indexOf(c))));
                if(retVal2.indexOf(c)>=numCards-2*getNumOfCrowns()){
                    PatchTool34.isBusted.set(c,true);
                }
            }
        }
    }

    /*
    //破碎金冠
    @SpirePatch(
            clz= AbstractDungeon.class,
            method="getColorlessRewardCards"
    )
    public static class PatchTool36 {
        @SpireInsertPatch(rloc=37,localvars = {"numCards","i","card"})
        public static void Insert(int numCards, int i, AbstractCard card) {
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&i>=numCards-2*getNumOfCrowns()){
                PatchTool34.isBusted.set(card,true);
            }
        }
    }

     */

    //破碎金冠
    @SpirePatch(
            clz= AbstractCard.class,
            method="update"
    )
    public static class PatchTool37 {
        @SpirePrefixPatch
        public static void Prefix(AbstractCard __inst) {
            if(PatchTool34.isBusted.get(__inst)){
                __inst.drawScale=0.5f;
            }
        }
    }

    /*
    //破碎金冠
    @SpirePatch(
            clz= FastCardObtainEffect.class,
            method=SpirePatch.CONSTRUCTOR
    )
    public static class PatchTool38 {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(FastCardObtainEffect __inst, AbstractCard card, float x, float y) {
            if(!PatchTool34.isBusted.get(card)){
                return SpireReturn.Continue();
            }
            AbstractRelic r2;
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                            .relicToDisenchant.equals(BustedCrown.ID)){
                r2.flash();
                ((Decanter)r2).decay();
                PatchTool34.isBusted.set(card,false);
                return SpireReturn.Continue();
            }
            __inst.isDone=true;
            return SpireReturn.Return();
        }
    }

     */

    //破碎金冠
    @SpirePatch(
            clz= CardRewardScreen.class,
            method="cardSelectUpdate"
    )
    public static class PatchTool38 {
        @SpireInsertPatch(rloc=30,localvars = {"hoveredCard"})
        public static SpireReturn<Void> Insert(CardRewardScreen __inst, AbstractCard hoveredCard) {
            if(!PatchTool34.isBusted.get(hoveredCard)){
                return SpireReturn.Continue();
            }
            AbstractRelic r2;
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                            .relicToDisenchant.equals(BustedCrown.ID)){
                r2.flash();
                ((Decanter)r2).decay();
                PatchTool34.isBusted.set(hoveredCard,false);
                return SpireReturn.Continue();
            }
            return SpireReturn.Return();
        }
    }

    /*
    @SpirePatch(
            clz= TheBeyond.class,
            method="update",
            paramtypez = {AbstractPlayer.class, ArrayList.class}
    )
    public static class PatchTool30 {
        @SpirePostfixPatch
        public static void Postfix(TheBeyond __inst, AbstractPlayer p, ArrayList<String> theList) {
            AbstractDungeon.currMapNode = new MapRoomNode(0, -1);
            AbstractDungeon.currMapNode.room = new EmptyRoom();
        }
    }

     */


    /*
    @SpirePatch(
            clz= RelicSelectScreen.class,
            method="renderList"
    )
    public static class PatchTool5 {
        @SpirePrefixPatch
        public static void Prefix(RelicSelectScreen __inst, SpriteBatch sb, ArrayList<AbstractRelic> list) {
            logger.info("进入绝地mod渲染遗物列表");
        }
    }

     */

    /*
    @SpirePatch(
            clz= AbstractPlayer.class,
            method="renderRelics"
    )
    public static class PatchTool6 {
        @SpireInsertPatch(rloc=2,localvars = {"i"})
        public static void Insert(AbstractPlayer __inst, SpriteBatch sb, int i) {
            logger.info("渲染顶部遗物："+__inst.relics.get(i).relicId);
        }
    }

     */

    private static int getNumOfCrowns(){
        int count=0;
        for(AbstractRelic r:AbstractDungeon.player.relics){
            if(r.relicId.equals(BustedCrown.ID)){
                count++;
            }
        }
        return count;
    }
}
