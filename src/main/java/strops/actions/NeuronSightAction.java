package strops.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import strops.relics.NeuronSight;

public class NeuronSightAction extends AbstractGameAction {
    NeuronSight neuronSight;

    public NeuronSightAction(NeuronSight neuronSight){
        this.neuronSight=neuronSight;
    }

    @Override
    public void update(){
        neuronSight.isReady=true;
        this.isDone=true;
    }
}
