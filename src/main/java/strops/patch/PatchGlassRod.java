package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.ui.buttons.CancelButton;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import strops.cards.SoulCraftedCard;
import strops.relics.GlassRod;
import strops.utilities.ExtendedCardSave;
import strops.utilities.ExtendedSaveFile;

import java.util.ArrayList;
import java.util.HashMap;

import static basemod.BaseMod.gson;

public class PatchGlassRod {

    /*
    @SpirePatch(
            clz= AbstractCard.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool1{
        public static SpireField<Boolean> everPreUpgrade= new SpireField<>(() -> false);
    }

     */

    @SpirePatch(
            clz= AbstractCard.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool2{
        //public static SpireField<Boolean> everCounted= new SpireField<>(() -> false);
        public static SpireField<Integer> lastSearing= new SpireField<>(() -> -1);
        public static SpireField<Integer> currentSearing= new SpireField<>(() -> -1);
    }

    @SpirePatch(
            clz= SaveFile.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool3{
        public static SpireField<ArrayList<ExtendedCardSave>> extendedCards=
                new SpireField<>(ArrayList::new);
    }

    @SpirePatch(
            clz= SaveFile.class,
            method=SpirePatch.CONSTRUCTOR,
            paramtypez = {SaveFile.SaveType.class}
    )
    public static class PatchTool4 {
        @SpireInsertPatch(rloc = 27,localvars = {"p"})
        public static void Insert(SaveFile __instance, SaveFile.SaveType type, AbstractPlayer p){
            ArrayList<ExtendedCardSave> arr=new ArrayList<>();
            for (AbstractCard c : p.masterDeck.group){
                arr.add(new ExtendedCardSave(c.cardID, c.timesUpgraded, c.misc,
                        /*PatchTool1.everPreUpgrade.get(c), PatchTool2.everCounted.get(c),*/ c.selfRetain));
            }
            PatchTool3.extendedCards.set(__instance,arr);
        }
    }

    @SpirePatch(
            clz= SaveAndContinue.class,
            method="save"
    )
    public static class PatchTool5 {
        @SpireInsertPatch(rloc = 92,localvars = {"params"})
        public static void Insert(SaveFile save,@ByRef HashMap<Object, Object>[] params) {
            params[0].put("extended_cards", PatchTool3.extendedCards.get(save));
        }
    }

    @SpirePatch(
            clz= CardCrawlGame.class,
            method="loadPlayerSave"
    )
    public static class PatchTool6 {
        @SpireInsertPatch(rloc = 70,localvars = {"saveFile"})
        public static void Insert(CardCrawlGame __instance, AbstractPlayer p, SaveFile saveFile){
            if(PatchTool3.extendedCards.get(saveFile)==null){
                return;
            }

            p.masterDeck.clear();
            for (ExtendedCardSave s : PatchTool3.extendedCards.get(saveFile)) {
                //logger.info(s.id + ", " + s.upgrades);
                p.masterDeck.addToTop(myGetCopy(s.id, s.upgrades, s.misc,
                        /*s.savedEverPreUpgrade, s.savedEverCounted,*/ s.isSelfRetain));
            }
        }
    }

    @SpirePatch(
            clz= SaveAndContinue.class,
            method="loadSaveFile",
            paramtypez={String.class}
    )
    public static class PatchTool7 {
        @SpireInsertPatch(rloc=23,localvars={"savestr","saveFile"})
        public static void Insert(String filePath,String savestr,SaveFile saveFile) {
            ExtendedSaveFile saveFilePlus;
            saveFilePlus=gson.fromJson(savestr,ExtendedSaveFile.class);
            PatchTool3.extendedCards.set(saveFile,saveFilePlus.extended_cards);
        }
    }

    @SpirePatch(clz = CancelButton.class, method = "update")
    public static class PatchTool8 {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("reopen"))
                        m.replace("if (strops.relics.GlassRod.canReopenIfCancel) {$_ = $proceed($$);}");
                }
            };
        }
    }

    @SpirePatch(
            clz= CardCrawlGame.class,
            method="update"
    )
    public static class PatchTool9 {
        @SpireInsertPatch(rloc = 107)
        public static void Insert(CardCrawlGame __inst) {
            GlassRod.canReopenIfCancel=true;
        }
    }

    public static AbstractCard myGetCopy(String key, int upgradeTime, int misc,
                                       /*boolean isEverPreUpgrade, boolean isEverCounted,*/
                                       boolean isSelfRetain) {
        if(key.startsWith(SoulCraftedCard.ID)){
            key=SoulCraftedCard.ID;
        }
        AbstractCard retVal = CardLibrary.getCopy(key, upgradeTime, misc);
        retVal.selfRetain = isSelfRetain;
        if (retVal.selfRetain) {
            retVal.initializeDescription();
        }

        return retVal;
    }

        /*
        AbstractCard source = getCard(key);
        AbstractCard retVal;
        if (source == null) {
            retVal = getCard("Madness").makeCopy();
        } else {
            retVal = getCard(key).makeCopy();
        }
        for (int i = 0; i < upgradeTime; i++){
            retVal.upgrade();
        }
        retVal.misc = misc;
        //PatchTool1.everPreUpgrade.set(retVal,isEverPreUpgrade);
        //PatchTool2.everCounted.set(retVal,isEverCounted);
        retVal.selfRetain=isSelfRetain;
        if(retVal.selfRetain){
            retVal.initializeDescription();

         */
            /*
            boolean hasRetainAlready=false;
            for(String s: GameDictionary.RETAIN.NAMES){
                if(retVal.rawDescription.toLowerCase().startsWith(s)||retVal.rawDescription.toLowerCase().startsWith(" "+s)){
                    hasRetainAlready=true;
                    break;
                }
            }
            if(!hasRetainAlready){
                retVal.rawDescription=(new SeaOfMoon()).DESCRIPTIONS[5]+retVal.rawDescription;
                retVal.initializeDescription();
            }

             */
        /*
        }
        if (misc != 0) {
            if (retVal.cardID.equals("Genetic Algorithm")) {
                retVal.block = misc;
                retVal.baseBlock = misc;
                retVal.initializeDescription();
            }
            if (retVal.cardID.equals("RitualDagger")) {
                retVal.damage = misc;
                retVal.baseDamage = misc;
                retVal.initializeDescription();
            }
        }
        return retVal;
    }

         */
}
