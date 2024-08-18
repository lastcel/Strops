package strops.patch;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StoreRelic;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import strops.helpers.ModHelper;
import strops.relics.ProfiteeringMerchant;
import strops.relics.ProfiteeringMerchantAchieved;
import strops.relics.ProfiteeringMerchantAttempted;

import java.util.ArrayList;

public class PatchProfiteeringMerchant {

    @SpirePatch(
            clz= ShopScreen.class,
            method="init"
    )
    public static class PatchTool1 {
        @SpirePostfixPatch
        public static void Postfix(ShopScreen __inst,
                                   ArrayList<AbstractCard> coloredCards,
                                   ArrayList<AbstractCard> colorlessCards,
                                   ArrayList<StoreRelic> ___relics) {
            for(AbstractRelic r:AbstractDungeon.player.relics){
                if(r.relicId.equals(ModHelper.makePath(
                        ProfiteeringMerchantAttempted.class.getSimpleName()))){
                    for (StoreRelic r2 : ___relics){
                        r2.price = MathUtils.round(
                                r2.price * (1-(float)ProfiteeringMerchant.DISCOUNT_ATTEMPTED.value/100));
                    }
                } else if(r.relicId.equals(ModHelper.makePath(
                        ProfiteeringMerchantAchieved.class.getSimpleName()))){
                    for (StoreRelic r2 : ___relics){
                        r2.price = MathUtils.round(
                                r2.price * (1-(float)ProfiteeringMerchant.DISCOUNT_ACHIEVED.value/100));
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz= ShopScreen.class,
            method="getNewPrice",
            paramtypez = {StoreRelic.class}
    )
    public static class PatchTool2 {
        @SpireInsertPatch(rloc=17,localvars = {"retVal"})
        public static void Insert(ShopScreen __inst,
                                   StoreRelic r,
                                   @ByRef int[] retVal) {
            for(AbstractRelic r1:AbstractDungeon.player.relics){
                if(r1.relicId.equals(ModHelper.makePath(
                        ProfiteeringMerchantAttempted.class.getSimpleName()))){
                    retVal[0]=MathUtils.round(retVal[0] * (1-(float)ProfiteeringMerchant.DISCOUNT_ATTEMPTED.value/100));
                } else if(r1.relicId.equals(ModHelper.makePath(
                        ProfiteeringMerchantAchieved.class.getSimpleName()))){
                    retVal[0]=MathUtils.round(retVal[0] * (1-(float)ProfiteeringMerchant.DISCOUNT_ATTEMPTED.value/100));
                }
            }
        }
    }

    @SpirePatch(clz = ShopScreen.class, method = "initRelics")
    public static class PatchTool3 {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("returnRandomRelicEnd"))
                        m.replace("if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.ProfiteeringMerchantAchieved.ID)) " +
                                "{$1=com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier.RARE;$_ = $proceed($$);}" +
                                " else " +
                                "{$_ = $proceed($$);}");
                }
            };
        }
    }

    @SpirePatch(clz = StoreRelic.class, method = "purchaseRelic")
    public static class PatchTool4 {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("returnRandomRelicEnd"))
                        m.replace("if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.ProfiteeringMerchantAchieved.ID)) " +
                                "{$1=com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier.RARE;$_ = $proceed($$);}" +
                                " else " +
                                "{$_ = $proceed($$);}");
                }
            };
        }
    }

    /*
    @SpirePatch(
            clz= ShopScreen.class,
            method="initRelics"
    )
    public static class PatchTool3 {
        @SpireInsertPatch(rloc=14,localvars = {"tempRelic"})
        public static void Insert(ShopScreen __inst,
                                   @ByRef AbstractRelic[] tempRelic) {
            if(AbstractDungeon.player.hasRelic(
                    ModHelper.makePath(ProfiteeringMerchantAchieved.class.getSimpleName()))&&
                    tempRelic[0].tier != AbstractRelic.RelicTier.RARE){
                tempRelic[0] = returnRandomRelicEnd(AbstractRelic.RelicTier.RARE);
            }
        }
    }

    @SpirePatch(
            clz= StoreRelic.class,
            method="purchaseRelic"
    )
    public static class PatchTool4 {
        @SpireInsertPatch(rloc=33,localvars = {"tempRelic"})
        public static void Insert(StoreRelic __inst,
                                  @ByRef AbstractRelic[] tempRelic) {
            if(AbstractDungeon.player.hasRelic(
                    ModHelper.makePath(ProfiteeringMerchantAchieved.class.getSimpleName()))&&
                    tempRelic[0].tier != AbstractRelic.RelicTier.RARE){
                tempRelic[0] = returnRandomRelicEnd(AbstractRelic.RelicTier.RARE);
            }
        }
    }

     */
}
