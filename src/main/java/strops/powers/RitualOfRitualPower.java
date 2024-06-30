package strops.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.RitualPower;
import strops.helpers.ModHelper;

public class RitualOfRitualPower extends AbstractPower {
    // 能力的ID
    public static final String POWER_ID = ModHelper.makePath(RitualOfRitualPower.class.getSimpleName());
    // 能力的本地化字段
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public RitualOfRitualPower(AbstractCreature owner, int Amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        //this.priority = 6; //加能力动作在排演时就已经传层数值进去，所以想通过控制排演的先后顺序影响执行效果是做不到的，这句完全不起作用

        // 如果需要不能叠加的能力，只需将上面的Amount参数删掉，并把下面的Amount改成-1就行
        this.amount = Amount;

        // 添加一大一小两张能力图
        String path128 = "StropsResources/img/powers/Example84.png";
        String path48 = "StropsResources/img/powers/Example32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        // 首次添加能力更新描述
        this.updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        flash();
        addToBot(new ApplyPowerAction(this.owner, this.owner, new RitualPower(this.owner, this.amount,
                true), this.amount));
    }

    /*
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!AbstractDungeon.player.hasPower("Ritual")) {
            flash();
            addToBot(new ApplyPowerAction(this.owner, this.owner, new RitualPower(this.owner, this.amount,
                    true), this.amount));
        }
    }

     */

    // 能力在更新时如何修改描述
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount) ;
    }
}

