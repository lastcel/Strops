package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.cards.ArcaneTalents;
import strops.cards.FireTalents;
import strops.cards.FrostTalents;
import strops.helpers.ModHelper;
import strops.relics.BambooDragonflyOfHanyuHorner;

public class PatchGrassNowAndFlowersThen {

    @SpirePatch(
            clz= AbstractPlayer.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool1{
        public static SpireField<Integer> earliestTurnCount= new SpireField<>(() -> 1);
    }

    @SpirePatch(
            clz= AbstractRoom.class,
            method="update"
    )
    public static class PatchTool2 {
        @SpireInsertPatch(rloc = 41)
        public static void Insert(AbstractRoom __instance){
            for(AbstractRelic r:AbstractDungeon.player.relics){
                if(r.relicId.equals(ModHelper.makePath(BambooDragonflyOfHanyuHorner.class.getSimpleName()))){
                    r.counter=0;
                    break;
                }
            }

            for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
                if ((c.cardID.equals(ModHelper.makePath(ArcaneTalents.class.getSimpleName())))
                        &&(PatchTool1.earliestTurnCount.get(AbstractDungeon.player)>=c.magicNumber)) {
                    c.baseDamage+=((ArcaneTalents)c).keyNumber1;
                }
                 else if ((c.cardID.equals(ModHelper.makePath(FrostTalents.class.getSimpleName())))
                        &&(PatchTool1.earliestTurnCount.get(AbstractDungeon.player)>=c.magicNumber)) {
                    c.baseBlock+=((FrostTalents)c).keyNumber1;
                }
                else if ((c.cardID.equals(ModHelper.makePath(FireTalents.class.getSimpleName())))
                        &&(PatchTool1.earliestTurnCount.get(AbstractDungeon.player)>=
                        ((FireTalents)c).keyNumber1)) {
                    c.baseMagicNumber+=((FireTalents)c).keyNumber2;
                }
            }

            /*
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if ((c.cardID.equals(ModHelper.makePath(ArcaneTalents.class.getSimpleName())))
                        &&(PatchTool1.earliestTurnCount.get(AbstractDungeon.player)>=c.magicNumber)) {
                    c.baseDamage+=((ArcaneTalents)c).keyNumber1;
                }
                if ((c.cardID.equals(ModHelper.makePath(FrostTalents.class.getSimpleName())))
                        &&(PatchTool1.earliestTurnCount.get(AbstractDungeon.player)>=c.magicNumber)) {
                    c.baseBlock+=((FrostTalents)c).keyNumber1;
                }
            }
            for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
                if ((c.cardID.equals(ModHelper.makePath(ArcaneTalents.class.getSimpleName())))
                        &&(PatchTool1.earliestTurnCount.get(AbstractDungeon.player)>=c.magicNumber)) {
                    c.baseDamage+=((ArcaneTalents)c).keyNumber1;
                }
                if ((c.cardID.equals(ModHelper.makePath(FrostTalents.class.getSimpleName())))
                        &&(PatchTool1.earliestTurnCount.get(AbstractDungeon.player)>=c.magicNumber)) {
                    c.baseBlock+=((FrostTalents)c).keyNumber1;
                }
            }

             */
        }
    }

    @SpirePatch(
            clz= GameActionManager.class,
            method="getNextAction"
    )
    public static class PatchTool3 {
        @SpireInsertPatch(rloc = 227)
        public static void Insert(GameActionManager __instance){
            for(AbstractRelic r:AbstractDungeon.player.relics){
                if(r.relicId.equals(ModHelper.makePath(BambooDragonflyOfHanyuHorner.class.getSimpleName()))){
                    r.counter=0;
                    break;
                }
            }

            PatchTool1.earliestTurnCount.set(AbstractDungeon.player,
                    PatchTool1.earliestTurnCount.get(AbstractDungeon.player)+1);

            for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
                if ((c.cardID.equals(ModHelper.makePath(ArcaneTalents.class.getSimpleName())))
                        &&(PatchTool1.earliestTurnCount.get(AbstractDungeon.player)==c.magicNumber)) {
                    c.baseDamage+=((ArcaneTalents)c).keyNumber1;
                }
                else if ((c.cardID.equals(ModHelper.makePath(FrostTalents.class.getSimpleName())))
                        &&(PatchTool1.earliestTurnCount.get(AbstractDungeon.player)==c.magicNumber)) {
                    c.baseBlock+=((FrostTalents)c).keyNumber1;
                }
                else if ((c.cardID.equals(ModHelper.makePath(FireTalents.class.getSimpleName())))
                        &&(PatchTool1.earliestTurnCount.get(AbstractDungeon.player)==
                        ((FireTalents)c).keyNumber1)) {
                    c.baseMagicNumber+=((FireTalents)c).keyNumber2;
                }
            }
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if ((c.cardID.equals(ModHelper.makePath(ArcaneTalents.class.getSimpleName())))
                        &&(PatchTool1.earliestTurnCount.get(AbstractDungeon.player)==c.magicNumber)) {
                    c.baseDamage+=((ArcaneTalents)c).keyNumber1;
                }
                else if ((c.cardID.equals(ModHelper.makePath(FrostTalents.class.getSimpleName())))
                        &&(PatchTool1.earliestTurnCount.get(AbstractDungeon.player)==c.magicNumber)) {
                    c.baseBlock+=((FrostTalents)c).keyNumber1;
                }
                else if ((c.cardID.equals(ModHelper.makePath(FireTalents.class.getSimpleName())))
                        &&(PatchTool1.earliestTurnCount.get(AbstractDungeon.player)==
                        ((FireTalents)c).keyNumber1)) {
                    c.baseMagicNumber+=((FireTalents)c).keyNumber2;
                }
            }
            for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
                if ((c.cardID.equals(ModHelper.makePath(ArcaneTalents.class.getSimpleName())))
                        &&(PatchTool1.earliestTurnCount.get(AbstractDungeon.player)==c.magicNumber)) {
                    c.baseDamage+=((ArcaneTalents)c).keyNumber1;
                }
                else if ((c.cardID.equals(ModHelper.makePath(FrostTalents.class.getSimpleName())))
                        &&(PatchTool1.earliestTurnCount.get(AbstractDungeon.player)==c.magicNumber)) {
                    c.baseBlock+=((FrostTalents)c).keyNumber1;
                }
                else if ((c.cardID.equals(ModHelper.makePath(FireTalents.class.getSimpleName())))
                        &&(PatchTool1.earliestTurnCount.get(AbstractDungeon.player)==
                        ((FireTalents)c).keyNumber1)) {
                    c.baseMagicNumber+=((FireTalents)c).keyNumber2;
                }
            }
            for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
                if ((c.cardID.equals(ModHelper.makePath(ArcaneTalents.class.getSimpleName())))
                        &&(PatchTool1.earliestTurnCount.get(AbstractDungeon.player)==c.magicNumber)) {
                    c.baseDamage+=((ArcaneTalents)c).keyNumber1;
                }
                else if ((c.cardID.equals(ModHelper.makePath(FrostTalents.class.getSimpleName())))
                        &&(PatchTool1.earliestTurnCount.get(AbstractDungeon.player)==c.magicNumber)) {
                    c.baseBlock+=((FrostTalents)c).keyNumber1;
                }
                else if ((c.cardID.equals(ModHelper.makePath(FireTalents.class.getSimpleName())))
                        &&(PatchTool1.earliestTurnCount.get(AbstractDungeon.player)==
                        ((FireTalents)c).keyNumber1)) {
                    c.baseMagicNumber+=((FireTalents)c).keyNumber2;
                }
            }
        }
    }

    @SpirePatch(
            clz= AbstractDungeon.class,
            method="resetPlayer"
    )
    public static class PatchTool4 {
        @SpirePostfixPatch
        public static void Postfix(){
            PatchTool1.earliestTurnCount.set(AbstractDungeon.player, 1);
        }
    }
}