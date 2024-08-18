package strops.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import strops.actions.MyMoveCardsAction;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class FishingNet extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(FishingNet.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(FishingNet.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(FishingNet.class.getSimpleName());
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public static final int NUM1=15,NUM2=5,NUM3=5,TIER=2;

    public static final IntSliderSetting INITIAL=new IntSliderSetting("FishingNet_Initial","N1", NUM1,30);
    public static final IntSliderSetting UP_REGULATE=new IntSliderSetting("FishingNet_Up-regulate","N2", NUM2,10);
    public static final IntSliderSetting DOWN_REGULATE=new IntSliderSetting("FishingNet_Down-regulate","N3", NUM3,10);
    public static final IntSliderSetting MH=new IntSliderSetting("FishingNet_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("FishingNet_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("FishingNet_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(INITIAL);
        settings.add(UP_REGULATE);
        settings.add(DOWN_REGULATE);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public FishingNet() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        counter=INITIAL.value;
    }

    @Override
    public void atBattleStart(){
        secondCounter=0;
        if(counter>0){
            grayscale=true;
        }
    }

    @Override
    public void onLoseHp(int damageAmount){
        secondCounter+=damageAmount;
        if(secondCounter>=counter){
            grayscale=false;
        }
    }

    @Override
    public void onVictory(){
        if(grayscale){
            counter-=DOWN_REGULATE.value;
        } else {
            counter+=UP_REGULATE.value;
        }
        grayscale=false;
        secondCounter=-1;
        setDescriptionAfterRegulating();
    }

    @Override
    public void atTurnStart(){
        if(!grayscale){
            AbstractPlayer p=AbstractDungeon.player;
            flash();
            addToTop(new MyMoveCardsAction(p.discardPile,p.drawPile,1,false));
            addToTop(new RelicAboveCreatureAction(p,this));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0],INITIAL.value,UP_REGULATE.value,DOWN_REGULATE.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0],INITIAL.value,UP_REGULATE.value,DOWN_REGULATE.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }

    private void setDescriptionAfterRegulating(){
        description = String.format(this.DESCRIPTIONS[0],counter,UP_REGULATE.value,DOWN_REGULATE.value);
        tips.clear();
        tips.add(new PowerTip(name, description));
        initializeTips();
    }

    @Override
    public void setCounter(int setCounter){
        counter=setCounter;
        setDescriptionAfterRegulating();
    }
}
