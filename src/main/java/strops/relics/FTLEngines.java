package strops.relics;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class FTLEngines extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(FTLEngines.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(FTLEngines.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(FTLEngines.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.BOSS;
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    private static boolean isNotBoss = true;
    private float prevX,prevY;
    public boolean isScaleUp=false;

    public static final int NUM1=6,TIER=4;

    public static final IntSliderSetting THRESHOLD=new IntSliderSetting("FTL_Threshold", "N1", NUM1, 3,12);
    public static final IntSliderSetting MH=new IntSliderSetting("FTL_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("FTL_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("FTL_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(THRESHOLD);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public FTLEngines() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        canCopy=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        AbstractDungeon.player.energy.energyMaster++;
    }

    @Override
    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster--;
    }

    @Override
    public void atBattleStart(){
        isNotBoss=true;
        for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
            if (m.type == AbstractMonster.EnemyType.BOSS){
                isNotBoss = false;
                break;
            }
        }
        if(isNotBoss){
            counter=0;
        }
    }

    @Override
    public void atTurnStart(){
        if(isNotBoss){
            counter++;
            if(counter==THRESHOLD.value){
                jump(true);
                beginLongPulse();
            } else if(counter>THRESHOLD.value){
                jump(false);
                stopPulse();
            }
        }
    }

    @Override
    public void onVictory(){
        counter=-1;
        if(pulse){
            jump(false);
            stopPulse();
        }
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], THRESHOLD.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], THRESHOLD.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }

    private void jump(boolean isOntoPlayer){
        AbstractPlayer p=AbstractDungeon.player;
        if(isOntoPlayer){
            prevX=currentX;
            prevY=currentY;
            isScaleUp=true;
            currentX=p.hb.cX - p.animX;
            currentY=p.hb.cY + p.hb.height * 0.8f - p.animY;
            scale*=2.0f;
        } else {
            currentX=prevX;
            currentY=prevY;
            isScaleUp=false;
        }
        hb.move(currentX,currentY);
    }
}
