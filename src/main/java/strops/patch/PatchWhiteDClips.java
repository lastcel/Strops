package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import strops.helpers.ModHelper;
import strops.relics.WhiteDClips;

import java.util.ArrayList;

import static strops.relics.WhiteDClips.BONUS;

public class PatchWhiteDClips {

    @SpirePatch(
            clz= AbstractCard.class,
            method="calculateCardDamage"
    )
    public static class PatchTool1 {
        @SpireInsertPatch(rloc = 29,localvars = {"tmp"})
        public static void Insert(AbstractCard __inst, AbstractMonster mo, @ByRef float[] tmp) {
            if(mo.currentBlock>0){
                float multiplier=(float) BONUS.value/10;
                for(AbstractRelic r: AbstractDungeon.player.relics){
                    if(r.relicId.equals(ModHelper.makePath(WhiteDClips.class.getSimpleName()))){
                        tmp[0]*=multiplier;
                    }
                }
                if (__inst.baseDamage != (int)tmp[0]){
                    __inst.isDamageModified = true;
                }
            }
        }
    }

    @SpirePatch(
            clz= AbstractCard.class,
            method="calculateCardDamage"
    )
    public static class PatchTool2 {
        @SpireInsertPatch(rloc = 88,localvars = {"tmp","m"})
        public static void Insert(AbstractCard __inst, AbstractMonster mo,
                                  @ByRef float[][] tmp, ArrayList<AbstractMonster> m) {
            float multiplier=(float) BONUS.value/10;
            for(int i=0;i<tmp[0].length;i++){
                if(m.get(i).currentBlock>0){
                    for(AbstractRelic r: AbstractDungeon.player.relics) {
                        if (r.relicId.equals(ModHelper.makePath(WhiteDClips.class.getSimpleName()))) {
                            tmp[0][i] *= multiplier;
                        }
                    }
                    if (__inst.baseDamage != (int)tmp[0][i]){
                        __inst.isDamageModified = true;
                    }
                }
            }
        }
    }
}
