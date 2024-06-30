package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PatchMultiDamagePreview {

    @SpirePatch(
            clz= AbstractCard.class,
            method="calculateCardDamage"
    )
    public static class PatchTool1 {
        @SpirePostfixPatch
        public static void Postfix(AbstractCard __inst, AbstractMonster mo,
                                   boolean ___isMultiDamage) {
            if(___isMultiDamage){
                int aliveMonsters=0,index=0;
                for (int i=0;i<AbstractDungeon.getCurrRoom().monsters.monsters.size();i++) {
                    AbstractMonster mon=AbstractDungeon.getCurrRoom().monsters.monsters.get(i);
                    if (!mon.isDying && mon.currentHealth > 0) {
                        aliveMonsters++;
                        index=i;
                    }
                }
                if(aliveMonsters==1){
                    __inst.damage=__inst.multiDamage[index];
                }
            }
        }
    }
}
