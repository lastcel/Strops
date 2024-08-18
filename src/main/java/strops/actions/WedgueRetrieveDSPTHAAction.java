package strops.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import strops.relics.Wedgue;

public class WedgueRetrieveDSPTHAAction extends AbstractGameAction {
    Wedgue wedgue;

    public WedgueRetrieveDSPTHAAction(Wedgue wedgue){
        this.wedgue=wedgue;
    }

    @Override
    public void update(){
        if(!GeneralDiscardPileToHandAction.drawnCards.isEmpty()){
            wedgue.drawnCard=GeneralDiscardPileToHandAction.drawnCards.get(0);
        }
        this.isDone=true;
    }
}
