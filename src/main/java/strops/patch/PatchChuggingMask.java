/*
package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import strops.relics.ChuggingMask;

public class PatchChuggingMask {

    public static boolean isInIncreaseMaxHp=false;
    public static boolean isFullHpHeal=false;

    @SpirePatch(
            clz= AbstractCreature.class,
            method="increaseMaxHp"
    )
    public static class PatchTool1 {
        @SpirePrefixPatch
        public static void Prefix(AbstractCreature __inst, int amount, boolean showEffect) {
            isInIncreaseMaxHp=true;
        }
    }

    @SpirePatch(
            clz= AbstractCreature.class,
            method="increaseMaxHp"
    )
    public static class PatchTool2 {
        @SpirePostfixPatch
        public static void Postfix(AbstractCreature __inst, int amount, boolean showEffect) {
            isInIncreaseMaxHp=false;
        }
    }

    @SpirePatch(
            clz= AbstractCreature.class,
            method="heal",
            paramtypez = {int.class,boolean.class}
    )
    public static class PatchTool3 {
        @SpireInsertPatch(rloc = 22)
        public static void Insert(AbstractCreature __inst, int healAmount, boolean showEffect) {
            if(__inst.currentHealth==__inst.maxHealth&&healAmount>0){
                isFullHpHeal = true;
            }
        }
    }

    @SpirePatch(
            clz= AbstractCreature.class,
            method="heal",
            paramtypez = {int.class,boolean.class}
    )
    public static class PatchTool4 {
        @SpireInsertPatch(rloc = 27)
        public static void Insert(AbstractCreature __inst, int healAmount, boolean showEffect) {
            if(isFullHpHeal){
                if(__inst.isPlayer&&!isInIncreaseMaxHp){
                    for(AbstractRelic r: AbstractDungeon.player.relics){
                        if(r.relicId.equals(ChuggingMask.ID)&&r.counter<ChuggingMask.TIMES_LIMIT_PER_FLOOR){
                            r.flash();
                            r.counter++;
                            __inst.increaseMaxHp(ChuggingMask.BONUS.value,true);
                        }
                    }
                }
                isFullHpHeal=false;
            }
        }
    }
}

 */
