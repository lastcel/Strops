/*
package strops.utilities;

import basemod.IUIElement;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.helpers.Prefs;
import strops.modcore.Strops;

import java.util.ArrayList;

public class FloatSliderSetting extends RelicSetting {
    public AdvModSlider slider;
    public float value;
    public float multi;
    public float min;
    public String suf;

    public FloatSliderSetting(String id, String name, float defaultProperty) {
        this(id, name, defaultProperty, true);
    }
    public FloatSliderSetting(String id, String name, float defaultProperty, boolean isPercentage) {
        this(id, name, defaultProperty, (isPercentage) ? 100.0f: 1.0f, (isPercentage) ? "%": "");
    }
    public FloatSliderSetting(String id, String name, float defaultProperty, float min, float max) {
        this(id, name, defaultProperty, min, max, true);
    }
    public FloatSliderSetting(String id, String name, float defaultProperty, float min, float max, boolean isPercentage) {
        this(id, name, defaultProperty, min, max, (isPercentage) ? "%": "");
    }
    public FloatSliderSetting(String id, String name, float defaultProperty, final float multi, final String suf) {
        this(id, name, defaultProperty, 0, multi, suf);
    }
    public FloatSliderSetting(String id, String name, float defaultProperty, final float min, final float multi, final String suf) {
        super(id, name, Float.toString(defaultProperty));
        this.multi = multi;
        this.min = min;
        this.value = defaultProperty;
        this.suf = suf;
    }

    @Override
    public void LoadFromData(SpireConfig config) {
        this.value = config.getFloat(this.settingsId);
        if (this.slider != null) {
            this.slider.value = this.value;
        }
    }

    @Override
    public void SaveToData(SpireConfig config) {
        config.setFloat(this.settingsId, this.value);
    }
    @Override
    public void LoadFromData(Prefs config) {
        this.value = config.getFloat(this.settingsId, Float.parseFloat(this.defaultProperty));
        if (this.slider != null) {
            this.slider.value = this.value;
        }
    }
    @Override
    public void SaveToData(Prefs config) {
        config.putFloat(this.settingsId, this.value);
    }

    @Override
    public ArrayList<IUIElement> GenerateElements(float x, float y) {
        this.slider = new AdvModSlider(this.name, x, y, this.min, this.multi, this.suf, Strops.settingsPanel, (me) -> {
            this.value = me.value + (this.min / me.multiplier);
            Strops.saveSettingsData();
        });
        this.slider.setValue(this.value);
        this.elements.add(this.slider);
        return this.elements;
    }
    @Override
    public void ResetToDefault() {
        this.value = Float.parseFloat(defaultProperty);
        if (this.slider != null) {
            this.slider.value = this.value;
        }
        Strops.saveSettingsData();
    }

}

 */