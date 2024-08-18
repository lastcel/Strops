package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;

import java.util.Iterator;

public class PatchBlackRabbit {

    @SpirePatch(
            clz= CardGroup.class,
            method="removeCard",
            paramtypez = {AbstractCard.class}
    )
    public static class PatchTool1 {
        @SpirePrefixPatch
        public static void Prefix(CardGroup __inst, AbstractCard c) {
            if(__inst.type == CardGroup.CardGroupType.MASTER_DECK){
                if(!__inst.group.remove(c)){
                    Iterator<AbstractCard> iterator = __inst.group.iterator();
                    while (iterator.hasNext()){
                        AbstractCard c2=iterator.next();
                        if(c2.uuid==c.uuid){
                            iterator.remove();
                            break;
                        }
                    }
                }
            }
        }
    }
}
