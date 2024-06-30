//borrowed from StunPower in StsLib
package strops.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import strops.helpers.ModHelper;

import java.lang.reflect.Field;

public class FrozenPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath(FrozenPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private byte moveByte;

    private AbstractMonster.Intent moveIntent;

    private EnemyMoveInfo move;

    public FrozenPower(AbstractMonster owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.DEBUFF;
        this.priority = 100;
        String path128 = "StropsResources/img/powers/Example84.png";
        String path48 = "StropsResources/img/powers/Example32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        updateDescription();
    }

    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public void onInitialApplication() {
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            public void update() {
                if (owner instanceof AbstractMonster) {
                    moveByte = ((AbstractMonster)owner).nextMove;
                    moveIntent = ((AbstractMonster)owner).intent;
                    try {
                        Field f = AbstractMonster.class.getDeclaredField("move");
                        f.setAccessible(true);
                        move = (EnemyMoveInfo)f.get(owner);
                        EnemyMoveInfo stunMove = new EnemyMoveInfo(moveByte, AbstractMonster.Intent.STUN, -1, 0, false);
                        f.set(owner, stunMove);
                        ((AbstractMonster)owner).createIntent();
                    } catch (IllegalAccessException|NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
                this.isDone = true;
            }
        });
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (damageAmount > 0){
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        }
        return damageAmount;
    }

    @Override
    public void onRemove() {
        if (owner instanceof AbstractMonster) {
            AbstractMonster m = (AbstractMonster)owner;
            if (move != null) {
                m.setMove(moveByte, moveIntent, move.baseDamage, move.multiplier, move.isMultiDamage);
            } else {
                m.setMove(moveByte, moveIntent);
            }
            m.createIntent();
            m.applyPowers();
        }
    }
}
