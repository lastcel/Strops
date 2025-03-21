package strops.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Sozu;
import strops.helpers.ModHelper;
import strops.modcore.Strops;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class Echo extends StropsAbstractRelic implements ClickableRelic {
    public static final String ID = ModHelper.makePath(Echo.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(Echo.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(Echo.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.RARE;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public static final int NUM1=3,TIER=3;

    public static final IntSliderSetting USABLE=new IntSliderSetting("Echo_Usable", "N1", NUM1, 1,10);
    public static final IntSliderSetting MH=new IntSliderSetting("Echo_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("Echo_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("Echo_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(USABLE);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public Echo() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        counter=USABLE.value;
        beginLongPulse();
        if(!Strops.lastPotion.equals("")){
            setDescriptionAfterObtainingPotion();
        }
    }

    @Override
    public void onRightClick() {
        //Strops.logger.info("当前房间阶段="+AbstractDungeon.getCurrRoom().phase);
        /*
        for(String s:AbstractDungeon.uncommonRelicPool){
            Strops.logger.info(s);
        }

         */
        //Strops.logger.info("当前屏幕="+AbstractDungeon.screen);

        if (counter <= 0) {
            return;
        }

        if(Strops.lastPotion.equals("")){
            return;
        }

        if (AbstractDungeon.player.hasRelic(Sozu.ID)) {
            AbstractRelic r2;
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                            .relicToDisenchant.equals(Sozu.ID)){
                r2.flash();
                ((Decanter) r2).decay();
            } else {
                AbstractDungeon.player.getRelic(Sozu.ID).flash();
                return;
            }
        }

        if (AbstractDungeon.player.obtainPotion(PotionHelper.getPotion(Strops.lastPotion))){
            counter--;
            if (counter == 0) {
                counter = -2;
                pulse = false;
                usedUp();
            }
        }
    }

    @Override
    public void setCounter(int setCounter){
        this.counter=setCounter;
        if (setCounter == -2) {
            usedUp();
            this.counter = -2;
        } else {
            beginLongPulse();
            flash();
        }
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(DESCRIPTIONS[0],USABLE.value);
        /*
        if(Strops.lastPotion.equals("")){
            return String.format(DESCRIPTIONS[0],USABLE.value);
        }
        return String.format(DESCRIPTIONS[0]+" NL "+DESCRIPTIONS[5], USABLE.value, PotionHelper.getPotion(Strops.lastPotion).name);

         */
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(DESCRIPTIONS[0],USABLE.value));
        /*
        if(Strops.lastPotion.equals("")){
            str_out.add(String.format(DESCRIPTIONS[0],USABLE.value));
        } else {
            str_out.add(String.format(DESCRIPTIONS[0]+" NL "+DESCRIPTIONS[5], USABLE.value, PotionHelper.getPotion(Strops.lastPotion).name));
        }

         */
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }

    //Obtain应为Use
    public void setDescriptionAfterObtainingPotion() {
        description=String.format(DESCRIPTIONS[0]+" NL "+DESCRIPTIONS[5], USABLE.value, PotionHelper.getPotion(Strops.lastPotion).name);
        tips.clear();
        tips.add(new PowerTip(name, description));
        showMHaG(MH,G);
        tips.add(new PowerTip(DESCRIPTIONS[1], DESCRIPTIONS[2]));
        initializeTips();
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.floorNum>=1;
    }
}
