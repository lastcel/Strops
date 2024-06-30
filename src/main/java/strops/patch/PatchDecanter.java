package strops.patch;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.KnowingSkull;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.EntropicBrew;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.ui.campfire.RestOption;
import com.megacrit.cardcrawl.ui.campfire.SmithOption;
import com.megacrit.cardcrawl.vfx.ObtainPotionEffect;
import strops.relics.Decanter;
import strops.relics.GlowFeather;

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
            AbstractRelic r2;
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
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
            AbstractRelic r2;
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                            .relicToDisenchant.equals(BustedCrown.ID)){
                r2.flash();
                ((Decanter) r2).decay();
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
                            .relicToDisenchant.equals(FusionHammer.ID)){
                if (option instanceof SmithOption && option.getClass().getName().equals(SmithOption.class.getName())) {
                    ((SmithOption) option).updateUsability(true);
                    r2.flash();
                    ((Decanter) r2).decay();
                    return SpireReturn.Return(true);
                }
            }
            return SpireReturn.Continue();
        }
    }

    //咖啡杯
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
                    ((Decanter) r2).decay();
                    return SpireReturn.Return(true);
                }
            }
            return SpireReturn.Continue();
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
            AbstractRelic r2;
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
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
            AbstractRelic r2;
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
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
            AbstractRelic r2;
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
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


}
