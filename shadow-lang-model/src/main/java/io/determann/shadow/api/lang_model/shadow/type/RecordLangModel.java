package io.determann.shadow.api.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.shadow.modifier.FinalModifiableLangModel;
import io.determann.shadow.api.lang_model.shadow.modifier.StaticModifiableLangModel;
import io.determann.shadow.api.lang_model.shadow.structure.RecordComponentLangModel;
import io.determann.shadow.api.shadow.structure.RecordComponent;
import io.determann.shadow.api.shadow.type.Record;

import java.util.List;

import static io.determann.shadow.api.shadow.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

public interface RecordLangModel extends Record,
                                         DeclaredLangModel,
                                         StaticModifiableLangModel,
                                         FinalModifiableLangModel
{
   default RecordComponent getRecordComponentOrThrow(String simpleName)
   {
      return getRecordComponents().stream().filter(field -> requestOrThrow(field, NAMEABLE_GET_NAME).equals(simpleName)).findAny().orElseThrow();
   }

   List<RecordComponentLangModel> getRecordComponents();

   /**
    * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenericTypes"}
    */
   List<ShadowLangModel> getGenericTypes();

   /**
    * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenerics"}
    */
   List<GenericLangModel> getGenerics();
}
