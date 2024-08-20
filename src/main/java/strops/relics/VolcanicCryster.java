package strops.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.GremlinWarrior;
import com.megacrit.cardcrawl.powers.StrengthPower;
import strops.actions.UsePreBattleActionAction;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class VolcanicCryster extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(VolcanicCryster.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(VolcanicCryster.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(VolcanicCryster.class.getSimpleName());
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    public static final int NUM1=29,NUM2=3,TIER=4;

    public static final IntSliderSetting PENALTY=new IntSliderSetting("VolcanicCryster_Penalty", "N1", NUM1, 11,80);
    public static final IntSliderSetting COMPENSATE=new IntSliderSetting("VolcanicCryster_Compensate", "N2", NUM2, -5,7);
    public static final IntSliderSetting MH=new IntSliderSetting("VolcanicCryster_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("VolcanicCryster_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("VolcanicCryster_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(PENALTY);
        settings.add(COMPENSATE);
        settings.add(MH);
        settings.add(G);
        return settings;
    }

    public VolcanicCryster() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
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
    public String getUpdatedDescription() {
        if(COMPENSATE.value>=0){
            return String.format(this.DESCRIPTIONS[0], PENALTY.value, COMPENSATE.value);
        }
        return String.format(this.DESCRIPTIONS[5], PENALTY.value, -COMPENSATE.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        if(COMPENSATE.value>=0){
            str_out.add(String.format(this.DESCRIPTIONS[0], PENALTY.value, COMPENSATE.value));
        } else {
            str_out.add(String.format(this.DESCRIPTIONS[5], PENALTY.value, -COMPENSATE.value));
        }
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }

    @Override
    public void atPreBattle() {
        if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                ((Decanter)AbstractDungeon.player.getRelic(Decanter.ID))
                        .relicToDisenchant.equals(VolcanicCryster.ID)){
            AbstractDungeon.player.getRelic(Decanter.ID).flash();
        } else {
            flash();

            MessyPuppy.isSmallerMonster=true;
            AbstractMonster m=new GremlinWarrior(-700.0F, 150.0F);
            MessyPuppy.isSmallerMonster=false;
            m.hb_h=170.0f;

            m.currentHealth=m.maxHealth=PENALTY.value;
            addToBot(new SpawnMonsterAction(m, true));
            addToBot(new UsePreBattleActionAction(m));
            addToBot(new RelicAboveCreatureAction(m, this));
            if(COMPENSATE.value!=0){
                addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new StrengthPower(m, -COMPENSATE.value), -COMPENSATE.value));
            }
        }
    }
}
