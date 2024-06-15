package io.determann.shadow.api.lang_model.query;

import io.determann.shadow.api.shadow.Declared;
import io.determann.shadow.api.shadow.Field;

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
