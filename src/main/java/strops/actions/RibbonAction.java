package strops.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import strops.relics.Ribbon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RibbonAction extends AbstractGameAction {
    private AbstractCreature c;

    public RibbonAction(AbstractCreature c) {
        this.c = c;
        this.duration = 0.5F;
    }

    public void update() {
        ArrayList<AbstractPower> debuffsList=new ArrayList<>();
        for (AbstractPower p : this.c.powers) {
            if (p.type == AbstractPower.PowerType.DEBUFF)
            {
                debuffsList.add(p);
            }
        }
        if(!debuffsList.isEmpty()){
            //int rng;
            //AbstractPower pow;
            //rng=AbstractDungeon.miscRng.random(0,debuffsList.size()-1);
            //pow=debuffsList.get(rng);
            //addToTop(new RemoveSpecificPowerAction(this.c, this.c, pow.ID));

            Collections.shuffle(debuffsList,new Random(AbstractDungeon.eventRng.randomLong()));
            for(int i=0;i<Ribbon.BONUS.value&&i<debuffsList.size();i++){
                addToTop(new RemoveSpecificPowerAction(this.c, this.c, debuffsList.get(i).ID));
            }
        }
        this.isDone = true;
    }
}
