package org.determann.shadow.api.shadow;

import org.determann.shadow.api.modifier.FinalModifiable;
import org.determann.shadow.api.modifier.StaticModifiable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.List;

public interface Record extends Declared,
                                StaticModifiable,
                                FinalModifiable
{
   RecordComponent getRecordComponent(String simpleName);

   @UnmodifiableView List<RecordComponent> getRecordComponents();

   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
