package io.determann.shadow.api.reflection.shadow.structure;

import io.determann.shadow.api.reflection.shadow.R_Annotationable;
import io.determann.shadow.api.reflection.shadow.R_Nameable;
import io.determann.shadow.api.reflection.shadow.modifier.R_Modifiable;
import io.determann.shadow.api.reflection.shadow.type.R_Class;
import io.determann.shadow.api.reflection.shadow.type.R_Declared;
import io.determann.shadow.api.reflection.shadow.type.R_Generic;
import io.determann.shadow.api.reflection.shadow.type.R_Type;
import io.determann.shadow.api.shadow.structure.C_Constructor;
import io.determann.shadow.api.shadow.structure.C_Executable;
import io.determann.shadow.api.shadow.structure.C_Method;
import io.determann.shadow.api.shadow.structure.C_Parameter;
import io.determann.shadow.api.shadow.type.C_Declared;

import java.lang.annotation.ElementType;
import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.Provider.requestOrThrow;

/**
 * <ul>
 *    <li>{@link C_Constructor}</li>
 *    <li>{@link C_Method}</li>
 * </ul>
 */
public sealed interface R_Executable

      extends C_Executable,
              R_Annotationable,
              R_Nameable,
              R_Modifiable,
              R_ModuleEnclosed

      permits R_Constructor,
              R_Method
{
   /**
    * {@snippet :
    *  public MyObject(String param){}//@highlight substring="String param"
    *}
    * Returns the formal parameters, meaning everything but the Receiver.
    * <p>
    * there is a bug in {@link java.lang.reflect.Executable#getParameters()} for {@link java.lang.reflect.Constructor}s. For
    * {@link C_Constructor}s with more than one {@link C_Parameter} of the {@link #getReceiverType()} a Receiver will be returned.
    */
   List<R_Parameter> getParameters();

   default R_Parameter getParameterOrThrow(String name)
   {
      return getParameters().stream().filter(parameter -> requestOrThrow(parameter, NAMEABLE_GET_NAME).equals(name)).findAny().orElseThrow();
   }

   /**
    * Can be annotated using annotations with {@link ElementType#TYPE_USE}
    */
   R_Return getReturn();

   R_Type getReturnType();

   List<R_Type> getParameterTypes();

   List<R_Class> getThrows();

   /**
    * {@link List#of(Object[])}
    */
   boolean isVarArgs();

   /**
    * returns the {@link C_Declared} that surrounds this {@link R_Executable}
    */
   R_Declared getSurrounding();

   /**
    * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenerics"}
    */
   List<R_Generic> getGenerics();

   /**
    * The receiver represents the instance the method is called on. This language feature is barely used, it makes it possible to annotate "this".
    * {@snippet file = "ReceiverUsageTest.java" region = "ReceiverUsageTest.method"}
    */
   Optional<R_Declared> getReceiverType();

   /**
    * The receiver represents the instance the method is called on. This language feature is barely used, it makes it possible to annotate "this".
    * {@snippet file = "ReceiverUsageTest.java" region = "ReceiverUsageTest.method"}
    */
   Optional<R_Receiver> getReceiver();
}
