package strops.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class LoseMaxHpAction extends AbstractGameAction {
    public LoseMaxHpAction(int amount) {
        this.amount = amount;
    }

    public void update() {
        AbstractDungeon.player.decreaseMaxHealth(this.amount);
        this.isDone = true;
    }
}
