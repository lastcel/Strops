package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import strops.modcore.Strops;
import strops.relics.Turbolens;

import java.lang.reflect.Field;

public class PatchTurbolens {

    /*
    @SpirePatch(
            clz=ExhaustCardEffect.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool1{
        public static SpireField<Boolean> isQuickCall=new SpireField<>(() -> false);
    }

     */

    @SpirePatch(
            clz= AbstractCard.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool1{
        public static SpireField<Boolean> isQuickCall=new SpireField<>(() -> false);
    }

    @SpirePatch(
            clz= ExhaustCardEffect.class,
            method="update"
    )
    public static class PatchTool2 {
        @SpireInsertPatch(rloc = 16)
        public static void Insert(ExhaustCardEffect __inst) {
            try {
                Field f= ExhaustCardEffect.class.getDeclaredField("c");
                f.setAccessible(true);
                AbstractCard c = (AbstractCard)f.get(__inst);
                if(PatchTool1.isQuickCall.get(c)&&__inst.duration<0.7f){
                    //Strops.logger.info("ECE响应快速调用，卡牌="+c.name);
                    __inst.duration=-0.1f;
                    PatchTool1.isQuickCall.set(c,false);
                }
            } catch (IllegalAccessException|NoSuchFieldException e) {
                Strops.logger.info("PatchTurbolens Reporting, failed to read ExhaustCardEffect#.c!");
            }
        }
    }

    @SpirePatch(
            clz= ExhaustSpecificCardAction.class,
            method=SpirePatch.CONSTRUCTOR,
            paramtypez = {AbstractCard.class, CardGroup.class, boolean.class}
    )
    public static class PatchTool3 {
        @SpireInsertPatch(rloc=7)
        public static void Insert(ExhaustSpecificCardAction __inst, AbstractCard targetCard, CardGroup group, boolean isFast, @ByRef float[] ___startingDuration, @ByRef float[] ___duration) {
            if(Turbolens.isUsingXXFAST){
                //Strops.logger.info("ESCA正在使用超快动作时长，目标卡牌="+targetCard.name+"，组别="+group.type);
                ___startingDuration[0]=Settings.ACTION_DUR_XFAST*0.5f;
                ___duration[0]=Settings.ACTION_DUR_XFAST*0.5f;
            }
        }
    }

    /*
    @SpirePatch(
            clz= ExhaustSpecificCardAction.class,
            method="update"
    )
    public static class PatchTool4 {
        @SpirePrefixPatch
        public static void Prefix(ExhaustSpecificCardAction __inst, AbstractCard ___targetCard, float ___startingDuration, float ___duration) {
            Strops.logger.info("ESCA更新中，目标卡牌="+___targetCard+"，起始时长="+___startingDuration+"，经过时长="+___duration);
        }
    }

     */
}
