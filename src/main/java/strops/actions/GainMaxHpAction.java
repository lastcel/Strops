package strops.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class GainMaxHpAction extends AbstractGameAction {
    public GainMaxHpAction(int amount) {
        this.amount = amount;
    }

    public void update() {
        AbstractDungeon.player.increaseMaxHp(this.amount,true);
        this.isDone = true;
    }
}
