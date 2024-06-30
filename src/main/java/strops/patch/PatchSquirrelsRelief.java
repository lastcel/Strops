package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import strops.relics.SquirrelsRelief;

public class PatchSquirrelsRelief {

    @SpirePatch(
            clz= AbstractCreature.class,
            method="hasPower"
    )
    public static class PatchTool1 {
        @SpirePrefixPatch
        public static void Prefix(AbstractCreature __inst, @ByRef String[] targetID) {
            if(__inst instanceof AbstractPlayer&&((AbstractPlayer)__inst).hasRelic(SquirrelsRelief.ID)&&targetID[0].equals(StrengthPower.POWER_ID)){
                ((AbstractPlayer)__inst).getRelic(SquirrelsRelief.ID).flash();
                targetID[0]=DexterityPower.POWER_ID;
            }
        }
    }

    @SpirePatch(
            clz= AbstractCreature.class,
            method="getPower"
    )
    public static class PatchTool2 {
        @SpirePrefixPatch
        public static void Prefix(AbstractCreature __inst, @ByRef String[] targetID) {
            if(__inst instanceof AbstractPlayer&&((AbstractPlayer)__inst).hasRelic(SquirrelsRelief.ID)&&targetID[0].equals(StrengthPower.POWER_ID)){
                ((AbstractPlayer)__inst).getRelic(SquirrelsRelief.ID).flash();
                targetID[0]=DexterityPower.POWER_ID;
            }
        }
    }

    @SpirePatch(
            clz= ApplyPowerAction.class,
            method=SpirePatch.CONSTRUCTOR,
            paramtypez = {AbstractCreature.class, AbstractCreature.class, AbstractPower.class, int.class, boolean.class, AbstractGameAction.AttackEffect.class}
    )
    public static class PatchTool3 {
        @SpirePrefixPatch
        public static void Prefix(ApplyPowerAction __inst, AbstractCreature target, AbstractCreature source, @ByRef AbstractPower[] powerToApply, int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect) {
            if(target instanceof AbstractPlayer&&((AbstractPlayer)target).hasRelic(SquirrelsRelief.ID)&&powerToApply[0].ID.equals(StrengthPower.POWER_ID)){
                ((AbstractPlayer)target).getRelic(SquirrelsRelief.ID).flash();
                powerToApply[0]=new DexterityPower(powerToApply[0].owner,powerToApply[0].amount);
            }
        }
    }
}
