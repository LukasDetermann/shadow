package io.determann.shadow.api.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.shadow.modifier.AccessModifiableLangModel;
import io.determann.shadow.api.lang_model.shadow.modifier.FinalModifiableLangModel;
import io.determann.shadow.api.lang_model.shadow.modifier.StaticModifiableLangModel;
import io.determann.shadow.api.lang_model.shadow.type.DeclaredLangModel;
import io.determann.shadow.api.shadow.structure.Field;

public interface FieldLangModel extends Field,
                                        VariableLangModel,
                                        AccessModifiableLangModel,
                                        FinalModifiableLangModel,
                                        StaticModifiableLangModel

{
   boolean isConstant();

   /**
    * String or primitive value of static fields
    */
   Object getConstantValue();

   @Override
   DeclaredLangModel getSurrounding();
}
