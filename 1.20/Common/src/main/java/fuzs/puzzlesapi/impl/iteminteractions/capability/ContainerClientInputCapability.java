package fuzs.puzzlesapi.impl.iteminteractions.capability;

import fuzs.puzzleslib.api.capability.v2.data.CapabilityComponent;

public interface ContainerClientInputCapability extends CapabilityComponent {

    int getCurrentSlot();

    void setCurrentSlot(int currentSlot);

    boolean extractSingleItemOnly();

    void extractSingleItem(boolean singleItemOnly);
}
