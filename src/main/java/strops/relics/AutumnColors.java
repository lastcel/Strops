package strops.relics;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class AutumnColors extends StropsAbstractRelic {
    public static final String ID = ModHelper.makePath(AutumnColors.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(AutumnColors.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(AutumnColors.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public static final int NUM1=1,NUM2=1,TIER=5;

    public boolean isWorking=false;

    public static final IntSliderSetting IN=new IntSliderSetting("AutumnColors_In_v0.16.11","N1",NUM1,1,5);
    public static final IntSliderSetting OUT=new IntSliderSetting("AutumnColors_Out_v0.16.11","N2",NUM2,1,5);
    public static final IntSliderSetting MH=new IntSliderSetting("AutumnColors_MH_v0.16.11","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("AutumnColors_G_v0.16.11","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("AutumnColors_R_v0.16.11","R", TIER,0,5);
    public static final IntSliderSetting P=new IntSliderSetting("AutumnColors_P","P", 150,50,300);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(IN);
        settings.add(OUT);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        settings.add(P);
        return settings;
    }

    public AutumnColors() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        canCopy=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        counter=0;
    }

    @Override
    public void justEnteredRoom(AbstractRoom room){
        if(!(room instanceof RestRoom)){
            return;
        }
        if(AbstractDungeon.actNum==2){
            flash();
            counter+=IN.value;
        } else if(AbstractDungeon.actNum==3&&counter>=OUT.value){
            flash();
            AbstractDungeon.getCurrRoom().rewards.clear();
            AbstractRelic.RelicTier tier = AbstractDungeon.returnRandomRelicTier();
            AbstractDungeon.getCurrRoom().addNoncampRelicToRewards(tier);
            counter-=OUT.value;
            if(hasTriColor()&&counter>=OUT.value){
                tier = AbstractDungeon.returnRandomRelicTier();
                AbstractDungeon.getCurrRoom().addNoncampRelicToRewards(tier);
                counter-=OUT.value;
            }
            //Strops.logger.info("初始房间阶段="+AbstractDungeon.getCurrRoom().phase);
            AbstractDungeon.combatRewardScreen.open(CardCrawlGame.languagePack.getRelicStrings(AutumnColors.ID).DESCRIPTIONS[5]);
            isWorking=true;
        }
    }

    @Override
    public void update(){
        super.update();
        if(!isObtained){
            return;
        }
        if(isWorking && AbstractDungeon.screen==AbstractDungeon.CurrentScreen.COMBAT_REWARD &&
                AbstractDungeon.combatRewardScreen.hasTakenAll){
            AbstractDungeon.closeCurrentScreen();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            isWorking=false;
        }
        if(isWorking && AbstractDungeon.screen==AbstractDungeon.CurrentScreen.NONE){
            //Strops.logger.info("已取消");
            isWorking=false;
        }
    }

    /*
    @Override
    public void onRightClick(){
        //Strops.logger.info("房间阶段="+AbstractDungeon.getCurrRoom().phase);
        if(isWorking && AbstractDungeon.screen==AbstractDungeon.CurrentScreen.COMBAT_REWARD){
            AbstractDungeon.closeCurrentScreen();
            isWorking=false;
        }
    }

     */

    /*
    @Override
    public void atBattleStart(){
        if(AbstractDungeon.actNum==3&&counter>=OUT.value){
            flash();
            AbstractRelic.RelicTier tier = AbstractDungeon.returnRandomRelicTier();
            AbstractDungeon.getCurrRoom().addRelicToRewards(tier);
            counter-=OUT.value;
        }
    }

     */

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0],IN.value,OUT.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0],IN.value,OUT.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }

    @Override
    public boolean canSpawn(){
        return !Settings.isEndless && AbstractDungeon.actNum==1;
    }

    @Override
    public int getPrice(){
        return P.value;
    }
}
