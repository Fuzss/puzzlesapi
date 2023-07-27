package fuzs.puzzlesapi.api.client.statues.v1.gui.components;

public interface UnboundedSliderButton {

    default boolean isDirty() {
        return false;
    }

    default void clearDirty() {

    }
}
