package io.determann.shadow.api.lang_model.shadow.structure;

import io.determann.shadow.api.shadow.structure.Field;
import io.determann.shadow.api.shadow.type.Declared;

public interface FieldLangModel extends Field,
                                        VariableLangModel
{
   boolean isConstant();

   /**
    * String or primitive value of static fields
    */
   Object getConstantValue();

   @Override
   Declared getSurrounding();
}
