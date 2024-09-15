package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import strops.relics.Maniacal;

public class PatchManiacal {

    @SpirePatch(
            clz= AbstractMonster.class,
            method="damage"
    )
    public static class PatchTool1 {
        @SpireInsertPatch(rloc=44,localvars = {"damageAmount"})
        public static void Insert(AbstractMonster __inst, DamageInfo info, int damageAmount) {
            for(AbstractRelic r:AbstractDungeon.player.relics){
                if(r.relicId.equals(Maniacal.ID)){
                    ((Maniacal)r).onThisAttack(info,damageAmount,__inst);
                    break;
                }
            }
        }
    }
}
