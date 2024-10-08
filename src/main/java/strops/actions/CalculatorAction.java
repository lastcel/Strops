package strops.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import strops.relics.Calculator;

public class CalculatorAction extends AbstractGameAction {
    @Override
    public void update(){
        AbstractRelic r=AbstractDungeon.player.getRelic(Calculator.ID);
        if(r==null){
            this.isDone=true;
            return;
        }

        if(DrawCardAction.drawnCards.isEmpty()){
            r.counter+=1;
        }

        for(AbstractCard c:DrawCardAction.drawnCards)
        {
            if(!c.freeToPlayOnce&&c.costForTurn>=2){
                r.counter+=c.costForTurn;
            } else {
                r.counter+=1;
            }
            break;
        }

        if(r.counter>Calculator.THRESHOLD.value){
            r.flash();
            r.grayscale=true;
            int count = AbstractDungeon.player.hand.size();
            if(count!=0){
                addToTop(new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, count, true));
            }
        }

        this.isDone = true;
    }
}
