//本遗物的代码大篇幅取自Loadout Mod，感谢JasonW！
package strops.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_S;
import com.megacrit.cardcrawl.monsters.exordium.ApologySlime;
import com.megacrit.cardcrawl.monsters.exordium.SpikeSlime_S;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import strops.actions.MessyPuppyAction;
import strops.helpers.ModHelper;
import strops.modcore.Strops;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class MessyPuppy extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(MessyPuppy.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(MessyPuppy.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(MessyPuppy.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.BOSS;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public final ArrayList<AbstractMonster> records=new ArrayList<>();
    public static boolean isSmallerMonster=false;

    public static final int NUM1=6,NUM2=4,TIER=4;

    public static final IntSliderSetting PENALTY=new IntSliderSetting("MessyPuppy_Penalty_v0.12.0", "N1", NUM1, 1,15);
    public static final IntSliderSetting CAPACITY=new IntSliderSetting("MessyPuppy_Capacity_v0.12.0", "N2", NUM2, 1,10);
    public static final IntSliderSetting MH=new IntSliderSetting("MessyPuppy_MH_v0.12.0","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("MessyPuppy_G_v0.12.0","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("MessyPuppy_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(PENALTY);
        settings.add(CAPACITY);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public MessyPuppy() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
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
        counter=0;
        records.clear();
    }

    /*
    @Override
    public void atTurnStart(){
        for(AbstractMonster m:AbstractDungeon.getCurrRoom().monsters.monsters){
            if(m.id.equals(AwakenedOne.ID)&&m.hasPower(UnawakenedPower.POWER_ID)){
                addToBot(new CannotLoseAction());
                break;
            }
        }
    }

     */

    @Override
    public void onVictory(){
        counter=-1;
        grayscale=false;
    }

    @Override
    public void onTrigger(){
        //Strops.logger.info("###插入MessyPuppy动作");
        addToBot(new MessyPuppyAction(this));
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], PENALTY.value, CAPACITY.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], PENALTY.value, CAPACITY.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }

    public static void spawnMonster(Class<? extends AbstractMonster> monsterClass, int counter) {
        isSmallerMonster=true;
        AbstractMonster m = createMonster(monsterClass);
        isSmallerMonster=false;
        //Strops.logger.info(">>>复活成功："+m.name);
        m.currentHealth=PENALTY.value;
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(m,m,new MinionPower(m)));
        MonsterGroup mg = AbstractDungeon.getMonsters();
        m.drawX = (float)counter/(CAPACITY.value+1)*Settings.WIDTH;
        m.drawY = 0.65f*Settings.HEIGHT;
        if(m.hb_h>220f){
            m.hb_h=220f;
        }
        m.hb.move(m.drawX, m.drawY);
        m.init();
        m.applyPowers();
        m.useUniversalPreBattleAction();
        m.showHealthBar();
        m.createIntent();
        m.usePreBattleAction();
        for (AbstractRelic r : AbstractDungeon.player.relics)
            r.onSpawnMonster(m);

        int position = 0;
        for (AbstractMonster mo : mg.monsters) {
            if (m.drawX > mo.drawX)
                position++;
        }
        mg.addMonster(position, m);
    }

    public static AbstractMonster createMonster(Class<? extends AbstractMonster> amClass) {
        if (amClass.equals(AcidSlime_S.class))
            return new AcidSlime_S(0.0F, 0.0F, 0);
        if (amClass.equals(SpikeSlime_S.class))
            return new SpikeSlime_S(0.0F, 0.0F, 0);
        if (amClass.getName().equals("monsters.pet.ScapeGoatPet"))
            return new ApologySlime();
        Constructor[] arrayOfConstructor = amClass.getDeclaredConstructors();
        if (arrayOfConstructor.length > 0) {
            Constructor<?> c = arrayOfConstructor[0];
            try {
                int paramCt = c.getParameterCount();
                Class[] params = c.getParameterTypes();
                Object[] paramz = new Object[paramCt];
                for (int i = 0; i < paramCt; i++) {
                    Class<?> param = params[i];
                    if (int.class.isAssignableFrom(param)) {
                        paramz[i] = 1;
                    } else if (boolean.class.isAssignableFrom(param)) {
                        paramz[i] = true;
                    } else if (float.class.isAssignableFrom(param)) {
                        paramz[i] = 0.0F;
                    }
                }
                return (AbstractMonster)c.newInstance(paramz);
            } catch (Exception e) {
                Strops.logger.info("Error occurred while trying to instantiate class: " + c.getName());
                Strops.logger.info("Reverting to Apology Slime");
                return new ApologySlime();
            }
        }
        Strops.logger.info("Failed to create monster, returning Apology Slime");
        return new ApologySlime();
    }
}
