package io.determann.shadow.api.lang_model.query;

import io.determann.shadow.api.shadow.Constructor;
import io.determann.shadow.api.shadow.Executable;
import io.determann.shadow.api.shadow.Method;
import io.determann.shadow.api.shadow.Parameter;

import java.util.List;

/**
 * Parameter of a method or constructor
 *
 * @see Method#getParameters()
 * @see Constructor#getParameters()
 */
public interface ParameterLangModel extends Parameter,
                                            VariableLangModel
{
   /**
    * {@link List#of(Object[])}
    */
   boolean isVarArgs();

   @Override
   Executable getSurrounding();
}