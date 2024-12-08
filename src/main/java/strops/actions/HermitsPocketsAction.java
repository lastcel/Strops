package strops.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import strops.cards.FinalForm;
import strops.relics.HermitsPockets;

import java.util.ArrayList;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.*;

public class HermitsPocketsAction extends AbstractGameAction {
    private boolean retrieveCard = false;
    public boolean shouldUpgrade;

    public HermitsPocketsAction(boolean shouldUpgrade) {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.shouldUpgrade = shouldUpgrade;
    }

    public void update() {
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.isDone = true;
            return;
        }
        if (this.duration == Settings.ACTION_DUR_FAST) {
            ArrayList<AbstractCard> temp = generateCardChoices();
            if(temp.isEmpty()){
                this.isDone = true;
                return;
            }
            AbstractDungeon.cardRewardScreen.customCombatOpen(temp, CardRewardScreen.TEXT[1], true);
            tickDuration();
            return;
        }
        if (!this.retrieveCard) {
            if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
                AbstractCard codexCard = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
                codexCard.current_x = -1000.0F * Settings.xScale;
                if(codexCard.isInnate){
                    AbstractDungeon.effectList.add(new ShowCardAndAddToDrawPileEffect(codexCard, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, false));
                } else {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToDrawPileEffect(codexCard, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, true));
                }
                AbstractDungeon.cardRewardScreen.discoveryCard = null;
            }
            this.retrieveCard = true;
        }
        tickDuration();
    }

    private ArrayList<AbstractCard> generateCardChoices() {
        ArrayList<AbstractCard> derp = new ArrayList<>();
        int neededSize=Math.min(HermitsPockets.BONUS.value,getValidSize());
        while (derp.size() != neededSize) {
            boolean dupe = false;
            AbstractCard tmp = AbstractDungeon.returnTrulyRandomCardInCombat();
            for (AbstractCard c : derp) {
                if (c.cardID.equals(tmp.cardID)) {
                    dupe = true;
                    break;
                }
            }
            if(!dupe){
                for(AbstractCard c:AbstractDungeon.player.masterDeck.group){
                    if(c.cardID.equals(tmp.cardID)){
                        dupe=true;
                        break;
                    }
                }
            }

            if (!dupe){
                AbstractCard tmp2=tmp.makeCopy();
                if(shouldUpgrade){
                    tmp2.upgrade();
                }
                derp.add(tmp2);
            }
        }

        if(AbstractDungeon.miscRng.randomBoolean(HermitsPockets.CHANCE.value/100.0f)&&
                player.masterDeck.group.stream().noneMatch(c->c.cardID.equals(FinalForm.ID))){
            AbstractCard treasure=new FinalForm();
            if(shouldUpgrade){
                treasure.upgrade();
            }
            derp.add(treasure);
        }

        return derp;
    }

    public static int getValidSize() {
        ArrayList<AbstractCard> list = new ArrayList<>();
        for (AbstractCard c : srcCommonCardPool.group) {
            if (!c.hasTag(AbstractCard.CardTags.HEALING)) {
                list.add(c);
            }
        }
        for (AbstractCard c : srcUncommonCardPool.group) {
            if (!c.hasTag(AbstractCard.CardTags.HEALING)) {
                list.add(c);
            }
        }
        for (AbstractCard c : srcRareCardPool.group) {
            if (!c.hasTag(AbstractCard.CardTags.HEALING)) {
                list.add(c);
            }
        }

        for(AbstractCard c:AbstractDungeon.player.masterDeck.group){
            list.removeIf(c2 -> c2.cardID.equals(c.cardID));
        }

        return list.size();
    }
}
