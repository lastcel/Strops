package strops.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import strops.relics.Maniacal;

public class ManiacalAction extends AbstractGameAction {
    @Override
    public void update(){
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
        this.isDone = true;
    }
}
