package io.determann.shadow.api.shadow;

import io.determann.shadow.api.modifier.FinalModifiable;
import io.determann.shadow.api.modifier.StaticModifiable;

import java.util.List;

public interface Record extends Declared,
                                StaticModifiable,
                                FinalModifiable
{
   default RecordComponent getRecordComponentOrThrow(String simpleName)
   {
      return getRecordComponents().stream().filter(field -> field.getName().equals(simpleName)).findAny().orElseThrow();
   }

   List<RecordComponent> getRecordComponents();

   /**
    * {@snippet file="GenericUsageTest.java" region="GenericUsage.getGenericTypes"}
    */
   List<Shadow> getGenericTypes();

   /**
    * {@snippet file="GenericUsageTest.java" region="GenericUsage.getGenerics"}
    */
   List<Generic> getGenerics();
}
