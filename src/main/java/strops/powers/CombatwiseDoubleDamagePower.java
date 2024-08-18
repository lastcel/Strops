package strops.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import strops.helpers.ModHelper;

public class CombatwiseDoubleDamagePower extends AbstractPower{
    public static final String POWER_ID = ModHelper.makePath(CombatwiseDoubleDamagePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public float multiplier;

    public CombatwiseDoubleDamagePower(AbstractCreature owner, float multiplier) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.multiplier = multiplier;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.priority=6;
        String path128 = "StropsResources/img/powers/Example84.png";
        String path48 = "StropsResources/img/powers/Example32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        this.updateDescription();
    }

    public void updateDescription() {
        if(multiplier!=MathUtils.floor(multiplier)){
            this.description = String.format(DESCRIPTIONS[0], multiplier);
        } else {
            this.description = String.format(DESCRIPTIONS[1], multiplier);
        }
    }

    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL)
            return damage * multiplier;
        return damage;
    }
}
