package strops.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import strops.relics.Wedgue;

public class WedgueRetainAction extends AbstractGameAction {
    Wedgue wedgue;

    public WedgueRetainAction(Wedgue wedgue){
        this.wedgue=wedgue;
    }

    @Override
    public void update(){
        for(AbstractCard c:wedgue.drawnCards){
            if(!c.isEthereal){
                c.retain=true;
            }
        }
        /*
        if(wedgue.drawnCard!=null&&!wedgue.drawnCard.isEthereal){
            wedgue.drawnCard.retain=true;
        }

         */
        this.isDone=true;
    }
}
