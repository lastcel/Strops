package strops.actions;


import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.function.Predicate;

public class GeneralDrawPileToHandAction extends AbstractGameAction {
    private AbstractPlayer p;
    private Predicate<AbstractCard> filter;
    public static ArrayList<AbstractCard> drawnCards=new ArrayList<>();

    public GeneralDrawPileToHandAction(int amount, Predicate<AbstractCard> filter) {
        this.p = AbstractDungeon.player;
        setValues(this.p, this.p, amount);
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
        this.filter=filter;
        //drawnCards.clear();
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED) {
            drawnCards.clear();

            if (this.p.drawPile.isEmpty()) {
                this.isDone = true;
                return;
            }
            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : this.p.drawPile.group) {
                if (filter.test(c))
                    tmp.addToRandomSpot(c);
            }
            if (tmp.isEmpty()) {
                this.isDone = true;
                return;
            }
            for (int i = 0; i < this.amount; i++) {
                if (!tmp.isEmpty()) {
                    tmp.shuffle();
                    AbstractCard card = tmp.getBottomCard();
                    tmp.removeCard(card);
                    if (this.p.hand.size() == BaseMod.MAX_HAND_SIZE) {
                        this.p.drawPile.moveToDiscardPile(card);
                        this.p.createHandIsFullDialog();
                    } else {
                        card.unhover();
                        card.lighten(true);
                        card.setAngle(0.0F);
                        card.drawScale = 0.12F;
                        card.targetDrawScale = 0.75F;
                        card.current_x = CardGroup.DRAW_PILE_X;
                        card.current_y = CardGroup.DRAW_PILE_Y;
                        this.p.drawPile.removeCard(card);
                        AbstractDungeon.player.hand.addToTop(card);
                        AbstractDungeon.player.hand.refreshHandLayout();
                        AbstractDungeon.player.hand.applyPowers();
                        drawnCards.add(card);
                    }
                }
            }
            this.isDone = true;
        }
        tickDuration();
    }
}

