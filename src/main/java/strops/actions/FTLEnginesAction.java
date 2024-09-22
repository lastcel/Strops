package strops.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class FTLEnginesAction extends AbstractGameAction {
    @Override
    public void update(){
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            public void update() {
                AbstractDungeon.actionManager.monsterAttacksQueued = true;
                this.isDone = true;
            }
        });

        AbstractDungeon.actionManager.addToBottom(new EscapeAction());
        for(int i=0;i<10;i++){
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1F));
        }

        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            public void update() {
                AbstractDungeon.actionManager.monsterAttacksQueued = false;
                this.isDone = true;
            }
        });
        this.isDone = true;
    }
}
