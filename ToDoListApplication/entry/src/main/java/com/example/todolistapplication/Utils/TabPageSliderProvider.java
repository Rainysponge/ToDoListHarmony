package com.example.todolistapplication.Utils;

import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.PageSliderProvider;

import java.util.List;

public class TabPageSliderProvider extends PageSliderProvider {
    private List<Integer> layoutFileIds;
    private AbilitySlice abilitySlice;

    public TabPageSliderProvider(List<Integer> layoutFileIds, AbilitySlice abilitySlice) {
        this.layoutFileIds = layoutFileIds;
        this.abilitySlice = abilitySlice;
    }

    @Override
    public int getCount() {
        return layoutFileIds.size();
    }

    @Override
    public Object createPageInContainer(ComponentContainer componentContainer, int i) {
        Integer id = layoutFileIds.get(i);
        // component 对应某一个xml
        Component component = LayoutScatter.getInstance(abilitySlice).parse(id, null, false);
        componentContainer.addComponent(component);

        return component;
    }

    @Override
    public void destroyPageFromContainer(ComponentContainer componentContainer, int i, Object o) {
        componentContainer.removeComponent((Component)o);
    }

    @Override
    public boolean isPageMatchToObject(Component component, Object o) {
        return true;
    }
}
