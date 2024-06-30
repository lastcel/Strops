package strops.actions;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import strops.cardmods.CardModDecreaseCostUntilPlayed;
import strops.relics.Atri;

public class AtriAction extends AbstractGameAction {
    @Override
    public void update(){
        if(Atri.BONUS.value>0){
            for ( AbstractCard c : DrawCardAction.drawnCards )
            {
                c.flash(Color.GREEN);
                CardModifierManager.addModifier(c, new CardModDecreaseCostUntilPlayed(Atri.BONUS.value));
            }
        }
        this.isDone = true;
    }
}
