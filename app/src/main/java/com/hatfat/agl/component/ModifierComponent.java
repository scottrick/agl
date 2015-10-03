package com.hatfat.agl.component;

import com.hatfat.agl.component.transform.Transform;
import com.hatfat.agl.modifiers.Modifier;

public class ModifierComponent extends AglComponent {

    protected int numModifiers    = 0;
    protected int maxNumModifiers = 4;
    protected Modifier[] modifiers;
    protected int updateInt = 0;

    public ModifierComponent() {
        super(ComponentType.MODIFIER);

        this.modifiers = new Modifier[maxNumModifiers];
    }

    public void addModifier(Modifier modifier) {
        if (numModifiers + 1 >= maxNumModifiers) {
            throw new RuntimeException("Can't add more modifiers!");
        }

        modifiers[numModifiers] = modifier;
        numModifiers++;
    }

    public void removeModifier(Modifier modifier) {
        int modifierIndex = -1;

        for (int i = 0; i < numModifiers; i++) {
            if (modifiers[i] == modifier) {
                modifierIndex = i;
                break;
            }
        }

        if (modifierIndex < 0) {
            //modifier was not on this node!
            return;
        }

        modifiers[modifierIndex] = modifiers[numModifiers - 1]; //swap the last modifier with the modifier we are removing

        numModifiers--;

        modifiers[numModifiers] = null;
    }

    public void update(float deltaTime, Transform transform) {
        for (updateInt = 0; updateInt < numModifiers; updateInt++) {
            modifiers[updateInt].update(deltaTime, transform);
        }
    }
}
