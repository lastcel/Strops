package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.unique.RestoreRetainedCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import strops.relics.SeaOfMoon;

public class PatchSeaOfMoon {

    @SpirePatch(
            clz= AbstractCard.class,
            method="makeStatEquivalentCopy"
    )
    public static class PatchTool1 {
        @SpireInsertPatch(rloc = 20, localvars = {"card"})
        public static void Insert(AbstractCard __inst, AbstractCard card) {
            card.selfRetain = __inst.selfRetain;
            if(card.selfRetain){
                boolean hasRetainAlready=false;
                //logger.info("卡牌生描述："+card.rawDescription.toLowerCase());
                for(String s: GameDictionary.RETAIN.NAMES){
                    //logger.info("保留关键词："+s);
                    if(card.rawDescription.toLowerCase().startsWith(s)||card.rawDescription.toLowerCase().startsWith(" "+s)){
                        hasRetainAlready=true;
                        break;
                    }
                }
                if(!hasRetainAlready){
                    card.rawDescription=(new SeaOfMoon()).DESCRIPTIONS[5]+card.rawDescription;
                    card.initializeDescription();
                }
            }
        }
    }

    @SpirePatch(
            clz= RestoreRetainedCardsAction.class,
            method="update"
    )
    public static class PatchTool2 {
        @SpireInsertPatch(rloc = 5)
        public static void Insert(RestoreRetainedCardsAction __inst) {
            for(AbstractRelic r: AbstractDungeon.player.relics){
                if(r.relicId.equals(SeaOfMoon.ID)){
                    r.onTrigger();
                }
            }
        }
    }

    @SpirePatch(
            clz= AbstractDungeon.class,
            method="nextRoomTransition",
            paramtypez = {SaveFile.class}
    )
    public static class PatchTool3 {
        @SpireInsertPatch(rloc = 100)
        public static void Insert(AbstractDungeon __inst, SaveFile saveFile) {
            for(AbstractCard c:AbstractDungeon.player.masterDeck.group){
                if(c.selfRetain){
                    boolean hasRetainAlready=false;
                    //logger.info("卡牌生描述："+card.rawDescription.toLowerCase());
                    for(String s: GameDictionary.RETAIN.NAMES){
                        //logger.info("保留关键词："+s);
                        if(c.rawDescription.toLowerCase().startsWith(s)||c.rawDescription.toLowerCase().startsWith(" "+s)){
                            hasRetainAlready=true;
                            break;
                        }
                    }
                    if(!hasRetainAlready){
                        c.rawDescription=(new SeaOfMoon()).DESCRIPTIONS[5]+c.rawDescription;
                        c.initializeDescription();
                    }
                }
            }
        }
    }
}
