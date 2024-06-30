package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import strops.relics.SeaOfMoon;
import strops.utilities.ExtendedCardSave;
import strops.utilities.ExtendedSaveFile;

import java.util.ArrayList;
import java.util.HashMap;

import static basemod.BaseMod.gson;
import static com.megacrit.cardcrawl.helpers.CardLibrary.getCard;

public class PatchGlassRod {

    @SpirePatch(
            clz= AbstractCard.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool1{
        public static SpireField<Boolean> everPreUpgrade= new SpireField<>(() -> false);
    }

    @SpirePatch(
            clz= AbstractCard.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool2{
        public static SpireField<Boolean> everCounted= new SpireField<>(() -> false);
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
                        PatchTool1.everPreUpgrade.get(c), PatchTool2.everCounted.get(c), c.selfRetain));
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
            p.masterDeck.clear();
            for (ExtendedCardSave s : PatchTool3.extendedCards.get(saveFile)) {
                //logger.info(s.id + ", " + s.upgrades);
                p.masterDeck.addToTop(getCopy(s.id, s.upgrades, s.misc,
                        s.savedEverPreUpgrade, s.savedEverCounted, s.isSelfRetain));
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

    public static AbstractCard getCopy(String key, int upgradeTime, int misc,
                                       boolean isEverPreUpgrade, boolean isEverCounted,
                                       boolean isSelfRetain) {
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
        PatchTool1.everPreUpgrade.set(retVal,isEverPreUpgrade);
        PatchTool2.everCounted.set(retVal,isEverCounted);
        retVal.selfRetain=isSelfRetain;
        if(retVal.selfRetain){
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
}
