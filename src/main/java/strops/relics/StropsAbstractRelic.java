package strops.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.utility.HandCheckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import strops.cards.AbstractStropsCard;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.lang.reflect.Field;
import java.util.ArrayList;

public abstract class StropsAbstractRelic extends CustomRelic {

    public boolean canCopy = true;

    protected boolean usedThisTurn = false;
    //protected boolean needUp = false;
    protected boolean upThisTurn = false;

    public boolean targetMode=false;
    public AbstractMonster hoveredMonster = null;
    protected Vector2[] points = new Vector2[20];
    private Vector2 controlPoint;
    private float arrowScale;
    private float arrowScaleTimer = 0.0F;

    public AbstractCard cardToPreview = null;

    public static String format1,format2,format3,format4;

    public int secondCounter=-1;

    public StropsAbstractRelic(String id, Texture texture, AbstractRelic.RelicTier tier, AbstractRelic.LandingSound sfx) {
        super(id, texture, tier, sfx);
    }

    public StropsAbstractRelic(String id, Texture texture, Texture outline, AbstractRelic.RelicTier tier, AbstractRelic.LandingSound sfx) {
        super(id, texture, outline, tier, sfx);
    }

    public StropsAbstractRelic(String id, String imgName, AbstractRelic.RelicTier tier, AbstractRelic.LandingSound sfx) {
        super(id, imgName, tier, sfx);
    }

    public abstract ArrayList<String> getUpdatedDescription2();

    public ArrayList<RelicSetting> BuildRelicSettings() {
        return new ArrayList<>();
    }

    public ArrayList<String> GetSettingStrings() {
        ArrayList<String> s = new ArrayList<>();
        s.add(this.name);
        return s;
    }

    public void updateDesc() {
        ArrayList<String> s=getUpdatedDescription2();

        this.description = s.get(0);
        this.tips = new ArrayList();
        this.tips.add(new PowerTip(this.name, this.description));

        for(int i=1;i<s.size();i+=2)
        {
            this.tips.add(new PowerTip(s.get(i),s.get(i+1)));
        }

        initializeTips();
    }

    public void onEquipMods(IntSliderSetting sA, IntSliderSetting sB){
        if(sA.value>0){
            AbstractDungeon.player.increaseMaxHp(sA.value,true);
        } else if(sA.value<0){
            AbstractDungeon.player.decreaseMaxHealth(-sA.value);
        }

        if(sB.value>0){
            AbstractDungeon.player.gainGold(sB.value);
        } else if(sB.value<0){
            AbstractDungeon.player.loseGold(-sB.value);
        }
    }

    public void showMHaG(IntSliderSetting sA, IntSliderSetting sB){
        String coloredMH,coloredG;

        if(sA.value>0){
            coloredMH="MH [#34AF23]"+sA.value+"[]";
        } else if(sA.value==0){
            coloredMH="MH "+sA.value;
        } else {
            coloredMH="MH #r"+sA.value;
        }

        if(sB.value>0){
            coloredG=" G [#34AF23]"+sB.value+"[]";
        } else if(sB.value==0){
            coloredG=" G "+sB.value;
        } else {
            coloredG=" G #r"+sB.value;
        }

        this.tips.add(new PowerTip("", coloredMH+coloredG));
    }

    public String getMHaG(IntSliderSetting sA, IntSliderSetting sB){
        String coloredMH,coloredG;

        if(sA.value>0){
            coloredMH="MH [#34AF23]"+sA.value+"[]";
        } else if(sA.value==0){
            coloredMH="MH "+sA.value;
        } else {
            coloredMH="MH #r"+sA.value;
        }

        if(sB.value>0){
            coloredG=" G [#34AF23]"+sB.value+"[]";
        } else if(sB.value==0){
            coloredG=" G "+sB.value;
        } else {
            coloredG=" G #r"+sB.value;
        }

        return (coloredMH+coloredG);
    }

    public void needWarmUp(String s){
        //this.needUp=true;
        this.description = s;
        this.tips.clear();
        this.tips.add(new PowerTip(DESCRIPTIONS[1],DESCRIPTIONS[2]));
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    public void rise(String s){
        //this.needUp=false;
        this.description = s;
        this.tips.clear();
        this.tips.add(new PowerTip(DESCRIPTIONS[1],DESCRIPTIONS[2]));
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    public static void decideFormat(){
        switch (AbstractDungeon.actNum){
            case 0:
            case 1:
                format1="#g";format2="";format3="";format4="";break;
            case 2:format1="";format2="#g";format3="";format4="";break;
            case 3:format1="";format2="";format3="#g";format4="";break;
            case 4:format1="";format2="";format3="";format4="#g";break;
            default:format1="";format2="";format3="";format4="";break;
        }
    }

    public void updateFormat(){

    }

    public void updateTargetMode() {
        if (InputHelper.justClickedRight || AbstractDungeon.isScreenUp || InputHelper.mY > Settings.HEIGHT - 80.0F * Settings.scale || AbstractDungeon.player.hoveredCard != null || InputHelper.mY < 140.0F * Settings.scale || CInputActionSet.cancel

                .isJustPressed()) {
            CInputActionSet.cancel.unpress();
            this.targetMode = false;
            GameCursor.hidden = false;
        }
        this.hoveredMonster = null;
        for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
            if (m.hb.hovered && !m.isDying) {
                this.hoveredMonster = m;
                break;
            }
        }
        if (InputHelper.justClickedLeft || CInputActionSet.select.isJustPressed()) {
            InputHelper.justClickedLeft = false;
            CInputActionSet.select.unpress();
            if (this.hoveredMonster != null) {
                if (AbstractDungeon.player.hasPower("Surrounded")){
                    AbstractDungeon.player.flipHorizontal = (this.hoveredMonster.drawX < AbstractDungeon.player.drawX);
                }
                cannonShoot(this.hoveredMonster);
                if ((AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT){
                    AbstractDungeon.actionManager.addToBottom(new HandCheckAction());
                }
                this.targetMode = false;
                GameCursor.hidden = false;
            }
        }
    }

    protected void renderTargetingUi(SpriteBatch sb) {

        float x = InputHelper.mX, y = InputHelper.mY;
        this.controlPoint = new Vector2(this.currentX - (x - this.currentX) / 4.0F, y + (y - this.currentY - 40.0F * Settings.scale) / 2.0F);
        if (this.hoveredMonster == null) {
            this.arrowScale = Settings.scale;
            this.arrowScaleTimer = 0.0F;
            sb.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
        } else {
            this.arrowScaleTimer += Gdx.graphics.getDeltaTime();
            if (this.arrowScaleTimer > 1.0F)
                this.arrowScaleTimer = 1.0F;
            this.arrowScale = Interpolation.elasticOut.apply(Settings.scale, Settings.scale * 1.2F, this.arrowScaleTimer);
            sb.setColor(new Color(1.0F, 0.2F, 0.3F, 1.0F));
        }
        Vector2 tmp = new Vector2(this.controlPoint.x - x, this.controlPoint.y - y);
        tmp.nor();

        drawCurvedLine(sb, new Vector2(this.currentX, this.currentY - 40.0F * Settings.scale), new Vector2(x, y), this.controlPoint);

        sb.draw(ImageMaster.TARGET_UI_ARROW, x - 128.0F, y - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, this.arrowScale, this.arrowScale, tmp

                .angle() + 90.0F, 0, 0, 256, 256, false, false);
    }

    private void drawCurvedLine(SpriteBatch sb, Vector2 start, Vector2 end, Vector2 control) {

        float radius = 7.0F * Settings.scale;
        for (int i = 0; i < this.points.length - 1; i++) {
            float angle;

            this.points[i] = (Vector2) Bezier.quadratic((Vector)this.points[i], i / 20.0F, (Vector)start, (Vector)control, (Vector)end, (Vector)new Vector2());
            radius += 0.4F * Settings.scale;
            if (i != 0) {
                Vector2 tmp = new Vector2((this.points[i - 1]).x - (this.points[i]).x, (this.points[i - 1]).y - (this.points[i]).y);
                angle = tmp.nor().angle() + 90.0F;
            } else {
                Vector2 tmp = new Vector2(this.controlPoint.x - (this.points[i]).x, this.controlPoint.y - (this.points[i]).y);
                angle = tmp.nor().angle() + 270.0F;
            }

            sb.draw(ImageMaster.TARGET_UI_CIRCLE, (this.points[i]).x - 64.0F, (this.points[i]).y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, radius / 18.0F, radius / 18.0F, angle, 0, 0, 128, 128, false, false);
        }
    }

    public void cannonShoot(AbstractCreature enemy){

    }

    @Override
    public void renderTip(SpriteBatch sb) {
        super.renderTip(sb);
        renderCardPreview(sb);
    }

    public void renderCardPreview(SpriteBatch sb) {
        if (this.cardToPreview != null) {
            if (CardCrawlGame.mainMenuScreen.screen == MainMenuScreen.CurScreen.RELIC_VIEW) {
                this.cardToPreview.current_x = Settings.WIDTH - 380.0F * Settings.scale;
                this.cardToPreview.current_y = Settings.HEIGHT * 0.6F;
            } else {
                if (this.currentX > Settings.WIDTH * 0.75F) {
                    this.cardToPreview.current_x = InputHelper.mX - 420.0F * this.scale;
                } else {
                    this.cardToPreview.current_x = InputHelper.mX + 420.0F * this.scale;
                }
                this.cardToPreview.current_y = InputHelper.mY - 100.0F * this.scale;
            }
            this.cardToPreview.drawScale = this.scale * 0.8F;
            this.cardToPreview.render(sb);
        }
    }

    @Override
    public void renderBossTip(SpriteBatch sb) {
        super.renderBossTip(sb);
        renderCardPreview(sb);
    }

    @Override
    public AbstractRelic makeCopy() {
        if(AbstractDungeon.player!=null&&AbstractDungeon.player.hasRelic(relicId)&&!canCopy){
            switch (tier){
                case COMMON:return new Strawberry();
                case UNCOMMON:return new Pear();
                case RARE:return new Mango();
                case BOSS:return new TinyHouse();
                case SHOP:return new Waffle();
                case SPECIAL:return new GoldenIdol();
                default:return new Circlet();
            }
        }

        return super.makeCopy();
    }

    public AbstractRelic dontCheckMakeCopy() {
        return super.makeCopy();
    }

    public AbstractRelic cannonMakeCopy(){
        if(AbstractDungeon.player==null){
            return super.makeCopy();
        }

        for(AbstractRelic r:AbstractDungeon.player.relics){
            if(AbstractStropsCard.CANNON_RELICS.contains(r.relicId)){
                return new Pear();
            }
        }

        return super.makeCopy();
    }

    public static RelicTier num2Tier(int num){
        switch (num){
            case 1:return RelicTier.COMMON;
            case 2:return RelicTier.UNCOMMON;
            case 3:return RelicTier.RARE;
            case 4:return RelicTier.BOSS;
            case 5:return RelicTier.SHOP;
            default:return RelicTier.DEPRECATED;
        }
    }

    public static int tier2Num(RelicTier tier){
        if(tier==null){
            return -1;
        }

        switch (tier){
            case COMMON:return 1;
            case UNCOMMON:return 2;
            case RARE:return 3;
            case BOSS:return 4;
            case SHOP:return 5;
            case SPECIAL:return 6;
            default:return 7;
        }
    }

    @Override
    public void renderCounter(SpriteBatch sb, boolean inTopPanel) {
        super.renderCounter(sb,inTopPanel);

        if (this.secondCounter > -1){
            if (inTopPanel) {
                try {
                    Field f = AbstractRelic.class.getDeclaredField("offsetX");
                    f.setAccessible(true);
                    float offX=(float)f.get(this);

                    FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont,

                            Integer.toString(this.secondCounter), offX + this.currentX + 30.0F * Settings.scale, this.currentY + 28.0F * Settings.scale, Color.CYAN);
                } catch (IllegalAccessException|NoSuchFieldException e) {
                    e.printStackTrace();
                }
            } else {
                FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont,

                        Integer.toString(this.secondCounter), this.currentX + 30.0F * Settings.scale, this.currentY + 28.0F * Settings.scale, Color.CYAN);
            }
        }
    }
}
