package org.determann.shadow.api.shadow;

import org.determann.shadow.api.modifier.FinalModifiable;
import org.determann.shadow.api.modifier.StaticModifiable;

import java.util.List;

public interface Record extends Declared,
                                StaticModifiable,
                                FinalModifiable
{
   RecordComponent getRecordComponent(String simpleName);

   List<RecordComponent> getRecordComponents();

   List<Interface> getDirectInterfaces();

   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
