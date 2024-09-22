//待研究&支持互动项：捣蛋的小狗，出口相位，荆棘，倏忽魔等

package strops.relics;

import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.Abacus;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import strops.actions.EndBattleAction;
import strops.helpers.ModHelper;
import strops.modcore.Strops;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class NoNameForNow5 extends StropsAbstractRelic implements ClickableRelic, CustomSavable<Boolean> {
    public static final String ID = ModHelper.makePath(NoNameForNow5.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(NoNameForNow5.class.getSimpleName());
    //private static final String IMG_PATH_O = ModHelper.makeOPath(NoNameForNow5.class.getSimpleName());
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    public boolean isRenewed =false;
    public static int xOffset,yOffset,charTarget;

    public boolean activated=false,finished=false/*,everActivated=false*/;

    public static final int NUM1=4,NUM2=5,NUM3=6,NUM4=1,TIER=2;

    public static final IntSliderSetting LOWER=new IntSliderSetting("NNFN5_Lower_v0.16.1","N1", NUM1,1,10);
    public static final IntSliderSetting MIDDLE=new IntSliderSetting("NNFN5_Middle","N2", NUM2,1,10);
    public static final IntSliderSetting UPPER=new IntSliderSetting("NNFN5_Upper_v0.16.1","N3", NUM3,2,11);
    public static final IntSliderSetting MAX_SHUFFLE=new IntSliderSetting("NNFN5_Max_Shuffle_v0.16.1","N4", NUM4,3);
    public static final IntSliderSetting MH=new IntSliderSetting("NNFN5_MH_v0.16.1","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("NNFN5_G_v0.16.1","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("NNFN5_R_v0.16.1","R", TIER,0,5);
    public static final IntSliderSetting L=new IntSliderSetting("NNFN5_Location","L", 1,1,15);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(LOWER);
        settings.add(MIDDLE);
        settings.add(UPPER);
        settings.add(MAX_SHUFFLE);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        settings.add(L);
        return settings;
    }

    public NoNameForNow5() {
        super(ID, ImageMaster.loadImage(IMG_PATH), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        canCopy=false;
    }

    @Override
    public Boolean onSave(){
        return isRenewed;
    }

    @Override
    public void onLoad(Boolean savedIsRenamed){
        isRenewed =savedIsRenamed;
        if(isRenewed){
            renew();
        }
    }

    @Override
    public void onRightClick(){
        if(!isRenewed){
            renew();
        } else {
            reOld();
        }
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
    }

    @Override
    public void atPreBattle(){
        counter=0;
        secondCounter=0;
    }

    @Override
    public void atTurnStart(){
        counter++;

        if(counter>=LOWER.value&&counter<=MIDDLE.value||counter==UPPER.value&&activated){
            beginLongPulse();
        } else {
            stopPulse();
        }

        /*
        if(counter>=LOWER.value&&counter<=UPPER.value&&!grayscale){
            //flash();
            beginLongPulse();
        }

         */
    }

    @Override
    public void onPlayerEndTurn(){
        if(activated&&counter==UPPER.value){
            //AbstractDungeon.getCurrRoom().endBattle();
            //activated=false;
            finished=true;
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player,this));
            addToBot(new EndBattleAction());
        }
    }

    @Override
    public void onShuffle(){
        secondCounter++;

        if(secondCounter>MAX_SHUFFLE.value){
            grayscale=true;
        }
    }

    @Override
    public void onVictory(){
        if(finished&&!grayscale){
            flash();
            addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.getCurrRoom().addRelicToRewards(RelicLibrary.getRelic(Abacus.ID).makeCopy());
        }

        counter=-1;
        secondCounter=-1;
        activated=false;
        finished=false;
        grayscale=false;
        stopPulse();
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0],LOWER.value,MIDDLE.value,UPPER.value,MAX_SHUFFLE.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0],LOWER.value,MIDDLE.value,UPPER.value,MAX_SHUFFLE.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }

    @Override
    public boolean canSpawn() {
        return ( Settings.isEndless || (AbstractDungeon.actNum <= 2) );
    }

    private void renew(){
        try {
            Field f=AbstractRelic.class.getDeclaredField("name");
            f.setAccessible(true);
            f.set(this,CardCrawlGame.playerName);
        } catch (IllegalAccessException|NoSuchFieldException e) {
            Strops.logger.info("Failed to modify name for NNFN5!");
        }

        img=ImageMaster.loadImage(reflectImage());
        outlineImg=img;

        flavorText=CardCrawlGame.playerName+DESCRIPTIONS[5];
        updateDesc();

        isRenewed=true;
    }

    private String reflectImage(){
        int width = 128;
        int height = 128;
        String fullName="sendToDevs/Strops/" + CardCrawlGame.playerName + ".png";

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        Color bgColor = new Color(255, 255, 255, 0);
        Color fgColor = new Color(247, 207, 153);
        g2d.setColor(bgColor);
        g2d.fillRect(0, 0, width, height);

        g2d.setColor(fgColor);
        int fontSize = 66;
        g2d.setFont(new java.awt.Font(null, java.awt.Font.BOLD, fontSize));
        decideTargetAndOffset();
        g2d.drawString(CardCrawlGame.playerName.substring(charTarget-1,charTarget), (width - fontSize) / 2+xOffset, (height + fontSize) / 2+yOffset);

        g2d.dispose();

        File folder=new File("sendToDevs/Strops");
        if(!folder.mkdir()){
            Strops.logger.info("NNFN5 reporting, failed to create reflectImage directory!");
        }
        try {
            ImageIO.write(image, "PNG", new File(fullName));
        } catch (IOException e) {
            Strops.logger.info("NNFN5 reporting, failed to write reflectImage!");
        }

        return fullName;
    }

    private void reOld(){
        try {
            Field f=AbstractRelic.class.getDeclaredField("name");
            f.setAccessible(true);
            f.set(this,CardCrawlGame.languagePack.getRelicStrings(relicId).NAME);
        } catch (IllegalAccessException|NoSuchFieldException e) {
            Strops.logger.info("Failed to restore name for NNFN5!");
        }

        img=ImageMaster.loadImage(IMG_PATH);
        outlineImg=img;

        flavorText=CardCrawlGame.languagePack.getRelicStrings(relicId).FLAVOR;
        updateDesc();

        isRenewed=false;
    }

    private static void decideTargetAndOffset(){
        if(L.value<=CardCrawlGame.playerName.length()){
            charTarget=L.value;
        } else {
            charTarget=1;
        }

        if(CardCrawlGame.playerName.substring(charTarget-1,charTarget).matches("[a-zA-Z0-9]+")){
            xOffset=15;
            yOffset=-10;
            return;
        }

        xOffset=0;
        yOffset=-10;
    }
}
