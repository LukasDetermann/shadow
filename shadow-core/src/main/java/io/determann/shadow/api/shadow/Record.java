package io.determann.shadow.api.shadow;

import io.determann.shadow.api.modifier.FinalModifiable;
import io.determann.shadow.api.modifier.StaticModifiable;

import java.util.List;

import static io.determann.shadow.meta_meta.Operations.NAMEABLE_NAME;
import static io.determann.shadow.meta_meta.Provider.requestOrThrow;

public interface Record extends Declared,
                                StaticModifiable,
                                FinalModifiable
{
   default RecordComponent getRecordComponentOrThrow(String simpleName)
   {
      return getRecordComponents().stream().filter(field -> requestOrThrow(field, NAMEABLE_NAME).equals(simpleName)).findAny().orElseThrow();
   }

   List<RecordComponent> getRecordComponents();

   /**
    * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenericTypes"}
    */
   List<Shadow> getGenericTypes();

   /**
    * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenerics"}
    */
   List<Generic> getGenerics();
}
