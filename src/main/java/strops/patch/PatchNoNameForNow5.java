package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import strops.actions.MessyPuppyAction;
import strops.relics.NoNameForNow5;

public class PatchNoNameForNow5 {

    /*
    @SpirePatch(
            clz= AbstractMonster.class,
            method="updateDeathAnimation"
    )
    public static class PatchTool1 {
        @SpireInsertPatch(rloc=12)
        public static SpireReturn<Void> Insert(AbstractMonster __inst) {
            NoNameForNow5 r5=(NoNameForNow5)AbstractDungeon.player.getRelic(NoNameForNow5.ID);

            if(r5!=null&&(PatchGrassNowAndFlowersThen.PatchTool1.earliestTurnCount.get(AbstractDungeon.player)==
                    NoNameForNow5.LOWER.value&&NoNameForNow5.UPPER.value>NoNameForNow5.LOWER.value||r5.activated)){
                r5.activated=true;
                //r5.everActivated=true;
                r5.flash();
                AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player,r5));
                r5.stopPulse();
                //AbstractDungeon.getCurrRoom().cannotLose=true;
                __inst.dispose();
                __inst.powers.clear();
                return SpireReturn.Return();
            }

            return SpireReturn.Continue();
        }
    }

     */

    @SpirePatch(
            clz= AbstractMonster.class,
            method="updateDeathAnimation"
    )
    public static class PatchTool1 {
        @SpirePrefixPatch
        public static void Prefix(AbstractMonster __inst) {
            NoNameForNow5 r5=(NoNameForNow5)AbstractDungeon.player.getRelic(NoNameForNow5.ID);

            if(r5!=null&&PatchGrassNowAndFlowersThen.PatchTool1.earliestTurnCount.get(AbstractDungeon.player)==
                    NoNameForNow5.LOWER.value&&NoNameForNow5.UPPER.value>NoNameForNow5.LOWER.value&&
                    MessyPuppyAction.myAreMonstersBasicallyDead(AbstractDungeon.getMonsters())&&
                    !(AbstractDungeon.getCurrRoom()).isBattleOver&&
                    !(AbstractDungeon.getCurrRoom()).cannotLose&&
                    !r5.activated){
                r5.activated=true;
                r5.flash();
                AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player,r5));
                r5.stopPulse();
            }
        }
    }

    /*
    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class PatchTool2 {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("areMonstersBasicallyDead"))
                        m.replace("if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(strops.relics.NoNameForNow5.ID)&&((strops.relics.NoNameForNow5)com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.getRelic(strops.relics.NoNameForNow5.ID)).activated) " +
                                "{$_ = false;} " +
                                "else " +
                                "{$_ = $proceed($$);}");
                }
            };
        }
    }

     */

    @SpirePatch(
            clz= MonsterGroup.class,
            method="areMonstersBasicallyDead"
    )
    public static class PatchTool3 {
        @SpirePrefixPatch
        public static SpireReturn<Boolean> Prefix(MonsterGroup __inst) {
            NoNameForNow5 r5=(NoNameForNow5)AbstractDungeon.player.getRelic(NoNameForNow5.ID);

            if(r5!=null&&(PatchGrassNowAndFlowersThen.PatchTool1.earliestTurnCount.get(AbstractDungeon.player)==
                    NoNameForNow5.LOWER.value&&NoNameForNow5.UPPER.value>NoNameForNow5.LOWER.value||r5.activated)){
                return SpireReturn.Return(false);
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz= MonsterGroup.class,
            method="areMonstersDead"
    )
    public static class PatchTool4 {
        @SpirePrefixPatch
        public static SpireReturn<Boolean> Prefix(MonsterGroup __inst) {
            NoNameForNow5 r5=(NoNameForNow5)AbstractDungeon.player.getRelic(NoNameForNow5.ID);

            if(r5!=null&&r5.activated){
                return SpireReturn.Return(false);
            }

            return SpireReturn.Continue();
        }
    }

    /*
    @SpirePatch(
            clz= GameActionManager.class,
            method="getNextAction"
    )
    public static class PatchTool4 {
        @SpireInsertPatch(rloc=47)
        public static SpireReturn<Void> Insert(GameActionManager __inst) {
            Strops.logger.info("卡牌队列大小="+__inst.cardQueue.size());
            if(!__inst.cardQueue.isEmpty()&&__inst.cardQueue.get(0).card!=null){
                Strops.logger.info("队首卡牌="+__inst.cardQueue.get(0).card.cardID);
            }

            if(__inst.cardQueue.isEmpty()){
                return SpireReturn.Return();
            }

            return SpireReturn.Continue();
        }
    }

     */

    @SpirePatch(
            clz= AbstractPlayer.class,
            method="updateEscapeAnimation"
    )
    public static class PatchTool5 {
        @SpirePrefixPatch
        public static void Insert(AbstractPlayer __inst) {
            NoNameForNow5 r5=(NoNameForNow5)__inst.getRelic(NoNameForNow5.ID);

            if(r5!=null&&__inst.escapeTimer!=0.0F&&PatchGrassNowAndFlowersThen.PatchTool1.earliestTurnCount.get(AbstractDungeon.player)==
                    NoNameForNow5.LOWER.value&&NoNameForNow5.UPPER.value>NoNameForNow5.LOWER.value){
                r5.activated=true;
                r5.flash();
                AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player,r5));
                r5.stopPulse();
                (AbstractDungeon.getCurrRoom()).smoked = false;
                __inst.showHealthBar();
                __inst.isEscaping = false;
                __inst.flipHorizontal = !AbstractDungeon.player.flipHorizontal;
                AbstractDungeon.overlayMenu.endTurnButton.enable();
                __inst.escapeTimer = 0.0F;
            }
        }
    }
}
