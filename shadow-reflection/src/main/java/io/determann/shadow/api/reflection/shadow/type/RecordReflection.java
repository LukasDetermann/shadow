package io.determann.shadow.api.reflection.shadow.type;

import io.determann.shadow.api.shadow.structure.RecordComponent;
import io.determann.shadow.api.shadow.type.Generic;
import io.determann.shadow.api.shadow.type.Record;
import io.determann.shadow.api.shadow.type.Shadow;

import java.util.List;

import static io.determann.shadow.api.shadow.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

public interface RecordReflection extends Record,
                                          DeclaredReflection
{
   default RecordComponent getRecordComponentOrThrow(String simpleName)
   {
      return getRecordComponents().stream().filter(field -> requestOrThrow(field, NAMEABLE_GET_NAME).equals(simpleName)).findAny().orElseThrow();
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
