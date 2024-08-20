package strops.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.OnLoseTempHpRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import strops.actions.MyMoveCardsAction;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class FishingNet extends StropsAbstractRelic implements OnLoseTempHpRelic {
    public static final String ID = ModHelper.makePath(FishingNet.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(FishingNet.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(FishingNet.class.getSimpleName());
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public static final int NUM1=2,NUM2=15,NUM3=5,NUM4=10,TIER=3;

    public static final IntSliderSetting CARDS=new IntSliderSetting("FishingNet_Cards","N1", NUM1,1,5);
    public static final IntSliderSetting INITIAL=new IntSliderSetting("FishingNet_Initial","N2", NUM2,30);
    public static final IntSliderSetting UP_REGULATE=new IntSliderSetting("FishingNet_Up-regulate","N3", NUM3,10);
    public static final IntSliderSetting DOWN_REGULATE=new IntSliderSetting("FishingNet_Down-regulate","N4", NUM4,10);
    public static final IntSliderSetting MH=new IntSliderSetting("FishingNet_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("FishingNet_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("FishingNet_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(CARDS);
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
        secondCounter=-999;
    }

    @Override
    public void atBattleStart(){
        secondCounter=0;
        if(counter>0){
            grayscale=true;
        }
    }

    @Override
    public void wasHPLost(int damageAmount){
        secondCounter+=damageAmount;
        if(secondCounter>=counter){
            grayscale=false;
        }
    }

    @Override
    public int onLoseTempHp(DamageInfo info, int damageAmount){
        secondCounter+=damageAmount;
        if(secondCounter>=counter){
            grayscale=false;
        }

        return damageAmount;
    }

    @Override
    public void onVictory(){
        if(grayscale){
            counter-=DOWN_REGULATE.value;
            if(counter<0){
                counter=0;
            }
        } else {
            counter+=UP_REGULATE.value;
        }
        grayscale=false;
        secondCounter=-999;
        setDescriptionAfterRegulating();
    }

    @Override
    public void atTurnStart(){
        if(!grayscale){
            AbstractPlayer p=AbstractDungeon.player;
            flash();
            addToTop(new MyMoveCardsAction(p.discardPile,p.drawPile,CARDS.value,false));
            addToTop(new RelicAboveCreatureAction(p,this));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0],CARDS.value,INITIAL.value,UP_REGULATE.value,DOWN_REGULATE.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0],CARDS.value,INITIAL.value,UP_REGULATE.value,DOWN_REGULATE.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }

    private void setDescriptionAfterRegulating(){
        description = String.format(this.DESCRIPTIONS[0],CARDS.value,counter,UP_REGULATE.value,DOWN_REGULATE.value);
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
