package io.determann.shadow.api.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.shadow.modifier.FinalModifiableLangModel;
import io.determann.shadow.api.shadow.structure.Constructor;
import io.determann.shadow.api.shadow.structure.Method;
import io.determann.shadow.api.shadow.structure.Parameter;

import java.util.List;

/**
 * Parameter of a method or constructor
 *
 * @see Method#getParameters()
 * @see Constructor#getParameters()
 */
public interface ParameterLangModel extends Parameter,
                                            VariableLangModel,
                                            FinalModifiableLangModel
{
   /**
    * {@link List#of(Object[])}
    */
   boolean isVarArgs();

   @Override
   ExecutableLangModel getSurrounding();
}
