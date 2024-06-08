package io.determann.shadow.api.reflection.query;

import io.determann.shadow.api.shadow.Generic;
import io.determann.shadow.api.shadow.Record;
import io.determann.shadow.api.shadow.RecordComponent;
import io.determann.shadow.api.shadow.Shadow;

import java.util.List;

import static io.determann.shadow.meta_meta.Operations.NAMEABLE_NAME;
import static io.determann.shadow.meta_meta.Provider.requestOrThrow;

public interface RecordReflection extends Record,
                                          DeclaredReflection
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
