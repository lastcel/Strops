package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;

import java.util.HashMap;

public class PatchVanillaBottles {

    @SpirePatch(
            clz= SaveAndContinue.class,
            method="saveBottle"
    )
    public static class PatchTool1 {
        @SpireInsertPatch(rloc = 3)
        public static void Insert(HashMap<Object, Object> params, String bottleId, String save_name, AbstractCard card) {
            params.replace(save_name, Integer.toString(AbstractDungeon.player.masterDeck.group.indexOf(card)));
        }
    }

    @SpirePatch(
            clz= CardCrawlGame.class,
            method="loadPlayerSave"
    )
    public static class PatchTool2 {
        @SpireInsertPatch(rloc = 137,localvars = {"tmpCard"})
        public static void Insert(CardCrawlGame __inst, AbstractPlayer p, @ByRef AbstractCard[] tmpCard) {
            for (AbstractCard abstractCard : AbstractDungeon.player.masterDeck.group) {
                if (Integer.toString(AbstractDungeon.player.masterDeck.group.indexOf(abstractCard)).equals(CardCrawlGame.saveFile.bottled_flame)) {
                    tmpCard[0] = abstractCard;
                    break;
                }
            }
        }
    }

    @SpirePatch(
            clz= CardCrawlGame.class,
            method="loadPlayerSave"
    )
    public static class PatchTool3 {
        @SpireInsertPatch(rloc = 157,localvars = {"tmpCard"})
        public static void Insert(CardCrawlGame __inst, AbstractPlayer p, @ByRef AbstractCard[] tmpCard) {
            for (AbstractCard abstractCard : AbstractDungeon.player.masterDeck.group) {
                if (Integer.toString(AbstractDungeon.player.masterDeck.group.indexOf(abstractCard)).equals(CardCrawlGame.saveFile.bottled_lightning)) {
                    tmpCard[0] = abstractCard;
                    break;
                }
            }
        }
    }

    @SpirePatch(
            clz= CardCrawlGame.class,
            method="loadPlayerSave"
    )
    public static class PatchTool4 {
        @SpireInsertPatch(rloc = 177,localvars = {"tmpCard"})
        public static void Insert(CardCrawlGame __inst, AbstractPlayer p, @ByRef AbstractCard[] tmpCard) {
            for (AbstractCard abstractCard : AbstractDungeon.player.masterDeck.group) {
                if (Integer.toString(AbstractDungeon.player.masterDeck.group.indexOf(abstractCard)).equals(CardCrawlGame.saveFile.bottled_tornado)) {
                    tmpCard[0] = abstractCard;
                    break;
                }
            }
        }
    }
}
