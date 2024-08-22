package strops.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import strops.relics.MessyPuppy;

public class MessyPuppyAction extends AbstractGameAction {
    MessyPuppy messyPuppy;

    public MessyPuppyAction(MessyPuppy messyPuppy) {
        this.messyPuppy=messyPuppy;
    }

    @Override
    public void update(){
        if (myAreMonstersBasicallyDead(AbstractDungeon.getMonsters())) {
            this.isDone=true;
            return;
        }

        for(AbstractMonster m: AbstractDungeon.getCurrRoom().monsters.monsters){
            //Strops.logger.info("。。。检测到怪物："+m.name+"，isDying="+m.isDying+"，halfDead="+m.halfDead);
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

                //Strops.logger.info("？？？准备复活：{}", m.name);
                addToBot(new SpawnMonsterAction(m.getClass(),messyPuppy.counter));
                messyPuppy.records.add(m);
            }
        }
        this.isDone = true;
    }

    public static boolean myAreMonstersBasicallyDead(MonsterGroup mg) {
        for (AbstractMonster m : mg.monsters) {
            if (!m.isDying && !m.isEscaping)
                return false;
        }
        return true;
    }
}
