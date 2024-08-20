package strops.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.ObtainKeyEffect;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class SiphonGem extends StropsAbstractRelic implements ClickableRelic {
    public static final String ID = ModHelper.makePath(SiphonGem.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(SiphonGem.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(SiphonGem.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.COMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    public static final int NUM1=1,NUM2=100,TIER=1;

    public static final IntSliderSetting USABLE=new IntSliderSetting("SiphonGem_Usable","N1",NUM1,1,4);
    public static final IntSliderSetting BONUS=new IntSliderSetting("SiphonGem_Bonus","N2",NUM2,300);
    public static final IntSliderSetting MH=new IntSliderSetting("SiphonGem_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("SiphonGem_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("SiphonGem_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(USABLE);
        settings.add(BONUS);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public SiphonGem() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        counter=USABLE.value;
        pulse=true;
    }

    @Override
    public void onRightClick(){
        if(counter<=0) {
            return;
        }
        AbstractRoom currRoom=AbstractDungeon.getCurrRoom();
        if((currRoom!=null)&&(currRoom.phase == AbstractRoom.RoomPhase.COMBAT)){
            return;
        }

        if(hasTriColor()){
            if(BONUS.value>0){
                AbstractDungeon.player.gainGold(BONUS.value);
                counter--;
                if(counter==0){
                    counter=-2;
                    pulse=false;
                    usedUp();
                }
            }
            return;
        }

        int rng;
        ObtainKeyEffect.KeyColor cl;

        ArrayList<ObtainKeyEffect.KeyColor> array=new ArrayList<>();
        if(!Settings.hasRubyKey){
            array.add(ObtainKeyEffect.KeyColor.RED);
        }
        if(!Settings.hasSapphireKey){
            array.add(ObtainKeyEffect.KeyColor.BLUE);
        }
        if(!Settings.hasEmeraldKey){
            array.add(ObtainKeyEffect.KeyColor.GREEN);
        }

        rng=AbstractDungeon.merchantRng.random(0,array.size()-1);
        cl=array.get(rng);
        AbstractDungeon.topLevelEffects.add(new ObtainKeyEffect(cl));
        counter--;
        if(counter==0){
            counter=-2;
            pulse=false;
            usedUp();
        }
    }

    @Override
    public void setCounter(int setCounter){
        this.counter=setCounter;
        if (setCounter == -2) {
            usedUp();
            this.counter = -2;
        } else {
            flash();
            pulse=true;
        }
    }

    @Override
    public boolean canSpawn() {
        return ( !Settings.isEndless && (AbstractDungeon.actNum <= 3) );
    }

    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0],USABLE.value,BONUS.value);
    }

    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0],USABLE.value,BONUS.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }
}
