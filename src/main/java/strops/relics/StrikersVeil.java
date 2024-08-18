//patch写在了虹色之环的patch里面
package strops.relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class StrikersVeil extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(StrikersVeil.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(StrikersVeil.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(StrikersVeil.class.getSimpleName());
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public AbstractCard card=null;
    public float timer;
    public static float DURATION=1.0f;
    public boolean inTurn;

    public static final int NUM1=2,TIER=1;

    public static final IntSliderSetting THRESHOLD=new IntSliderSetting("StrikersVeil_Threshold","N1", NUM1,3);
    public static final IntSliderSetting MH=new IntSliderSetting("StrikersVeil_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("StrikersVeil_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("StrikersVeil_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(THRESHOLD);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public StrikersVeil() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        canCopy=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
    }

    @Override
    public void atTurnStart(){
        timer=0.0f;
        inTurn=true;
        /*
        if(card!=null&&AbstractDungeon.player.drawPile.group.contains(card)){
            card.shrink();
            (AbstractDungeon.getCurrRoom()).souls.onToDeck(card, false,true);
            card=null;
        }

         */
    }

    @Override
    public void update(){
        super.update();
        if(!isObtained){
            return;
        }

        if(/*card!=null&&*/inTurn){
            timer+=Gdx.graphics.getDeltaTime();
            if(timer>=DURATION){
                card=null;
                inTurn=false;
            }
        }
    }

    @Override
    public void renderAndCheck(SpriteBatch sb){
        if(card!=null){
            card.render(sb);
        }
    }

    @Override
    public void onVictory(){
        card=null;
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0],THRESHOLD.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0],THRESHOLD.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }
}
