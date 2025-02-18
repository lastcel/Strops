package strops.relics;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.AsyncSaver;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFileObfuscator;
import strops.actions.NeuronSightAction;
import strops.helpers.ModHelper;
import strops.modcore.Strops;
import strops.patch.PatchNeuronSight;
import strops.utilities.IntSliderSetting;
import strops.utilities.NeuronSightScreen;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class NeuronSight extends StropsAbstractRelic {
    public static final String ID = ModHelper.makePath(NeuronSight.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(NeuronSight.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(NeuronSight.class.getSimpleName());
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public static final int NUM1=20,TIER=2;
    public boolean isReady=false/*,isListening=false*/;
    public NeuronSightScreen neuronSightScreen;
    public static final String SAVE_NAME="NeuronSightCounter";
    private static final String KEY="seed-work";

    public static final IntSliderSetting BONUS=new IntSliderSetting("NeuronSight_Bonus","N1", NUM1,1,50);
    public static final IntSliderSetting MH=new IntSliderSetting("NeuronSight_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("NeuronSight_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("NeuronSight_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(BONUS);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public NeuronSight() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        canCopy=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);

        saveCounter();
    }

    @Override
    public void atBattleStart(){
        secondCounter=0;
        color=Color.TEAL;

        if(counter==-1){
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player,this));
            addToBot(new NeuronSightAction(this));
        }
    }

    @Override
    public void atTurnStart() {
        secondCounter++;
        if(secondCounter==1){
            return;
        }
        if(secondCounter==counter){
            color=Color.LIME;
            flash();
            beginLongPulse();
        } else {
            if(secondCounter<counter){
                color=Color.TEAL;
            } else {
                color=Color.SCARLET;
            }
            stopPulse();
        }
    }

    @Override
    public void update(){
        super.update();
        if(isReady){
            /*
            for(AbstractMonster m:AbstractDungeon.getMonsters().monsters){
                if(m.intent==AbstractMonster.Intent.DEBUG){
                    return;
                }
            }

             */

            /*
            for(AbstractMonster m:AbstractDungeon.getMonsters().monsters){
                Strops.logger.info("怪物"+m.name+"的意图为"+m.intent.name());
            }

             */

            isReady=false;
            //isListening=true;
            //neuronSightScreen.open(this);
            BaseMod.openCustomScreen(PatchNeuronSight.PatchTool1.NS_FORETELL,this);
        }

        /*
        if(isListening){
            neuronSightScreen.update();
        }

         */
    }

    /*
    @Override
    public void renderAndCheck(SpriteBatch sb){
        if(isListening){
            neuronSightScreen.render(sb);
        }
    }

     */

    @Override
    public void setCounter(int setCounter){
        FileHandle file;
        String data;

        try {
            file=Gdx.files.local(getCounterSavePath());
            data=file.readString();
        } catch(GdxRuntimeException e) {
            Strops.logger.info("Neuron Sight's counter save doesn't exist!");
            counter=20;
            return;
        }

        try {
            counter=Integer.parseInt(SaveFileObfuscator.decode(data, KEY));
        } catch (NumberFormatException e) {
            Strops.logger.info("Failed to parse Neuron Sight's counter save!");
            counter=20;
        }
    }

    @Override
    public void onVictory(){
        if(pulse){
            AbstractDungeon.player.gainGold(BONUS.value);
            stopPulse();
        }

        counter=-1;
        saveCounter();
        secondCounter=-1;
    }

    @Override
    public void justEnteredRoom(AbstractRoom room){
        if(!(room instanceof MonsterRoom)){
            counter=-1;
            saveCounter();
        }
    }

    @Override
    public boolean canSpawn(){
        return (Settings.isEndless||AbstractDungeon.actNum<=2)&&!(AbstractDungeon.getCurrRoom() instanceof ShopRoom);
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0],BONUS.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0],BONUS.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }

    public static String getSavePath(String name){
        StringBuilder sb = new StringBuilder();
        sb.append("sendToDevs/Strops/");
        if (CardCrawlGame.saveSlot != 0){
            sb.append(CardCrawlGame.saveSlot).append("_");
        }
        sb.append(name).append(".autosave");
        return sb.toString();
    }

    public static void saveToDisk(String name, int data, String key){
        String filepath=getSavePath(name);
        AsyncSaver.save(filepath, SaveFileObfuscator.encode(Integer.toString(data),key));
    }

    public void saveCounter(){
        saveToDisk(SAVE_NAME,counter,KEY);
    }

    public static String getCounterSavePath(){
        return getSavePath(SAVE_NAME);
    }
}
