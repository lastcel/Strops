package strops.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import strops.relics.MessyPuppy;

public class SpawnMonsterAction extends AbstractGameAction {
    Class<? extends AbstractMonster> monster;
    int counter;

    public SpawnMonsterAction(Class<? extends AbstractMonster> monster, int counter) {
        this.monster=monster;
        this.counter=counter;
    }

    @Override
    public void update(){
        MessyPuppy.spawnMonster(monster,counter);
        this.isDone = true;
    }
}
