//这段代码照搬了AKDsMoreRelics mod中的同名类，感谢akdream10086！
package strops.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import strops.relics.FishingNet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class MyMoveCardsAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("stslib:MoveCardsAction");

    private AbstractPlayer p;

    private CardGroup source;

    private CardGroup destination;

    private Predicate<AbstractCard> predicate;

    private Consumer<List<AbstractCard>> callback;

    private boolean isRandom;

    private boolean isSpecific;

    private List<AbstractCard> callbackList = new ArrayList<>();

    public MyMoveCardsAction(CardGroup destination, CardGroup source, int amount, boolean isRandom, Predicate<AbstractCard> predicate, Consumer<List<AbstractCard>> callback) {
        this.p = AbstractDungeon.player;
        this.destination = destination;
        this.source = source;
        this.predicate = predicate;
        this.callback = callback;
        this.isRandom = isRandom;
        this.isSpecific = false;
        setValues(this.p, this.p, amount);
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
    }

    public MyMoveCardsAction(CardGroup destination, CardGroup source, int amount, boolean isRandom, Predicate<AbstractCard> predicate) {
        this(destination, source, amount, isRandom, predicate, null);
    }

    public MyMoveCardsAction(CardGroup destination, CardGroup source, int amount, boolean isRandom) {
        this(destination, source, amount, isRandom, card -> true, null);
    }

    public void moveCards() {
        for (AbstractCard card : this.callbackList) {
            if (this.source == this.p.exhaustPile)
                card.unfadeOut();
            if (this.destination == this.p.hand && this.p.hand.size() == BaseMod.MAX_HAND_SIZE) {
                this.source.moveToDiscardPile(card);
                this.p.createHandIsFullDialog();
                continue;
            }
            card.untip();
            card.unhover();
            card.stopGlowing();
            card.lighten(true);
            card.setAngle(0.0F);
            card.drawScale = 0.12F;
            card.targetDrawScale = 0.75F;
            card.current_x = CardGroup.DISCARD_PILE_X;
            card.current_y = 0.0F;
            if(card.cost>0){
                card.freeToPlayOnce=true;
            }
            this.source.removeCard(card);
            if (this.destination == this.p.drawPile) {
                this.destination.addToRandomSpot(card);
            } else {
                this.destination.addToTop(card);
            }
            this.p.hand.refreshHandLayout();
            this.p.hand.applyPowers();
        }
        if (this.callback != null)
            this.callback.accept(this.callbackList);
    }

    public void update() {
        //Strops.logger.info("时长="+duration+"，isDone="+isDone);
        if (this.duration == Settings.ACTION_DUR_MED) {
            if (this.isSpecific) {
                moveCards();
                this.isDone = true;
                return;
            }
            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : this.source.group) {
                if (this.predicate.test(c)) {
                    if (this.source == this.p.drawPile) {
                        tmp.addToRandomSpot(c);
                        continue;
                    }
                    tmp.addToTop(c);
                }
            }
            if (tmp.isEmpty()) {
                this.isDone = true;
                return;
            }
            if (this.isRandom) {
                this.amount = Math.min(this.amount, tmp.size());
                Collections.shuffle(tmp.group, new Random(AbstractDungeon.shuffleRng.randomLong()));
                for (int i = 0; i < this.amount; i++)
                    this.callbackList.add(tmp.getNCardFromTop(i));
                moveCards();
                this.isDone = true;
                return;
            }
            AbstractDungeon.gridSelectScreen.open(tmp, this.amount, true, makeText());
            tickDuration();
            return;
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            this.callbackList.addAll(AbstractDungeon.gridSelectScreen.selectedCards);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.p.hand.refreshHandLayout();
            moveCards();
        }
        tickDuration();
    }

    private String makeText() {
        String ret=CardCrawlGame.languagePack.getRelicStrings(FishingNet.ID).DESCRIPTIONS[5];

        String location = null;
        if (this.destination == this.p.hand) {
            location = uiStrings.TEXT_DICT.get("HAND");
        } else if (this.destination == this.p.drawPile) {
            location = uiStrings.TEXT_DICT.get("DRAW");
        } else if (this.destination == this.p.discardPile) {
            location = uiStrings.TEXT_DICT.get("DISCARD");
        } else if (this.destination == this.p.exhaustPile) {
            location = uiStrings.TEXT_DICT.get("EXHAUST");
        }
        if (location == null)
            location = "<Unknown>";
        return String.format(ret, amount, location);
    }
}
