package strops.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import strops.cards.Aurora;

public class FilmStarryCurtainAction extends AbstractGameAction {
    public FilmStarryCurtainAction(AbstractCreature source, int amount) {
        setValues(this.target, source, amount);
        this.actionType = AbstractGameAction.ActionType.WAIT;
    }

    public void update() {
        int toCreate = this.amount - AbstractDungeon.player.hand.size();
        if (toCreate > 0)
        {
            addToTop(new MakeTempCardInHandAction(new Aurora(), toCreate,false));
        }
        this.isDone = true;
    }
}
