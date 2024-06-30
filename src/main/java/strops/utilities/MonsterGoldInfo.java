package strops.utilities;

import com.megacrit.cardcrawl.core.AbstractCreature;

public class MonsterGoldInfo {
    public AbstractCreature extractedMonster;
    public int extractedTimes;
    public MonsterGoldInfo(AbstractCreature m, int times){
        extractedMonster=m;
        extractedTimes=times;
    }
}
