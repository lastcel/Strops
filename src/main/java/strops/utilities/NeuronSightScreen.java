package strops.utilities;

import basemod.abstracts.CustomScreen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.buttons.PeekButton;
import strops.patch.PatchNeuronSight;
import strops.relics.NeuronSight;

public class NeuronSightScreen extends CustomScreen {
    public NeuronSightPanel neuronSightPanel=new NeuronSightPanel();
    public PeekButton peekButton=new PeekButton();
    public NeuronSight neuronSight;

    @Override
    public AbstractDungeon.CurrentScreen curScreen(){
        return PatchNeuronSight.PatchTool1.NS_FORETELL;
    }

    public void open(NeuronSight neuronSight) {
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = curScreen();
        AbstractDungeon.overlayMenu.proceedButton.hide();
        AbstractDungeon.overlayMenu.showBlackScreen(0.3F);
        this.neuronSight=neuronSight;
        this.neuronSightPanel.neuronSight=neuronSight;
        this.neuronSightPanel.neuronSightScreen=this;
        this.neuronSight.neuronSightScreen=this;
        neuronSightPanel.open();
        peekButton.show();
    }

    @Override
    public void reopen(){
        //Strops.logger.info("重新打开神经视觉屏幕");
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = curScreen();
        //neuronSight.isListening=true;
        AbstractDungeon.overlayMenu.showBlackScreen(0.3F);
    }


    @Override
    public void close() {
        if (AbstractDungeon.previousScreen == null) {
            if (AbstractDungeon.player.isDead) {
                AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.DEATH;
            } else {
                AbstractDungeon.isScreenUp = false;
                AbstractDungeon.overlayMenu.hideBlackScreen();
            }
        }

        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.player.isDead) {
            AbstractDungeon.overlayMenu.showCombatPanels();
        }

        //neuronSight.isListening=false;
    }

    @Override
    public void openingSettings(){
        AbstractDungeon.previousScreen=curScreen();
    }

    @Override
    public boolean allowOpenDeck(){
        return true;
    }

    @Override
    public void openingDeck(){
        AbstractDungeon.previousScreen=curScreen();
    }

    @Override
    public boolean allowOpenMap(){
        return true;
    }

    @Override
    public void openingMap(){
        AbstractDungeon.previousScreen=curScreen();
    }

    @Override
    public void update() {
        /*
        if(!neuronSight.isListening){
            return;
        }

         */

        peekButton.update();
        if(!PeekButton.isPeeking){
            neuronSightPanel.update();
        } else {
            AbstractDungeon.getCurrRoom().update();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        /*
        if(!neuronSight.isListening){
            return;
        }

         */

        peekButton.render(sb);
        if(!PeekButton.isPeeking){
            neuronSightPanel.render(sb);
        }
    }
}


