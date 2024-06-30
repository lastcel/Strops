package strops.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

public class GeneralDamageRandomEnemyAction extends AbstractGameAction{
    int damage;
    private AttackEffect effect;

    public GeneralDamageRandomEnemyAction(int damage, AttackEffect effect) {
        this.damage = damage;
        this.effect = effect;
    }

    public GeneralDamageRandomEnemyAction(int damage) {
        this(damage, AttackEffect.NONE);
    }

    public void update() {
        this.target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        if (this.target != null) {
            if (AbstractGameAction.AttackEffect.LIGHTNING == this.effect) {
                addToTop(new FastDamageAction(this.target, new DamageInfo(AbstractDungeon.player, damage, DamageType.THORNS), AbstractGameAction.AttackEffect.NONE));
                addToTop(new SFXAction("ORB_LIGHTNING_EVOKE", 0.1F));
                addToTop(new VFXAction(new LightningEffect(this.target.hb.cX, this.target.hb.cY)));
            } else {
                addToTop(new FastDamageAction(this.target, new DamageInfo(AbstractDungeon.player, damage, DamageType.THORNS), this.effect));
            }
        }
        this.isDone = true;
    }
}
