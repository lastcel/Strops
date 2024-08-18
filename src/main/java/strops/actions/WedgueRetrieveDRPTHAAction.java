package strops.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import strops.relics.Wedgue;

public class WedgueRetrieveDRPTHAAction extends AbstractGameAction {
    Wedgue wedgue;

    public WedgueRetrieveDRPTHAAction(Wedgue wedgue){
        this.wedgue=wedgue;
    }

    @Override
    public void update(){
        if(!GeneralDrawPileToHandAction.drawnCards.isEmpty()){
            wedgue.drawnCard=GeneralDrawPileToHandAction.drawnCards.get(0);
        }
        this.isDone=true;
    }
}
