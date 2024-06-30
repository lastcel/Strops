package strops.utilities;

import basemod.IUIElement;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.random.Random;
import strops.modcore.Strops;

import java.util.ArrayList;

//import static basemod.BaseMod.logger;

public class IntSliderSetting extends RelicSetting {
    public AdvModSlider slider;

    public int value;

    public float multi;

    public int min;

    public String suf;

    public IntSliderSetting(String id, String name, int defaultProperty, int multi) {
        this(id, name, defaultProperty, 0, multi);
    }

    public IntSliderSetting(String id, String name, int defaultProperty, int min, int multi) {
        this(id, name, defaultProperty, min, multi, "");
    }

    public IntSliderSetting(String id, String name, int defaultProperty, int min, int multi, String suf) {
        super(id, name, Integer.toString(defaultProperty));
        this.multi = multi;
        this.min = min;
        this.suf = suf;
        this.value = defaultProperty;
    }

    public void LoadFromData(SpireConfig config) {
        this.value = config.getInt(this.settingsId);
        //logger.info("读取，settingsId="+this.settingsId+",value="+this.value);
        if (config.getBool(this.settingsId + "_IS_SET_TO_DEFAULT"))
            this.value = Integer.parseInt(this.defaultProperty);
        if (this.slider != null)
            this.slider.setValue(this.value);
    }

    public void SaveToData(SpireConfig config) {
        config.setInt(this.settingsId, this.value);
        //logger.info("保存，settingsId="+this.settingsId+",value="+this.value);
        config.setBool(this.settingsId + "_IS_SET_TO_DEFAULT", (this.value == Integer.parseInt(this.defaultProperty)));
    }

    public void LoadFromData(Prefs config) {
        this.value = config.getInteger(this.settingsId, Integer.parseInt(this.defaultProperty));
        if (this.slider != null)
            this.slider.setValue(this.value);
    }

    public void SaveToData(Prefs config) {
        config.putInteger(this.settingsId, this.value);
    }

    public ArrayList<IUIElement> GenerateElements(float x, float y) {
        this.slider = new AdvModSlider(this.name, x + 125.0F, y, this.min, this.multi, this.suf, Strops.settingsPanel, me -> {
            this.value = Math.round(me.value * me.multiplier + this.min);
            Strops.saveSettingsData();
        });
        this.slider.setValue(this.value);
        this.elements.add(this.slider);
        return this.elements;
    }

    public boolean testChance(Random rng) {
        return (rng.random(99) < this.value);
    }

    public void ResetToDefault() {
        this.value = Integer.parseInt(this.defaultProperty);
        if (this.slider != null)
            this.slider.setValue(this.value);
        Strops.saveSettingsData();
    }
}

