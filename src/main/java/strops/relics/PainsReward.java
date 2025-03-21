package strops.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class PainsReward extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(PainsReward.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(PainsReward.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(PainsReward.class.getSimpleName());
    private static final LandingSound LANDING_SOUND = LandingSound.HEAVY;

    public static final int NUM1=30,NUM2=2,NUM3=20,TIER=1;

    public static final IntSliderSetting THRESHOLD=new IntSliderSetting("PainsReward_Threshold_v0.16.12","N1", NUM1,1,50);
    public static final IntSliderSetting BONUS=new IntSliderSetting("PainsReward_Bonus_v0.16.12","N2", NUM2,5);
    public static final IntSliderSetting GOLD=new IntSliderSetting("PainsReward_Gold","N3", NUM3,50);
    public static final IntSliderSetting MH=new IntSliderSetting("PainsReward_MH_v0.16.12","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("PainsReward_G_v0.16.12","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("PainsReward_R_v0.16.12","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(THRESHOLD);
        settings.add(BONUS);
        settings.add(GOLD);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public PainsReward() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
    }

    @Override
    public void onTrigger(){
        AbstractPlayer p = AbstractDungeon.player;
        if (p.currentHealth <= THRESHOLD.value && p.currentHealth > 0 && BONUS.value > 0) {
            flash();
            addToTop(new RelicAboveCreatureAction(p, this));
            p.increaseMaxHp(BONUS.value, true);
            if(hasTriColor()&&GOLD.value>0){
                AbstractDungeon.player.gainGold(GOLD.value);
            }
        }
    }

    /*
    @Override
    public int onPlayerHeal(int healAmount){
        pulse=AbstractDungeon.player.currentHealth<=THRESHOLD.value;
        if(pulse){
            flash();
        }
        return healAmount;
    }

    @Override
    public void wasHPLost(int damageAmount){
        pulse=AbstractDungeon.player.currentHealth<=THRESHOLD.value;
        if(pulse){
            flash();
        }
    }

     */

    /*
    @Override
    public void update(){
        super.update();
        if(isObtained){
            pulse=AbstractDungeon.player.currentHealth<=THRESHOLD.value;
        }
    }

     */

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0],THRESHOLD.value,BONUS.value,GOLD.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0],THRESHOLD.value,BONUS.value,GOLD.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }

    @Override
    public boolean canSpawn() {
        return ( Settings.isEndless || (AbstractDungeon.floorNum <= 43) );
    }

    /*
    @Override
    public void setCounter(int setCounter){
        counter=setCounter;

        pulse=AbstractDungeon.player.currentHealth<=THRESHOLD.value;
        if(pulse){
            flash();
        }
    }

     */
}
