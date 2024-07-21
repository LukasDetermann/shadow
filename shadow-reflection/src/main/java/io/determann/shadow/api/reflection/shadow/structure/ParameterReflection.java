package io.determann.shadow.api.reflection.shadow.structure;

import io.determann.shadow.api.reflection.shadow.modifier.FinalModifiableReflection;
import io.determann.shadow.api.shadow.structure.Constructor;
import io.determann.shadow.api.shadow.structure.Executable;
import io.determann.shadow.api.shadow.structure.Method;
import io.determann.shadow.api.shadow.structure.Parameter;

import java.util.List;

/**
 * Parameter of a method or constructor
 *
 * @see Method#getParameters()
 * @see Constructor#getParameters()
 */
public interface ParameterReflection extends Parameter,
                                             VariableReflection,
                                             FinalModifiableReflection
{
   /**
    * {@link List#of(Object[])}
    */
   boolean isVarArgs();

   @Override
   Executable getSurrounding();
}
