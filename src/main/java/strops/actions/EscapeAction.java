package strops.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;

public class EscapeAction extends AbstractGameAction {
    public void update() {
        AbstractPlayer abstractPlayer = AbstractDungeon.player;
        if ((AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT) {
            (AbstractDungeon.getCurrRoom()).smoked = true;
            addToBot(new VFXAction(new SmokeBombEffect((abstractPlayer).hb.cX, (abstractPlayer).hb.cY)));
            AbstractDungeon.player.hideHealthBar();
            AbstractDungeon.player.isEscaping = true;
            AbstractDungeon.player.flipHorizontal = !AbstractDungeon.player.flipHorizontal;
            AbstractDungeon.overlayMenu.endTurnButton.disable();
            AbstractDungeon.player.escapeTimer = 0.5F;
        }

        this.isDone=true;
    }
}
