package strops.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import strops.relics.Wedgue;

public class WedgueRemoveRetainAction extends AbstractGameAction {
    @Override
    public void update(){
        for(AbstractRelic r:AbstractDungeon.player.relics){
            if(r.relicId.equals(Wedgue.ID)){
                for(AbstractCard c:((Wedgue)r).drawnCards){
                    c.retain=false;
                }
                //((Wedgue)r).drawnCard.retain=false;
            }
        }
        this.isDone=true;
    }
}
