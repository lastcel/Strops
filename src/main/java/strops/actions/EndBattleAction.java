package strops.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class EndBattleAction extends AbstractGameAction {
    @Override
    public void update(){
        AbstractDungeon.getCurrRoom().endBattle();
        this.isDone = true;
    }
}
