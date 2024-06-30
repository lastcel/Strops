//Adapted from the ExhaustAction class in base game
package strops.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class ExcludeAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("ExhaustAction");

    public static final String[] TEXT = uiStrings.TEXT;

    private AbstractPlayer p;

    private boolean isRandom;

    private boolean anyNumber;

    private boolean canPickZero;

    public static int numExcluded;

    public ExcludeAction(int amount, boolean isRandom, boolean anyNumber, boolean canPickZero) {
        this.anyNumber = anyNumber;
        this.p = AbstractDungeon.player;
        this.canPickZero = canPickZero;
        this.isRandom = isRandom;
        this.amount = amount;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.actionType = AbstractGameAction.ActionType.EXHAUST;
    }

    public ExcludeAction(AbstractCreature target, AbstractCreature source, int amount, boolean isRandom, boolean anyNumber) {
        this(amount, isRandom, anyNumber);
        this.target = target;
        this.source = source;
    }

    public ExcludeAction(AbstractCreature target, AbstractCreature source, int amount, boolean isRandom) {
        this(amount, isRandom, false, false);
        this.target = target;
        this.source = source;
    }

    public ExcludeAction(AbstractCreature target, AbstractCreature source, int amount, boolean isRandom, boolean anyNumber, boolean canPickZero) {
        this(amount, isRandom, anyNumber, canPickZero);
        this.target = target;
        this.source = source;
    }

    public ExcludeAction(boolean isRandom, boolean anyNumber, boolean canPickZero) {
        this(99, isRandom, anyNumber, canPickZero);
    }

    public ExcludeAction(int amount, boolean canPickZero) {
        this(amount, false, false, canPickZero);
    }

    public ExcludeAction(int amount, boolean isRandom, boolean anyNumber) {
        this(amount, isRandom, anyNumber, false);
    }

    public ExcludeAction(int amount, boolean isRandom, boolean anyNumber, boolean canPickZero, float duration) {
        this(amount, isRandom, anyNumber, canPickZero);
        this.duration = this.startDuration = duration;
    }

    private void resetCardBeforeMoving(AbstractCard c) {
        if (this.p.hoveredCard == c)
            this.p.releaseCard();
        AbstractDungeon.actionManager.removeFromQueue(c);
        c.unhover();
        c.untip();
        c.stopGlowing();
        this.p.hand.group.remove(c);
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (this.p.hand.size() == 0) {
                this.isDone = true;
                return;
            }
            if (!this.anyNumber &&
                    this.p.hand.size() <= this.amount) {
                this.amount = this.p.hand.size();
                numExcluded = this.amount;
                int tmp = this.p.hand.size();
                for (int i = 0; i < tmp; i++) {
                    AbstractCard c = this.p.hand.getTopCard();
                    resetCardBeforeMoving(c);
                }
                CardCrawlGame.dungeon.checkForPactAchievement();
                return;
            }
            if (this.isRandom) {
                for (int i = 0; i < this.amount; i++)
                    resetCardBeforeMoving(this.p.hand.getRandomCard(AbstractDungeon.cardRandomRng));
                CardCrawlGame.dungeon.checkForPactAchievement();
            } else {
                numExcluded = this.amount;
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.amount, this.anyNumber, this.canPickZero);
                tickDuration();
                return;
            }
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group)
                resetCardBeforeMoving(c);
            CardCrawlGame.dungeon.checkForPactAchievement();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }
        tickDuration();
    }
}