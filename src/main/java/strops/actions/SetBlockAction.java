package strops.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class SetBlockAction extends AbstractGameAction {
    public int toBlock;
    public SetBlockAction(AbstractCreature target, int blockAmount) {
        this.target=target;
        toBlock=blockAmount;
        this.actionType = AbstractGameAction.ActionType.BLOCK;
        this.duration = 0.25F;
    }

    public void update() {
        if (!target.isDying && !target.isDead && !target.halfDead &&
                this.duration == 0.25F){
            int delta=toBlock-target.currentBlock;
            if(delta>0)
            {
                this.target.addBlock(delta);
            } else if(delta<0){
                this.target.loseBlock(-delta);
            }
        }
        tickDuration();
    }
}
