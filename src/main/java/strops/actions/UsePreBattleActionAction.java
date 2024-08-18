package strops.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class UsePreBattleActionAction extends AbstractGameAction {
    public AbstractMonster m;

    public UsePreBattleActionAction(AbstractMonster m){
        this.m=m;
    }

    @Override
    public void update(){
        m.usePreBattleAction();
        this.isDone=true;
    }
}
