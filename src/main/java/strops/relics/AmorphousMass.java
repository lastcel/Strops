package strops.relics;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class AmorphousMass extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(AmorphousMass.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(AmorphousMass.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(AmorphousMass.class.getSimpleName());
    private static final LandingSound LANDING_SOUND = LandingSound.HEAVY;

    public static final int NUM1=3,TIER=1;

    public static final IntSliderSetting BONUS=new IntSliderSetting("AmorphousMass_Bonus", "N1", NUM1, 1,10);
    public static final IntSliderSetting MH=new IntSliderSetting("AmorphousMass_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("AmorphousMass_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("AmorphousMass_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(BONUS);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public AmorphousMass() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], BONUS.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], BONUS.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }

    @Override
    public void onTrigger(){
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

        for(AbstractMonster m:AbstractDungeon.getCurrRoom().monsters.monsters){
            if(!m.isDeadOrEscaped()&&m.currentHealth%2==0){
                addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, BONUS.value));
            }
        }
    }
}
