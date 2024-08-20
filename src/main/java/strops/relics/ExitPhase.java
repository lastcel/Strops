package strops.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BackAttackPower;
import strops.actions.EscapeAction;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;
import strops.utilities.StaticHelpers;

import java.util.ArrayList;

public class ExitPhase extends StropsAbstractRelic implements ClickableRelic {
    public static final String ID = ModHelper.makePath(ExitPhase.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(ExitPhase.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(ExitPhase.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public static final int NUM1=2,NUM2=50,TIER=2;

    public static final IntSliderSetting COOLDOWN=new IntSliderSetting("ExitPhase_Cooldown_v0.12.4", "N1", NUM1, 1,5);
    public static final IntSliderSetting BONUS=new IntSliderSetting("ExitPhase_Bonus_v0.12.4", "N2", NUM2, 100);
    public static final IntSliderSetting MH=new IntSliderSetting("ExitPhase_MH_v0.12.4","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("ExitPhase_G_v0.12.4","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("ExitPhase_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(COOLDOWN);
        settings.add(BONUS);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public ExitPhase() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        counter=0;
        beginLongPulse();
    }

    @Override
    public void atBattleStart(){
        if(counter!=0){
            counter--;
        }
        if(counter==0){
            beginLongPulse();
            flash();
        }

        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
            if (m.type == AbstractMonster.EnemyType.BOSS||m.hasPower(BackAttackPower.POWER_ID)){
                stopPulse();
            }
        }
    }

    @Override
    public void onVictory(){
        if(counter==1){
            beginLongPulse();
        }
    }

    @Override
    public void setCounter(int setCounter){
        counter=setCounter;
        if (counter == 1||counter == 0) {
            flash();
            beginLongPulse();
        }
    }

    @Override
    public void onRightClick() {
        if (!StaticHelpers.canClickRelic(this)) {
            return;
        }

        if (counter != 0) {
            return;
        }

        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
            if (m.type == AbstractMonster.EnemyType.BOSS||m.hasPower(BackAttackPower.POWER_ID)){
                return;
            }
        }

        addToBot(new EscapeAction());
        counter=COOLDOWN.value;
        stopPulse();
    }

    @Override
    public boolean canSpawn(){
        return Settings.isEndless||AbstractDungeon.floorNum<=48;
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], COOLDOWN.value, BONUS.value);
    }


    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], COOLDOWN.value, BONUS.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }
}
