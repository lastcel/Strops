package strops.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class LoseBlockAction extends AbstractGameAction {
    public LoseBlockAction(int amount) {
        this.amount = amount;
    }

    public void update() {
        AbstractDungeon.player.loseBlock(this.amount);
        this.isDone = true;
    }
}
