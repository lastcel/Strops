package strops.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import strops.relics.SoulCannonFour;

public class ReduceCardRewardSizeAction extends AbstractGameAction {
    SoulCannonFour soulCannonFour;

    public ReduceCardRewardSizeAction(SoulCannonFour soulCannonFour) {
        this.soulCannonFour = soulCannonFour;
    }

    public void update() {
        soulCannonFour.numReduced++;
        this.isDone = true;
    }
}
