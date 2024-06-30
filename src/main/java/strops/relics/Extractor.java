package strops.relics;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.MinionPower;
import strops.actions.ExtractorAction;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.MonsterGoldInfo;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class Extractor extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(Extractor.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(Extractor.class.getSimpleName());
    //private static final String IMG_PATH_O = ModHelper.makeOPath(FTLEngines.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.COMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    private final ArrayList<MonsterGoldInfo> accounts=new ArrayList<>();

    public static final int NUM1=2,NUM2=5;

    public static final IntSliderSetting BONUS=new IntSliderSetting("Extractor_BONUS", "N1", NUM1, 1,10);
    public static final IntSliderSetting THRESHOLD=new IntSliderSetting("Extractor_THRESHOLD", "N2", NUM2, 1,10);
    public static final IntSliderSetting MH=new IntSliderSetting("Extractor_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("Extractor_G","G",0,-100,100);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(BONUS);
        settings.add(THRESHOLD);
        settings.add(MH);
        settings.add(G);
        return settings;
    }

    public Extractor() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
    }

    @Override
    public void atBattleStart(){
        accounts.clear();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount){
        if (damageAmount==0&&
                info.type != DamageInfo.DamageType.THORNS &&
                info.type != DamageInfo.DamageType.HP_LOSS &&
                info.owner != null && info.owner != AbstractDungeon.player &&
                !info.owner.hasPower(MinionPower.POWER_ID)){
            boolean isOnAccounts=false;
            MonsterGoldInfo monsterGoldInfo=null;
            for(MonsterGoldInfo i:accounts){
                if(i.extractedMonster==info.owner){
                    isOnAccounts=true;
                    i.extractedTimes++;
                    monsterGoldInfo=i;
                    break;
                }
            }
            if(!isOnAccounts){
                monsterGoldInfo=new MonsterGoldInfo(info.owner, 1);
                accounts.add(monsterGoldInfo);
            }

            if(monsterGoldInfo.extractedTimes<=THRESHOLD.value){
                addToTop(new ExtractorAction(monsterGoldInfo.extractedMonster,BONUS.value));
            }
        }

        return damageAmount;
    }

    @Override
    public boolean canSpawn(){
        return Settings.isEndless||AbstractDungeon.actNum<=3;
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], BONUS.value, THRESHOLD.value);
    }


    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], BONUS.value, THRESHOLD.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }
}
