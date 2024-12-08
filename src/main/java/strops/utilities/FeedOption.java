package strops.utilities;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import strops.relics.Gluttony;

public class FeedOption extends AbstractCampfireOption {
    public static final String[] TEXT=CardCrawlGame.languagePack.getRelicStrings(Gluttony.ID).DESCRIPTIONS;

    Gluttony gluttony;

    public FeedOption(boolean active, Gluttony gluttony) {
        this.gluttony = gluttony;
        this.label = TEXT[5];
        this.usable = active;
        this.description = String.format(TEXT[6],Gluttony.STEP.value,gluttony.counter-AbstractDungeon.player.maxHealth*(Gluttony.INITIAL.value-(gluttony.level+1)*Gluttony.STEP.value)/100/*Gluttony.STEP.value*AbstractDungeon.player.maxHealth/100*/);
        this.img = ImageMaster.loadImage("StropsResources/img/misc/GluttonyFeedOption.png");
    }

    @Override
    public void useOption() {
        gluttony.level++;
        /*
        for(AbstractRelic r:AbstractDungeon.player.relics){
            if(r.relicId.equals(Gluttony.ID)){
                ((Gluttony)r).level++;
                break;
            }
        }

         */

        (AbstractDungeon.getCurrRoom()).phase = AbstractRoom.RoomPhase.COMPLETE;
    }
}
