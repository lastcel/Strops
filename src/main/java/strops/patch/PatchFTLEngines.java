package strops.patch;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.actions.EscapeAction;
import strops.actions.LoseBlockAction;
import strops.helpers.ModHelper;
import strops.relics.Decanter;
import strops.relics.FTLEngines;
import strops.relics.Maniacal;

public class PatchFTLEngines {

    @SpirePatch(
            clz= AbstractRoom.class,
            method="endTurn"
    )
    public static class PatchTool1 {
        @SpireInsertPatch(rloc=6)
        public static void Insert(AbstractRoom __inst) {

            for(AbstractRelic r:AbstractDungeon.player.relics){
                if(r.relicId.equals(Maniacal.ID)){
                    if(!r.grayscale){
                        int blockLost=MathUtils.floor(AbstractDungeon.player.currentBlock*Maniacal.COST.value/10.0f);
                        int damage=MathUtils.floor(blockLost*Maniacal.DAMAGE_CO.value/10.0f);
                        if(blockLost>0){
                            AbstractDungeon.actionManager.addToBottom(new LoseBlockAction(blockLost));
                        }
                        if(damage>0){
                            AbstractDungeon.actionManager.addToBottom(
                                    new DamageAllEnemiesAction(
                                            AbstractDungeon.player, DamageInfo.createDamageMatrix(damage, true),
                                            DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE));
                        }
                    }
                    break;
                }
            }



            if (AbstractDungeon.player.hasRelic(ModHelper.makePath(FTLEngines.class.getSimpleName()))) {
                if(AbstractDungeon.player.getRelic(ModHelper.makePath(FTLEngines.class.getSimpleName()))
                        .counter==FTLEngines.THRESHOLD.value){
                    AbstractRelic r2;
                    if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                            ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                                    .relicToDisenchant.equals(FTLEngines.ID)){
                        r2.flash();
                    } else {
                        AbstractDungeon.actionManager.addToBottom(new EscapeAction());
                        for(int i=0;i<20;i++){
                            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1F));
                        }
                    }
                }
            }
        }
    }
}
