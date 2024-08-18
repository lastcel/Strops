package strops.patch;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.shop.OnSaleTag;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.shop.StoreRelic;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;
import strops.relics.GlowFeather;

import java.lang.reflect.Field;
import java.util.*;

import static com.megacrit.cardcrawl.shop.ShopScreen.actualPurgeCost;

public class PatchGlowFeather {
    public static Set<Object> freeItems=new HashSet<>();
    public static ArrayList<Object> allItems=new ArrayList<>();
    public static Integer purgeFlag=0;

    @SpirePatch(clz = ShopScreen.class, method = "purchaseCard")
    public static class PatchTool1 {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(FieldAccess m) throws CannotCompileException {
                    if (m.getFieldName().equals("gold"))
                        m.replace("if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)) {$_=com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.currentHealth;} else {$_ = $proceed($$);}");
                }
            };
        }
    }

    @SpirePatch(clz = ShopScreen.class, method = "purchaseCard")
    public static class PatchTool2 {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(FieldAccess m) throws CannotCompileException {
                    if (m.getFieldName().equals("price")&&m.getLineNumber()-m.where().getMethodInfo().getLineNumber(0)==0)
                        m.replace("if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)&&!strops.patch.PatchGlowFeather.freeItems.contains(hoveredCard))" +
                                " {$_=com.badlogic.gdx.math.MathUtils.floor(hoveredCard.price*strops.relics.GlowFeather.RATIO.value*0.01f)+1;}" +
                                " else if(com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)&&strops.patch.PatchGlowFeather.freeItems.contains(hoveredCard)) " +
                                "{$_=1;}" +
                                " else " +
                                "{$_ = $proceed($$);}");
                }
            };
        }
    }

    @SpirePatch(clz = ShopScreen.class, method = "purchaseCard")
    public static class PatchTool3 {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("loseGold"))
                        m.replace("if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)&&!strops.patch.PatchGlowFeather.freeItems.contains(hoveredCard)) " +
                                "{com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.damage(new com.megacrit.cardcrawl.cards.DamageInfo(null, com.badlogic.gdx.math.MathUtils.floor(hoveredCard.price*strops.relics.GlowFeather.RATIO.value*0.01f), com.megacrit.cardcrawl.cards.DamageInfo.DamageType.HP_LOSS));} " +
                                "else if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)&&strops.patch.PatchGlowFeather.freeItems.contains(hoveredCard)) " +
                                "{} " +
                                "else " +
                                "{$_ = $proceed($$);}");
                }
            };
        }
    }

    @SpirePatch(clz = ShopScreen.class, method = "renderCardsAndPrices")
    public static class PatchTool4 {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("draw"))
                        m.replace("if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)) {$1=com.megacrit.cardcrawl.helpers.ImageMaster.TP_HP;$_ = $proceed($$);} else {$_ = $proceed($$);}");
                }
            };
        }
    }

    @SpirePatch(clz = ShopScreen.class, method = "renderCardsAndPrices")
    public static class PatchTool5 {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("renderFontLeftTopAligned"))
                        m.replace("if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)&&!strops.patch.PatchGlowFeather.freeItems.contains(c))" +
                                " {$3=Integer.toString(com.badlogic.gdx.math.MathUtils.floor(c.price*strops.relics.GlowFeather.RATIO.value*0.01f));$_ = $proceed($$);}" +
                                " else if(com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)&&strops.patch.PatchGlowFeather.freeItems.contains(c))" +
                                " {$3=\"0\";$_ = $proceed($$);}" +
                                " else" +
                                " {$_ = $proceed($$);}");
                }
            };
        }
    }

    /*↑basemod.BaseMod.logger.info("情况为"+strops.patch.PatchGlowFeather.PatchTool8.isFree.get(c));*/


    @SpirePatch(
            clz= ShopScreen.class,
            method="renderCardsAndPrices"
    )
    public static class PatchTool6 {
        @SpireInsertPatch(rloc = 21,localvars = {"c","color"})
        public static void Insert(ShopScreen __inst, SpriteBatch sb, OnSaleTag ___saleTag, AbstractCard c, @ByRef Color[] color) {
            if(AbstractDungeon.player.hasRelic(GlowFeather.ID)){
                color[0]=Color.WHITE.cpy();
                if(!freeItems.contains(c)&&MathUtils.floor(c.price*GlowFeather.RATIO.value*0.01f)>=AbstractDungeon.player.currentHealth){
                    color[0] = Color.SALMON.cpy();
                } else if (c.equals(___saleTag.card)) {
                    color[0] = Color.SKY.cpy();
                }
            }
        }
    }

    @SpirePatch(
            clz= ShopScreen.class,
            method="renderCardsAndPrices"
    )
    public static class PatchTool7 {
        @SpireInsertPatch(rloc = 52,localvars = {"c","color"})
        public static void Insert(ShopScreen __inst, SpriteBatch sb, OnSaleTag ___saleTag, AbstractCard c, @ByRef Color[] color) {
            if(AbstractDungeon.player.hasRelic(GlowFeather.ID)){
                color[0]=Color.WHITE.cpy();
                if(!freeItems.contains(c)&&MathUtils.floor(c.price*GlowFeather.RATIO.value*0.01f)>=AbstractDungeon.player.currentHealth){
                    color[0] = Color.SALMON.cpy();
                } else if (c.equals(___saleTag.card)) {
                    color[0] = Color.SKY.cpy();
                }
            }
        }
    }

    @SpirePatch(
            clz= ShopScreen.class,
            method="init"
    )
    public static class PatchTool8 {
        @SpirePostfixPatch
        public static void Postfix(ShopScreen __inst, ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards,
                                   ArrayList<StoreRelic> ___relics, ArrayList<StorePotion> ___potions) {
            allItems.clear();
            freeItems.clear();
            allItems.addAll(__inst.coloredCards);
            allItems.addAll(__inst.colorlessCards);
            allItems.addAll(___relics);
            allItems.addAll(___potions);
            if (!ModHelper.isModEnabled("Hoarder")){
                allItems.add(purgeFlag);
            }
            Collections.shuffle(allItems,new Random(AbstractDungeon.miscRng.randomLong()));
            for(int i=0;i<GlowFeather.SCOPE.value&&i<allItems.size();i++){
                freeItems.add(allItems.get(i));
            }
        }
    }

    @SpirePatch(
            clz= StoreRelic.class,
            method="purchaseRelic"
    )
    public static class PatchTool9 {
        @SpireInsertPatch(rloc = 8)
        public static void Insert(StoreRelic __inst, ShopScreen ___shopScreen) {
            freeItems.clear();
            if(__inst.relic.relicId.equals(GlowFeather.ID)){
                allItems.clear();
                allItems.addAll(___shopScreen.coloredCards);
                allItems.addAll(___shopScreen.colorlessCards);

                try {
                    Field f = ShopScreen.class.getDeclaredField("relics");
                    f.setAccessible(true);
                    if(f.get(___shopScreen) instanceof ArrayList<?>){
                        ArrayList<?> copiedRelics=(ArrayList<?>)f.get(___shopScreen);
                        allItems.addAll(copiedRelics);
                        allItems.remove(__inst);
                    }
                } catch (IllegalAccessException|NoSuchFieldException e) {
                    e.printStackTrace();
                }

                try {
                    Field f = ShopScreen.class.getDeclaredField("potions");
                    f.setAccessible(true);
                    if(f.get(___shopScreen) instanceof ArrayList<?>){
                        ArrayList<?> copiedPotions=(ArrayList<?>)f.get(___shopScreen);
                        allItems.addAll(copiedPotions);
                    }
                } catch (IllegalAccessException|NoSuchFieldException e) {
                    e.printStackTrace();
                }

                if(___shopScreen.purgeAvailable){
                    allItems.add(purgeFlag);
                }

                Collections.shuffle(allItems,new Random(AbstractDungeon.miscRng.randomLong()));
                for(int i=0;i<GlowFeather.SCOPE.value&&i<allItems.size();i++){
                    freeItems.add(allItems.get(i));
                }

                /*
                for(Object o:freeItems){
                    Strops.logger.info("免费物品：{}", o.getClass().getSimpleName());
                }

                 */
            }
        }
    }

    @SpirePatch(
            clz= ShopScreen.class,
            method="purchaseCard"
    )
    public static class PatchTool10 {
        @SpireInsertPatch(rloc = 5)
        public static void Insert(ShopScreen __inst, AbstractCard hoveredCard) {
            freeItems.clear();
        }
    }

    @SpirePatch(
            clz= StorePotion.class,
            method="purchasePotion"
    )
    public static class PatchTool11 {
        @SpireInsertPatch(rloc = 8)
        public static void Insert(StorePotion __inst) {
            freeItems.clear();
        }
    }

    @SpirePatch(
            clz= ShopScreen.class,
            method="purgeCard"
    )
    public static class PatchTool12 {
        @SpireInsertPatch(rloc = 1)
        public static void Insert() {
            freeItems.clear();
        }
    }

    @SpirePatch(clz = StoreRelic.class, method = "purchaseRelic")
    public static class PatchTool13 {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(FieldAccess m) throws CannotCompileException {
                    if (m.getFieldName().equals("gold"))
                        m.replace("if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)) {$_=com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.currentHealth;} else {$_ = $proceed($$);}");
                }
            };
        }
    }

    @SpirePatch(clz = StoreRelic.class, method = "purchaseRelic")
    public static class PatchTool14 {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(FieldAccess m) throws CannotCompileException {
                    if (m.getFieldName().equals("price")&&m.getLineNumber()-m.where().getMethodInfo().getLineNumber(0)==0)
                        m.replace("if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)&&!strops.patch.PatchGlowFeather.freeItems.contains(this))" +
                                " {$_=com.badlogic.gdx.math.MathUtils.floor(this.price*strops.relics.GlowFeather.RATIO.value*0.01f)+1;}" +
                                " else if(com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)&&strops.patch.PatchGlowFeather.freeItems.contains(this)) " +
                                "{$_=1;}" +
                                " else " +
                                "{$_ = $proceed($$);}");
                }
            };
        }
    }

    @SpirePatch(clz = StoreRelic.class, method = "purchaseRelic")
    public static class PatchTool15 {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("loseGold"))
                        m.replace("if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)&&!strops.patch.PatchGlowFeather.freeItems.contains(this)) " +
                                "{com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.damage(new com.megacrit.cardcrawl.cards.DamageInfo(null, com.badlogic.gdx.math.MathUtils.floor(this.price*strops.relics.GlowFeather.RATIO.value*0.01f), com.megacrit.cardcrawl.cards.DamageInfo.DamageType.HP_LOSS));} " +
                                "else if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)&&strops.patch.PatchGlowFeather.freeItems.contains(this)) " +
                                "{} " +
                                "else " +
                                "{$_ = $proceed($$);}");
                }
            };
        }
    }

    @SpirePatch(clz = StoreRelic.class, method = "render")
    public static class PatchTool16 {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("draw"))
                        m.replace("if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)) {$1=com.megacrit.cardcrawl.helpers.ImageMaster.TP_HP;$_ = $proceed($$);} else {$_ = $proceed($$);}");
                }
            };
        }
    }

    @SpirePatch(clz = StoreRelic.class, method = "render")
    public static class PatchTool17 {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("renderFontLeftTopAligned"))
                        m.replace("if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)&&!strops.patch.PatchGlowFeather.freeItems.contains(this))" +
                                " {$3=Integer.toString(com.badlogic.gdx.math.MathUtils.floor(this.price*strops.relics.GlowFeather.RATIO.value*0.01f));$_ = $proceed($$);}" +
                                " else if(com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)&&strops.patch.PatchGlowFeather.freeItems.contains(this))" +
                                " {$3=\"0\";$_ = $proceed($$);}" +
                                " else" +
                                " {$_ = $proceed($$);}");
                }
            };
        }
    }

    @SpirePatch(
            clz= StoreRelic.class,
            method="render"
    )
    public static class PatchTool18 {
        @SpireInsertPatch(rloc = 16,localvars = {"color"})
        public static void Insert(StoreRelic __inst, SpriteBatch sb, @ByRef Color[] color) {
            if(AbstractDungeon.player.hasRelic(GlowFeather.ID)){
                color[0]=Color.WHITE;
                if(!freeItems.contains(__inst)&&MathUtils.floor(__inst.price*GlowFeather.RATIO.value*0.01f)>=AbstractDungeon.player.currentHealth){
                    color[0] = Color.SALMON;
                }
            }
        }
    }

    @SpirePatch(clz = StorePotion.class, method = "purchasePotion")
    public static class PatchTool19 {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(FieldAccess m) throws CannotCompileException {
                    if (m.getFieldName().equals("gold"))
                        m.replace("if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)) {$_=com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.currentHealth;} else {$_ = $proceed($$);}");
                }
            };
        }
    }

    @SpirePatch(clz = StorePotion.class, method = "purchasePotion")
    public static class PatchTool20 {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(FieldAccess m) throws CannotCompileException {
                    if (m.getFieldName().equals("price")&&m.getLineNumber()-m.where().getMethodInfo().getLineNumber(0)==5)
                        m.replace("if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)&&!strops.patch.PatchGlowFeather.freeItems.contains(this))" +
                                " {$_=com.badlogic.gdx.math.MathUtils.floor(this.price*strops.relics.GlowFeather.RATIO.value*0.01f)+1;}" +
                                " else if(com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)&&strops.patch.PatchGlowFeather.freeItems.contains(this)) " +
                                "{$_=1;}" +
                                " else " +
                                "{$_ = $proceed($$);}");
                }
            };
        }
    }

    @SpirePatch(clz = StorePotion.class, method = "purchasePotion")
    public static class PatchTool21 {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("loseGold"))
                        m.replace("if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)&&!strops.patch.PatchGlowFeather.freeItems.contains(this)) " +
                                "{com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.damage(new com.megacrit.cardcrawl.cards.DamageInfo(null, com.badlogic.gdx.math.MathUtils.floor(this.price*strops.relics.GlowFeather.RATIO.value*0.01f), com.megacrit.cardcrawl.cards.DamageInfo.DamageType.HP_LOSS));} " +
                                "else if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)&&strops.patch.PatchGlowFeather.freeItems.contains(this)) " +
                                "{} " +
                                "else " +
                                "{$_ = $proceed($$);}");
                }
            };
        }
    }

    @SpirePatch(clz = StorePotion.class, method = "render")
    public static class PatchTool22 {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("draw"))
                        m.replace("if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)) {$1=com.megacrit.cardcrawl.helpers.ImageMaster.TP_HP;$_ = $proceed($$);} else {$_ = $proceed($$);}");
                }
            };
        }
    }

    @SpirePatch(clz = StorePotion.class, method = "render")
    public static class PatchTool23 {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("renderFontLeftTopAligned"))
                        m.replace("if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)&&!strops.patch.PatchGlowFeather.freeItems.contains(this))" +
                                " {$3=Integer.toString(com.badlogic.gdx.math.MathUtils.floor(this.price*strops.relics.GlowFeather.RATIO.value*0.01f));$_ = $proceed($$);}" +
                                " else if(com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)&&strops.patch.PatchGlowFeather.freeItems.contains(this))" +
                                " {$3=\"0\";$_ = $proceed($$);}" +
                                " else" +
                                " {$_ = $proceed($$);}");
                }
            };
        }
    }

    @SpirePatch(
            clz= StorePotion.class,
            method="render"
    )
    public static class PatchTool24 {
        @SpireInsertPatch(rloc = 16,localvars = {"color"})
        public static void Insert(StorePotion __inst, SpriteBatch sb, @ByRef Color[] color) {
            if(AbstractDungeon.player.hasRelic(GlowFeather.ID)){
                color[0]=Color.WHITE;
                if(!freeItems.contains(__inst)&&MathUtils.floor(__inst.price*GlowFeather.RATIO.value*0.01f)>=AbstractDungeon.player.currentHealth){
                    color[0] = Color.SALMON;
                }
            }
        }
    }

    @SpirePatch(clz = ShopScreen.class, method = "purchasePurge")
    public static class PatchTool25 {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(FieldAccess m) throws CannotCompileException {
                    if (m.getFieldName().equals("gold"))
                        m.replace("if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)) {$_=com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.currentHealth;} else {$_ = $proceed($$);}");
                }
            };
        }
    }

    @SpirePatch(clz = ShopScreen.class, method = "purchasePurge")
    public static class PatchTool26 {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(FieldAccess m) throws CannotCompileException {
                    if (m.getFieldName().equals("actualPurgeCost"))
                        m.replace("if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)&&!strops.patch.PatchGlowFeather.freeItems.contains(strops.patch.PatchGlowFeather.purgeFlag))" +
                                " {$_=com.badlogic.gdx.math.MathUtils.floor(actualPurgeCost*strops.relics.GlowFeather.RATIO.value*0.01f)+1;}" +
                                " else if(com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)&&strops.patch.PatchGlowFeather.freeItems.contains(strops.patch.PatchGlowFeather.purgeFlag)) " +
                                "{$_=1;}" +
                                " else " +
                                "{$_ = $proceed($$);}");
                }
            };
        }
    }

    @SpirePatch(clz = ShopScreen.class, method = "purgeCard")
    public static class PatchTool27 {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("loseGold"))
                        m.replace("if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)&&!strops.patch.PatchGlowFeather.freeItems.contains(strops.patch.PatchGlowFeather.purgeFlag)) " +
                                "{com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.damage(new com.megacrit.cardcrawl.cards.DamageInfo(null, com.badlogic.gdx.math.MathUtils.floor(actualPurgeCost*strops.relics.GlowFeather.RATIO.value*0.01f), com.megacrit.cardcrawl.cards.DamageInfo.DamageType.HP_LOSS));} " +
                                "else if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)&&strops.patch.PatchGlowFeather.freeItems.contains(strops.patch.PatchGlowFeather.purgeFlag)) " +
                                "{} " +
                                "else " +
                                "{$_ = $proceed($$);}");
                }
            };
        }
    }

    @SpirePatch(clz = ShopScreen.class, method = "renderPurge")
    public static class PatchTool28 {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("draw")&&m.getLineNumber()-m.where().getMethodInfo().getLineNumber(0)==35)
                        m.replace("if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)) {$1=com.megacrit.cardcrawl.helpers.ImageMaster.TP_HP;$_ = $proceed($$);} else {$_ = $proceed($$);}");
                }
            };
        }
    }

    @SpirePatch(clz = ShopScreen.class, method = "renderPurge")
    public static class PatchTool29 {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("renderFontLeftTopAligned"))
                        m.replace("if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)&&!strops.patch.PatchGlowFeather.freeItems.contains(strops.patch.PatchGlowFeather.purgeFlag))" +
                                " {$3=Integer.toString(com.badlogic.gdx.math.MathUtils.floor(actualPurgeCost*strops.relics.GlowFeather.RATIO.value*0.01f));$_ = $proceed($$);}" +
                                " else if(com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.GlowFeather.ID)&&strops.patch.PatchGlowFeather.freeItems.contains(strops.patch.PatchGlowFeather.purgeFlag))" +
                                " {$3=\"0\";$_ = $proceed($$);}" +
                                " else" +
                                " {$_ = $proceed($$);}");
                }
            };
        }
    }

    @SpirePatch(
            clz= ShopScreen.class,
            method="renderPurge"
    )
    public static class PatchTool30 {
        @SpireInsertPatch(rloc = 50,localvars = {"color"})
        public static void Insert(ShopScreen __inst, SpriteBatch sb, @ByRef Color[] color) {
            if(AbstractDungeon.player.hasRelic(GlowFeather.ID)){
                color[0]=Color.WHITE;
                if(!freeItems.contains(purgeFlag)&&MathUtils.floor(actualPurgeCost*GlowFeather.RATIO.value*0.01f)>=AbstractDungeon.player.currentHealth){
                    color[0] = Color.SALMON;
                }
            }
        }
    }

    /*
    @SpirePatch(
            clz= AbstractPlayer.class,
            method="gainGold"
    )
    public static class PatchTool31 {
        @SpireInsertPatch(rloc = 5)
        public static SpireReturn<Void> Insert(AbstractPlayer __inst, int amount) {
            if (__inst.hasRelic(GlowFeather.ID)) {
                __inst.getRelic(GlowFeather.ID).flash();
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

     */




    /*
    @SpirePatch(
            clz= AbstractCard.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool8{
        public static SpireField<Boolean> isFree= new SpireField<>(() -> false);
    }

     */

    /*
    @SpirePatch(
            clz= AbstractCard.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool3{
        public static SpireField<Integer> glowPrice= new SpireField<>(() -> 0);
    }

    @SpirePatch(clz = ShopScreen.class, method = "update")
    public static class PatchTool4 {
        @SpirePostfixPatch
        public static void Postfix(ShopScreen __inst) {
            for(AbstractCard c:__inst.coloredCards){
                PatchTool3.glowPrice.set(c, MathUtils.floor(c.price*0.15f));
            }
        }
    }

     */
}
