package strops.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import strops.relics.MessyPuppy;

public class MessyPuppyAction extends AbstractGameAction {
    MessyPuppy messyPuppy;

    public MessyPuppyAction(MessyPuppy messyPuppy) {
        this.messyPuppy=messyPuppy;
    }

    @Override
    public void update(){
        for(AbstractMonster m: AbstractDungeon.getCurrRoom().monsters.monsters){
            if(messyPuppy.counter>=MessyPuppy.CAPACITY.value){
                break;
            }

            if(!m.isDying&&!m.halfDead){
                continue;
            }

            boolean hasRevived=false;
            for(AbstractMonster i:messyPuppy.records){
                if(i==m){
                    hasRevived=true;
                    break;
                }
            }

            if(!hasRevived){
                messyPuppy.flash();
                messyPuppy.counter++;
                if(messyPuppy.counter==MessyPuppy.CAPACITY.value){
                    messyPuppy.grayscale=true;
                }

                addToBot(new SpawnMonsterAction(m.getClass(),messyPuppy.counter));
                messyPuppy.records.add(m);
            }
        }
        this.isDone = true;
    }
}
