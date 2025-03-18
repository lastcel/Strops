package strops.relics;

import basemod.abstracts.CustomBottleRelic;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.helpers.ModHelper;
import strops.patch.PatchCatalyst;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;
import java.util.function.Predicate;

public class Catalyst extends StropsAbstractRelic implements
        CustomBottleRelic, CustomSavable<ArrayList<Integer>> {
    public static final String ID = ModHelper.makePath(Catalyst.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(Catalyst.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(Catalyst.class.getSimpleName());
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    private boolean cardSelected = true;
    public static AbstractCard card = null;

    public static final int NUM1=1,NUM2=2,TIER=3;

    public static final IntSliderSetting RECEPTOR=new IntSliderSetting("Catalyst_Receptor", "N1", NUM1, 1,3);
    public static final IntSliderSetting DRAW=new IntSliderSetting("Catalyst_Draw", "N2", NUM2, 1,3);
    public static final IntSliderSetting MH=new IntSliderSetting("Catalyst_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("Catalyst_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("Catalyst_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(RECEPTOR);
        settings.add(DRAW);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public Catalyst() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        canCopy=false;
        canSpawnInBattle=false;
    }

    @Override
    public Predicate<AbstractCard> isOnCard() {
        return PatchCatalyst.PatchTool1.inCatalyst::get;
    }

    @Override
    public ArrayList<Integer> onSave() {
        ArrayList<Integer> cardsIndex=new ArrayList<>();
        for(AbstractCard c: AbstractDungeon.player.masterDeck.group){
            if(PatchCatalyst.PatchTool1.inCatalyst.get(c)){
                cardsIndex.add(AbstractDungeon.player.masterDeck.group.indexOf(c));
            }
        }
        return cardsIndex;
    }

    @Override
    public void onLoad(ArrayList<Integer> cardsIndex) {
        for(int i:cardsIndex){
            if(i>=0&&i<AbstractDungeon.player.masterDeck.group.size()){
                card=AbstractDungeon.player.masterDeck.group.get(i);
                if(card!=null){
                    PatchCatalyst.PatchTool1.inCatalyst.set(card,true);
                }
            }
        }
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);

        cardSelected = false;
        CardGroup cardGroup = CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck);
        if(cardGroup.size()<=RECEPTOR.value){
            cardSelected=true;
            for(AbstractCard c:cardGroup.group){
                PatchCatalyst.PatchTool1.inCatalyst.set(c,true);
            }
        } else {
            if (AbstractDungeon.isScreenUp) {
                AbstractDungeon.dynamicBanner.hide();
                AbstractDungeon.overlayMenu.cancelButton.hide();
                AbstractDungeon.previousScreen = AbstractDungeon.screen;
            }
            (AbstractDungeon.getCurrRoom()).phase = AbstractRoom.RoomPhase.INCOMPLETE;
            AbstractDungeon.gridSelectScreen.open(cardGroup, RECEPTOR.value, String.format(DESCRIPTIONS[5],RECEPTOR.value)+name+LocalizedStrings.PERIOD, false, false,false,false);
        }
    }

    @Override
    public void onUnequip() {
        for(AbstractCard c:AbstractDungeon.player.masterDeck.group){
            PatchCatalyst.PatchTool1.inCatalyst.set(c,false);
        }
    }

    @Override
    public void onCardDraw(AbstractCard drawnCard) {
        if(PatchCatalyst.PatchTool1.inCatalyst.get(drawnCard)){
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player,this));
            addToBot(new DrawCardAction(AbstractDungeon.player, DRAW.value));
        }
    }

    @Override
    public void update() {
        super.update();

        if (!cardSelected && AbstractDungeon.gridSelectScreen.selectedCards.size()==RECEPTOR.value) {
            cardSelected = true;
            for(AbstractCard c:AbstractDungeon.gridSelectScreen.selectedCards){
                PatchCatalyst.PatchTool1.inCatalyst.set(c,true);
            }
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
    }

    @Override
    public boolean canSpawn() {
        CardGroup cardGroup = CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck);
        return cardGroup.size()>=RECEPTOR.value;
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], RECEPTOR.value, DRAW.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], RECEPTOR.value, DRAW.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }
}
