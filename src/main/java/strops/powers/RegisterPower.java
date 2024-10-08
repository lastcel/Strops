package strops.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import strops.helpers.ModHelper;

import java.util.ArrayList;

public class RegisterPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath(RegisterPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ArrayList<AbstractCard> cards=new ArrayList<>();

    public RegisterPower(AbstractMonster owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        String path128 = "StropsResources/img/powers/Example84.png";
        String path48 = "StropsResources/img/powers/Example32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
        boolean firstCard=true;
        for(AbstractCard c:cards){
            if(firstCard){
                description+=" NL #y"+c.name;
            } else {
                description+=" | #y"+c.name;
            }
            firstCard=false;
        }
    }

    @Override
    public void onDeath() {
        for(AbstractCard c:cards){
            addToBot(new MakeTempCardInHandAction(c, false, true));
        }
    }
}
