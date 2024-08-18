package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.BattleStartEffect;
import strops.relics.BigBangBell;
import strops.relics.Register;

public class PatchBigBangBell {

    private static boolean heldIsHoveringDropZone=false;
    private static boolean hasChangedIHDZ=false;

    @SpirePatch(
            clz= AbstractCard.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool1{
        public static SpireField<Boolean> inBigBangBell=new SpireField<>(() -> false);
    }

    @SpirePatch(
            clz= AbstractCard.class,
            method="makeStatEquivalentCopy"
    )
    public static class PatchTool2 {
        @SpireInsertPatch(rloc=20,localvars = {"card"})
        public static void Insert(AbstractCard __inst, AbstractCard card) {
            PatchTool1.inBigBangBell.set(card, PatchTool1.inBigBangBell.get(__inst));
        }
    }

    @SpirePatch(
            clz= AbstractPlayer.class,
            method="bottledCardUpgradeCheck"
    )
    public static class PatchTool3 {
        @SpirePostfixPatch
        public static void Postfix(AbstractPlayer __instance, AbstractCard c) {
            if (PatchTool1.inBigBangBell.get(c) && __instance.hasRelic(BigBangBell.ID))
                ((BigBangBell) __instance.getRelic(BigBangBell.ID)).setDescriptionAfterLoading();
        }
    }

    @SpirePatch(
            clz= BattleStartEffect.class,
            method="update"
    )
    public static class PatchTool4 {
        @SpireInsertPatch(rloc=29)
        public static void Insert(BattleStartEffect __instance) {
            if(AbstractDungeon.player.hasRelic(BigBangBell.ID)&&BigBangBell.hasDrawnBellFirstTurn){
                for(AbstractMonster m:AbstractDungeon.getCurrRoom().monsters.monsters){
                    if(!m.isDeadOrEscaped()&&m.getIntentBaseDmg()>=0&&BigBangBell.PASSIVE.value>0){
                        AbstractDungeon.actionManager.addToBottom(new DamageAction(m,new DamageInfo(AbstractDungeon.player,BigBangBell.PASSIVE.value, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
                    }
                }
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "clickAndDragCards")
    public static class PatchTool5 {
        @SpireInsertPatch(rloc = 114)
        public static void Insert(AbstractPlayer __inst) {
            AbstractRelic r=AbstractDungeon.player.getRelic(Register.ID);

            if(PatchTool1.inBigBangBell.get(__inst.hoveredCard)||(r!=null&&!r.grayscale)){
                heldIsHoveringDropZone=__inst.isHoveringDropZone;
                __inst.isHoveringDropZone=false;
                hasChangedIHDZ=true;
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "clickAndDragCards")
    public static class PatchTool6 {
        @SpireInsertPatch(rloc = 121)
        public static void Insert(AbstractPlayer __inst) {
            if(hasChangedIHDZ){
                __inst.isHoveringDropZone=heldIsHoveringDropZone;
                hasChangedIHDZ=false;
            }
        }
    }

    /*
    @SpirePatch(
            clz= AbstractPlayer.class,
            method="clickAndDragCards"
    )
    public static class PatchTool4 {
        @SpirePrefixPatch
        public static void Prefix(AbstractPlayer __inst) {
            if (PatchTool1.inBigBangBell.get(__inst.hoveredCard)) {
                BigBangBell.canShowHitArea=true;
            }
        }
    }

     */
}
