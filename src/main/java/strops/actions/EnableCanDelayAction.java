package strops.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

import static strops.relics.DelayedGratification.canBeDelayed;

public class EnableCanDelayAction extends AbstractGameAction {
    @Override
    public void update(){
        canBeDelayed = true;
        this.isDone = true;
    }
}
