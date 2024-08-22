//patch写在了发光羽毛的patch里面

package strops.relics;

import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class CirculatingLightAndColorShifter extends StropsAbstractRelic implements CustomSavable<ArrayList<CirculatingLightAndColorShifter.Angle>> {
    public static final String ID = ModHelper.makePath(CirculatingLightAndColorShifter.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(CirculatingLightAndColorShifter.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(CirculatingLightAndColorShifter.class.getSimpleName());
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;
    private static final Texture hitAreaImg = ImageMaster.loadImage("StropsResources/img/misc/BBBHitArea.png");

    public ArrayList<Angle> angles=new ArrayList<>();

    public enum Angle{
        CARD,RELIC,POTION,NULL
    }

    public static float xSc = Settings.xScale;
    public static float ySc = Settings.yScale;
    public static int wid=Settings.WIDTH;
    //public static int hei=Settings.HEIGHT;

    public static final int NUM1=2,NUM2=20,NUM3=0,NUM4=0,NUM5=0,TIER=2;

    public static final IntSliderSetting SHOP=new IntSliderSetting("CLACS_Shop","N1", NUM1,1,5);
    public static final IntSliderSetting TEMP_HP=new IntSliderSetting("CLACS_Temp_Hp","N2", NUM2,8,40);
    public static final IntSliderSetting ACT1=new IntSliderSetting("CLACS_Act1","A1", NUM3,3);
    public static final IntSliderSetting ACT2=new IntSliderSetting("CLACS_Act2","A2", NUM4,3);
    public static final IntSliderSetting ACT3=new IntSliderSetting("CLACS_Act3","A3", NUM5,3);
    public static final IntSliderSetting MH=new IntSliderSetting("CLACS_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("CLACS_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("CLACS_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(SHOP);
        settings.add(TEMP_HP);
        settings.add(ACT1);
        settings.add(ACT2);
        settings.add(ACT3);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public CirculatingLightAndColorShifter() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));

        ArrayList<Angle> tempAngles=new ArrayList<>();
        tempAngles.add(Angle.CARD);
        tempAngles.add(Angle.RELIC);
        tempAngles.add(Angle.POTION);

        if(AbstractDungeon.player==null||AbstractDungeon.miscRng==null){
            if(ACT1.value==0){
                angles.add(Angle.NULL);
            } else {
                angles.add(tempAngles.get(ACT1.value-1));
            }
            if(ACT2.value==0){
                angles.add(Angle.NULL);
            } else {
                angles.add(tempAngles.get(ACT2.value-1));
            }
            if(ACT3.value==0){
                angles.add(Angle.NULL);
            } else {
                angles.add(tempAngles.get(ACT3.value-1));
            }
        } else {
            int rng;
            if(ACT1.value==0){
                rng=AbstractDungeon.miscRng.random(0,2);
                angles.add(tempAngles.get(rng));
            } else {
                angles.add(tempAngles.get(ACT1.value-1));
            }
            if(ACT2.value==0){
                rng=AbstractDungeon.miscRng.random(0,2);
                angles.add(tempAngles.get(rng));
            } else {
                angles.add(tempAngles.get(ACT2.value-1));
            }
            if(ACT3.value==0){
                rng=AbstractDungeon.miscRng.random(0,2);
                angles.add(tempAngles.get(rng));
            } else {
                angles.add(tempAngles.get(ACT3.value-1));
            }
        }

        updateDesc();
    }

    @Override
    public ArrayList<Angle> onSave(){
        return angles;
    }

    @Override
    public void onLoad(ArrayList<Angle> savedAngles){
        angles=savedAngles;
        updateDesc();
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        counter=0;
    }

    @Override
    public void onTrigger(){
        counter=SHOP.value;
        flash();
        beginLongPulse();
    }

    @Override
    public void atPreBattle(){
        if(counter<=0){
            return;
        }

        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, TEMP_HP.value));
        counter--;
        if(counter==0){
            stopPulse();
        }
    }

    @Override
    public String getUpdatedDescription() {
        if(angles==null){
            if(DESCRIPTIONS[9].equals("1")){
                return String.format(this.DESCRIPTIONS[0],TEMP_HP.value,SHOP.value,
                        getColor(Angle.NULL),getAngleString(Angle.NULL),
                        getColor(Angle.NULL),getAngleString(Angle.NULL),
                        getColor(Angle.NULL),getAngleString(Angle.NULL));
            }
            return String.format(this.DESCRIPTIONS[0],SHOP.value,TEMP_HP.value,
                    getColor(Angle.NULL),getAngleString(Angle.NULL),
                    getColor(Angle.NULL),getAngleString(Angle.NULL),
                    getColor(Angle.NULL),getAngleString(Angle.NULL));
        }

        if(DESCRIPTIONS[9].equals("1")){
            return String.format(this.DESCRIPTIONS[0],TEMP_HP.value,SHOP.value,
                    getColor(angles.get(0)),getAngleString(angles.get(0)),
                    getColor(angles.get(1)),getAngleString(angles.get(1)),
                    getColor(angles.get(2)),getAngleString(angles.get(2)));
        }
        return String.format(this.DESCRIPTIONS[0],SHOP.value,TEMP_HP.value,
                getColor(angles.get(0)),getAngleString(angles.get(0)),
                getColor(angles.get(1)),getAngleString(angles.get(1)),
                getColor(angles.get(2)),getAngleString(angles.get(2)));
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();

        if(angles==null){
            if(DESCRIPTIONS[9].equals("1")){
                str_out.add(String.format(this.DESCRIPTIONS[0],TEMP_HP.value,SHOP.value,
                        getColor(Angle.NULL),getAngleString(Angle.NULL),
                        getColor(Angle.NULL),getAngleString(Angle.NULL),
                        getColor(Angle.NULL),getAngleString(Angle.NULL)));
            } else {
                str_out.add(String.format(this.DESCRIPTIONS[0],SHOP.value,TEMP_HP.value,
                        getColor(Angle.NULL),getAngleString(Angle.NULL),
                        getColor(Angle.NULL),getAngleString(Angle.NULL),
                        getColor(Angle.NULL),getAngleString(Angle.NULL)));
            }
        } else {
            if(DESCRIPTIONS[9].equals("1")){
                str_out.add(String.format(this.DESCRIPTIONS[0],TEMP_HP.value,SHOP.value,
                        getColor(angles.get(0)),getAngleString(angles.get(0)),
                        getColor(angles.get(1)),getAngleString(angles.get(1)),
                        getColor(angles.get(2)),getAngleString(angles.get(2))));
            } else {
                str_out.add(String.format(this.DESCRIPTIONS[0],SHOP.value,TEMP_HP.value,
                        getColor(angles.get(0)),getAngleString(angles.get(0)),
                        getColor(angles.get(1)),getAngleString(angles.get(1)),
                        getColor(angles.get(2)),getAngleString(angles.get(2))));
            }
        }
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }

    @Override
    public boolean canSpawn() {
        return (Settings.isEndless || AbstractDungeon.actNum <= 3);
    }

    public String getAngleString(Angle angle){
        return DESCRIPTIONS[5+angle.ordinal()];
    }

    public static String getColor(Angle angle){
        switch (angle){
            case CARD:return "#r";
            case RELIC:return "#g";
            case POTION:return "#b";
            case NULL:return "#y";
            default:return "";
        }
    }

    @Override
    public void renderAndCheck(SpriteBatch sb){
        if(AbstractDungeon.screen!=AbstractDungeon.CurrentScreen.SHOP){
            return;
        }

        if(hasTriColor()){
            renderAngleOfCard(sb);
            renderAngleOfRelic(sb);
            renderAngleOfPotion(sb);
            return;
        }

        if(AbstractDungeon.actNum!=1&&AbstractDungeon.actNum!=2&&AbstractDungeon.actNum!=3){
            return;
        }

        switch(angles.get(AbstractDungeon.actNum-1)){
            case CARD:renderAngleOfCard(sb);break;
            case RELIC:renderAngleOfRelic(sb);break;
            case POTION:renderAngleOfPotion(sb);break;
            default:break;
        }
    }

    public void renderAngleOfCard(SpriteBatch sb){
        int tmp = (int)(wid - wid*0.12F * 2.0F - AbstractCard.IMG_WIDTH_S * 5.0F) / 4;
        float padX = (int)(tmp + AbstractCard.IMG_WIDTH_S) + 10.0F * Settings.scale;

        Color col=Settings.RED_TEXT_COLOR.cpy();
        col.a=0.3f;
        sb.setColor(col);
        sb.draw(hitAreaImg, wid*0.12F, 540.0F*ySc, padX*4.8F, AbstractCard.IMG_HEIGHT_S*1.45F,  0, 0, hitAreaImg.getWidth(), hitAreaImg.getHeight(), false, false);
        sb.draw(hitAreaImg, wid*0.12F, 100.0F*ySc, padX*2, AbstractCard.IMG_HEIGHT_S*1.45F,  0, 0, hitAreaImg.getWidth(), hitAreaImg.getHeight(), false, false);
    }

    public void renderAngleOfRelic(SpriteBatch sb){
        Color col=Settings.GREEN_TEXT_COLOR.cpy();
        col.a=0.3f;
        sb.setColor(col);
        sb.draw(hitAreaImg, 950.0F * xSc, 270.0F *ySc, 420.0F*xSc, 200.0F*ySc,  0, 0, hitAreaImg.getWidth(), hitAreaImg.getHeight(), false, false);
    }

    public void renderAngleOfPotion(SpriteBatch sb){
        Color col=Settings.BLUE_TEXT_COLOR.cpy();
        col.a=0.3f;
        sb.setColor(col);
        sb.draw(hitAreaImg, 950.0F * xSc,50.0F *ySc, 420.0F*xSc, 200.0F*ySc,  0, 0, hitAreaImg.getWidth(), hitAreaImg.getHeight(), false, false);
    }

    @Override
    public void setCounter(int setCounter){
        this.counter=setCounter;
        if (setCounter > 0) {
            beginLongPulse();
        }
    }
}
