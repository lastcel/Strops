package strops.relics;

import basemod.abstracts.CustomBottleRelic;
import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.mod.stslib.relics.OnAfterUseCardRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.helpers.ModHelper;
import strops.patch.PatchMosquitoCoil;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;
import java.util.function.Predicate;

public class MosquitoCoil extends StropsAbstractRelic implements CustomBottleRelic, CustomSavable<ArrayList<Integer>>, OnAfterUseCardRelic {
    public static final String ID = ModHelper.makePath(MosquitoCoil.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(MosquitoCoil.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(MosquitoCoil.class.getSimpleName());
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    private boolean cardSelected=true;

    public static final int NUM1=6,NUM2=2,NUM3=2,TIER=2;

    public static final IntSliderSetting BONUS=new IntSliderSetting("NNFN4_Bonus","N1", NUM1,1,10);
    public static final IntSliderSetting RECEPTOR=new IntSliderSetting("NNFN4_Receptor","N2", NUM2,4);
    public static final IntSliderSetting VIGOR=new IntSliderSetting("NNFN4_Vigor","N3", NUM3,1,3);
    public static final IntSliderSetting MH=new IntSliderSetting("NNFN4_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("NNFN4_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("NNFN4_R","R", TIER,0,5);
    //public static final IntSliderSetting P=new IntSliderSetting("NNFN4_P","P", 250,100,400);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(BONUS);
        settings.add(RECEPTOR);
        settings.add(VIGOR);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        //settings.add(P);
        return settings;
    }

    public MosquitoCoil() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        canCopy=false;
        canSpawnInBattle=false;
    }

    @Override
    public Predicate<AbstractCard> isOnCard() {
        return PatchMosquitoCoil.PatchTool1.inNoNameForNow4::get;
    }

    @Override
    public ArrayList<Integer> onSave() {
        ArrayList<Integer> cardsIndex=new ArrayList<>();
        for(AbstractCard c:AbstractDungeon.player.masterDeck.group){
            if(PatchMosquitoCoil.PatchTool1.inNoNameForNow4.get(c)){
                cardsIndex.add(AbstractDungeon.player.masterDeck.group.indexOf(c));
            }
        }
        return cardsIndex;
    }

    @Override
    public void onLoad(ArrayList<Integer> cardsIndex) {
        for(int i:cardsIndex){
            if(i>=0&&i<AbstractDungeon.player.masterDeck.group.size()){
                AbstractCard card=AbstractDungeon.player.masterDeck.group.get(i);
                if(card!=null){
                    PatchMosquitoCoil.PatchTool1.inNoNameForNow4.set(card,true);
                }
            }
        }
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);

        if(RECEPTOR.value==0){
            return;
        }

        cardSelected = false;
        CardGroup cardGroup = CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck);
        cardGroup.group.removeIf(c -> (c.type != AbstractCard.CardType.ATTACK));
        if(cardGroup.size()<=RECEPTOR.value){
            cardSelected=true;
            for(AbstractCard c:cardGroup.group){
                PatchMosquitoCoil.PatchTool1.inNoNameForNow4.set(c,true);
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
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            PatchMosquitoCoil.PatchTool1.inNoNameForNow4.set(c, false);
        }
    }

    @Override
    public void update() {
        super.update();

        if (!cardSelected && AbstractDungeon.gridSelectScreen.selectedCards.size()==RECEPTOR.value) {
            cardSelected = true;
            for(AbstractCard c:AbstractDungeon.gridSelectScreen.selectedCards){
                PatchMosquitoCoil.PatchTool1.inNoNameForNow4.set(c,true);
            }
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard cardIn, UseCardAction action){
        if(PatchMosquitoCoil.PatchTool1.inNoNameForNow4.get(cardIn)){
            if (AbstractDungeon.player.getRelic(ID) != this){
                return;
            }
            flash();
            AbstractPlayer p=AbstractDungeon.player;
            addToBot(new RelicAboveCreatureAction(p, this));
            addToBot(new ApplyPowerAction(p,p,new VigorPower(p,VIGOR.value),VIGOR.value));
        }
    }

    @Override
    public boolean canSpawn() {
        CardGroup cardGroup = CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck);
        cardGroup.group.removeIf(c -> (c.type != AbstractCard.CardType.ATTACK));
        return cardGroup.size()>=RECEPTOR.value;
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0],BONUS.value,RECEPTOR.value,VIGOR.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0],BONUS.value,RECEPTOR.value,VIGOR.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }

    /*
    @Override
    public int getPrice(){
        return P.value;
    }

     */
}
