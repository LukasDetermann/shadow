package io.determann.shadow.api.shadow;

import io.determann.shadow.api.modifier.AbstractModifiable;
import io.determann.shadow.api.modifier.Sealable;
import io.determann.shadow.api.modifier.StaticModifiable;

import java.util.List;

public interface Interface extends Declared,
                                   AbstractModifiable,
                                   StaticModifiable,
                                   Sealable
{
   boolean isFunctional();

   /**
    * {@code List<}<b>String</b>{@code >}
    */
   List<Shadow> getGenericTypes();

   /**
    * {@code List<}<b>T</b>{@code >}
    */
   List<Generic> getGenerics();
}
