package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import strops.relics.MinoshiroModoki;

public class PatchMinoshiroModoki {

    @SpirePatch(
            clz = CardGroup.class,
            method = "initializeDeck"
    )
    public static class PatchTool1 {
        @SpireInsertPatch(rloc = 2,localvars = {"copy"})
        public static void Insert(CardGroup __inst, CardGroup copy) {
            CardGroup savedCopy=new CardGroup(copy, CardGroup.CardGroupType.UNSPECIFIED);
            savedCopy.group.removeIf(c -> c.rarity == AbstractCard.CardRarity.BASIC);

            for(AbstractRelic r:AbstractDungeon.player.relics){
                if(r.relicId.equals(MinoshiroModoki.ID)&&!r.grayscale){
                    ((MinoshiroModoki)r).onThisTriggered(copy,savedCopy);
                }
            }
        }
    }
}
