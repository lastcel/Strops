package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import strops.helpers.ModHelper;
import strops.relics.Bindings;

public class PatchBindings {

    @SpirePatch(
            clz= AbstractCard.class,
            method="calculateCardDamage"
    )
    public static class PatchTool1 {
        @SpireInsertPatch(rloc = 18,localvars = {"tmp"})
        public static void Insert(AbstractCard __inst, AbstractMonster mo, @ByRef float[] tmp) {
            if(__inst.target==AbstractCard.CardTarget.ENEMY||__inst.target==AbstractCard.CardTarget.SELF_AND_ENEMY){
                if(AbstractDungeon.player.hasRelic(ModHelper.makePath(Bindings.class.getSimpleName()))&&
                        AbstractDungeon.player.getRelic(ModHelper.makePath(Bindings.class.getSimpleName()))
                                .counter<Bindings.THRESHOLD.value){
                    tmp[0]+= Bindings.BONUS.value;
                    if (__inst.baseDamage != (int)tmp[0]){
                        __inst.isDamageModified = true;
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz= AbstractCard.class,
            method="calculateCardDamage"
    )
    public static class PatchTool2 {
        @SpireInsertPatch(rloc = 76,localvars = {"tmp"})
        public static void Insert(AbstractCard __inst, AbstractMonster mo,
                                  @ByRef float[][] tmp) {
            if(__inst.target==AbstractCard.CardTarget.ENEMY||__inst.target==AbstractCard.CardTarget.SELF_AND_ENEMY){
                if(AbstractDungeon.player.hasRelic(ModHelper.makePath(Bindings.class.getSimpleName()))&&
                        AbstractDungeon.player.getRelic(ModHelper.makePath(Bindings.class.getSimpleName()))
                                .counter<Bindings.THRESHOLD.value){
                    for(int i=0;i<tmp[0].length;i++){
                        tmp[0][i]+=Bindings.BONUS.value;
                        if (__inst.baseDamage != (int)tmp[0][i]){
                            __inst.isDamageModified = true;
                        }
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz= AbstractCard.class,
            method="applyPowers"
    )
    public static class PatchTool3 {
        @SpireInsertPatch(rloc = 18,localvars = {"tmp"})
        public static void Insert(AbstractCard __inst, @ByRef float[] tmp) {
            if(__inst.target==AbstractCard.CardTarget.ENEMY||__inst.target==AbstractCard.CardTarget.SELF_AND_ENEMY){
                if(AbstractDungeon.player.hasRelic(ModHelper.makePath(Bindings.class.getSimpleName()))&&
                        AbstractDungeon.player.getRelic(ModHelper.makePath(Bindings.class.getSimpleName()))
                                .counter<Bindings.THRESHOLD.value){
                    tmp[0]+= Bindings.BONUS.value;
                    if (__inst.baseDamage != (int)tmp[0]){
                        __inst.isDamageModified = true;
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz= AbstractCard.class,
            method="applyPowers"
    )
    public static class PatchTool4 {
        @SpireInsertPatch(rloc = 64,localvars = {"tmp"})
        public static void Insert(AbstractCard __inst, @ByRef float[][] tmp) {
            if(__inst.target==AbstractCard.CardTarget.ENEMY||__inst.target==AbstractCard.CardTarget.SELF_AND_ENEMY){
                if(AbstractDungeon.player.hasRelic(ModHelper.makePath(Bindings.class.getSimpleName()))&&
                        AbstractDungeon.player.getRelic(ModHelper.makePath(Bindings.class.getSimpleName()))
                                .counter<Bindings.THRESHOLD.value){
                    for(int i=0;i<tmp[0].length;i++){
                        tmp[0][i]+=Bindings.BONUS.value;
                        if (__inst.baseDamage != (int)tmp[0][i]){
                            __inst.isDamageModified = true;
                        }
                    }
                }
            }
        }
    }
}
