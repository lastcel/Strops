package strops.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import strops.relics.Wedgue;

import java.util.ArrayList;
import java.util.function.Predicate;

public class GeneralDiscardPileToHandAction extends AbstractGameAction {
    private AbstractPlayer p;
    private Predicate<AbstractCard> filter;
    public static ArrayList<AbstractCard> drawnCards=new ArrayList<>();
    public Wedgue wedgue=null;

    public GeneralDiscardPileToHandAction(int amount, Predicate<AbstractCard> filter) {
        this.p = AbstractDungeon.player;
        setValues(this.p, this.p, amount);
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
        this.filter=filter;
        //drawnCards.clear();
    }

    public GeneralDiscardPileToHandAction(Wedgue wedgue, Predicate<AbstractCard> filter) {
        this(0,filter);
        this.wedgue=wedgue;
        //drawnCards.clear();
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED) {
            drawnCards.clear();
            if(wedgue!=null){
                amount=wedgue.discardPart;
                //Strops.logger.info("需要从弃牌堆中取牌数量："+amount);
            }

            if (this.p.discardPile.isEmpty()) {
                this.isDone = true;
                return;
            }
            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : this.p.discardPile.group) {
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

                    if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
                        AbstractDungeon.player.hand.addToHand(card);
                        card.unhover();
                        card.setAngle(0.0F, true);
                        card.lighten(false);
                        card.drawScale = 0.12F;
                        card.targetDrawScale = 0.75F;
                        card.applyPowers();
                        AbstractDungeon.player.discardPile.removeCard(card);
                        drawnCards.add(card);
                    }
                    AbstractDungeon.player.hand.refreshHandLayout();
                    AbstractDungeon.player.hand.glowCheck();
                }
            }
            this.isDone = true;
        }
        tickDuration();
    }
}
