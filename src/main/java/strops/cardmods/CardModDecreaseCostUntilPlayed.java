package strops.cardmods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class CardModDecreaseCostUntilPlayed extends AbstractCardModifier {
    public static final String ID = "Strops:CardModDecreaseCostUntilPlayed";

    int amount;

    public CardModDecreaseCostUntilPlayed(int amount) {
        this.amount = -amount;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        return card.cost >= 1;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        if(-amount>card.cost){
            amount=-card.cost;
        }
        card.updateCost(amount);
    }

    @Override
    public boolean removeOnCardPlayed(AbstractCard card) {
        return true;
    }

    @Override
    public void onRemove(AbstractCard card) {
        super.onRemove(card);
        card.updateCost(-amount);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
    @Override
    public AbstractCardModifier makeCopy() {
        return new CardModDecreaseCostUntilPlayed(amount);
    }
}
