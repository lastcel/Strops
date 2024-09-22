//Partially copied from Feather Duster in Reliquary mod, credits to Cae!
package strops.relics;

import basemod.abstracts.CustomBottleRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import strops.helpers.ModHelper;
import strops.patch.PatchGrabbyHands;
import strops.utilities.CardGrabbyInfo;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;
import java.util.function.Predicate;

public class GrabbyHands extends StropsAbstractRelic implements ClickableRelic, CustomBottleRelic,
        CustomSavable<ArrayList<CardGrabbyInfo>> {
    public static final String ID = ModHelper.makePath(GrabbyHands.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(GrabbyHands.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(GrabbyHands.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.SHOP;
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    //public static ArrayList<AbstractCard> cards = new ArrayList<>();
    public boolean cardsSelected=true;
    private AbstractDungeon.CurrentScreen prevScreen;
    private AbstractRoom.RoomPhase prevPhase;

    public static final int NUM1=1,NUM2=4;

    public static final IntSliderSetting SIZE=new IntSliderSetting("GrabbyHands_Size", "N1", NUM1, 1,3);
    public static final IntSliderSetting LATENCY=new IntSliderSetting("GrabbyHands_Latency", "N2", NUM2, 1,9);
    public static final IntSliderSetting MH=new IntSliderSetting("GrabbyHands_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("GrabbyHands_G","G",0,-100,100);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(SIZE);
        settings.add(LATENCY);
        settings.add(MH);
        settings.add(G);
        return settings;
    }

    public GrabbyHands() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), RELIC_TIER, LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        canCopy=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
    }

    @Override
    public void onUnequip(){
        for(AbstractCard c:AbstractDungeon.player.masterDeck.group){
            PatchGrabbyHands.PatchTool6.isGrabbed.set(c,false);
        }
        /*
        for(AbstractCard c:cards){
            AbstractCard cChecked=AbstractDungeon.player.masterDeck.getSpecificCard(c);
            if(cChecked!=null){
                PatchGrabbyHands.PatchTool6.isGrabbed.set(cChecked,false);
            }
        }

         */
    }

    @Override
    public void onVictory(){
        for(AbstractCard c:AbstractDungeon.player.masterDeck.group){
            if(PatchGrabbyHands.PatchTool6.isGrabbed.get(c)){
                int lastAge=PatchGrabbyHands.PatchTool7.ages.get(c);
                PatchGrabbyHands.PatchTool7.ages.set(c,lastAge+1);
            }
        }
    }

    @Override
    public ArrayList<CardGrabbyInfo> onSave() {
        ArrayList<CardGrabbyInfo> savedCards=new ArrayList<>();

        for(AbstractCard c:AbstractDungeon.player.masterDeck.group){
            if(PatchGrabbyHands.PatchTool6.isGrabbed.get(c)){
                CardGrabbyInfo oneInfo=new CardGrabbyInfo(AbstractDungeon.player.masterDeck.group.indexOf(c),PatchGrabbyHands.PatchTool7.ages.get(c));
                savedCards.add(oneInfo);
            }
        }
        return savedCards;
    }

    @Override
    public void onLoad(ArrayList<CardGrabbyInfo> cardIndexes) {
        //cards.clear();
        for(CardGrabbyInfo info:cardIndexes){
            int i=info.indexInDeck,a=info.passedCombats;
            if(i>=0&&i<AbstractDungeon.player.masterDeck.group.size()){
                AbstractCard c=AbstractDungeon.player.masterDeck.group.get(i);
                if(c!=null){
                    PatchGrabbyHands.PatchTool6.isGrabbed.set(c,true);
                    PatchGrabbyHands.PatchTool7.ages.set(c,a);
                    //cards.add(c);
                }
            }
        }
    }

    @Override
    public Predicate<AbstractCard> isOnCard() {
        return PatchGrabbyHands.PatchTool6.isGrabbed::get;
    }

    @Override
    public void onRightClick(){
        AbstractRoom currRoom=AbstractDungeon.getCurrRoom();
        if((currRoom!=null)&&(currRoom.phase == AbstractRoom.RoomPhase.COMBAT)){
            return;
        }

        if(AbstractDungeon.screen==AbstractDungeon.CurrentScreen.GRID||AbstractDungeon.screen==AbstractDungeon.CurrentScreen.CARD_REWARD){
            return;
        }

        CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : AbstractDungeon.player.masterDeck.getPurgeableCards().group) {
            if (PatchGrabbyHands.PatchTool6.isGrabbed.get(card)&&
                    PatchGrabbyHands.PatchTool7.ages.get(card)>=LATENCY.value){
                tmp.addToTop(card);
            }
        }
        if (tmp.group.isEmpty()) {
            this.cardsSelected = true;
            return;
        }
        this.cardsSelected = false;
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        prevPhase=AbstractDungeon.getCurrRoom().phase;
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
        prevScreen = AbstractDungeon.screen;
        AbstractDungeon.gridSelectScreen.open(tmp, tmp.size(), true, this.DESCRIPTIONS[6]);
    }

    @Override
    public void update() {
        super.update();
        if (!cardsSelected && AbstractDungeon.screen == prevScreen) {
            cardsSelected = true;
            int numCards = AbstractDungeon.gridSelectScreen.selectedCards.size();
            float sigmoid = 1 / (1 + (float)Math.pow(Math.E, -numCards * .4)) - .5f;
            sigmoid *= .8f;
            for (int i = 0; i < numCards; i++) {
                float xPercent = numCards == 1 ? .5f : MathUtils.lerp(.5f - sigmoid, .5f + sigmoid, (float)i / (numCards - 1));
                AbstractCard card = AbstractDungeon.gridSelectScreen.selectedCards.get(i);
                //cards.remove(card);
                card.untip();
                card.unhover();
                card.stopGlowing();
                AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(card, 0, 0));
                card.current_x = Settings.WIDTH / 2f;
                card.current_y = 0;
                card.target_x = Settings.WIDTH * xPercent;
                card.target_y = Settings.HEIGHT / 2f;
                AbstractDungeon.player.masterDeck.removeCard(card);
            }
            AbstractDungeon.getCurrRoom().phase = prevPhase;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
    }

    /*
    @Override
    public void onEnterRoom(AbstractRoom room){
        isUsed=false;
    }

     */

    /*
    @Override
    public void onTrigger(){
        InputHelper.moveCursorToNeutralPosition();
        ArrayList<AbstractCard> grabbyHandChoices = AbstractDungeon.getRewardCards();
        if(AbstractDungeon.isScreenUp){
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.cardRewardScreen.chooseOneOpen(grabbyHandChoices);
    }

     */

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], SIZE.value, LATENCY.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], SIZE.value, LATENCY.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }
}
