package io.determann.shadow.api.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.shadow.LM_Annotationable;
import io.determann.shadow.api.lang_model.shadow.LM_Documented;
import io.determann.shadow.api.lang_model.shadow.LM_ModuleEnclosed;
import io.determann.shadow.api.lang_model.shadow.LM_Nameable;
import io.determann.shadow.api.lang_model.shadow.modifier.LM_Modifiable;
import io.determann.shadow.api.lang_model.shadow.type.LM_Class;
import io.determann.shadow.api.lang_model.shadow.type.LM_Declared;
import io.determann.shadow.api.lang_model.shadow.type.LM_Generic;
import io.determann.shadow.api.lang_model.shadow.type.LM_Type;
import io.determann.shadow.api.shadow.structure.C_Constructor;
import io.determann.shadow.api.shadow.structure.C_Executable;
import io.determann.shadow.api.shadow.structure.C_Parameter;
import io.determann.shadow.api.shadow.type.C_Declared;

import java.lang.annotation.ElementType;
import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.Provider.requestOrThrow;

public sealed interface LM_Executable

      extends C_Executable,
              LM_Annotationable,
              LM_Nameable,
              LM_Modifiable,
              LM_ModuleEnclosed,
              LM_Documented

      permits LM_Constructor,
              LM_Method
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
   List<LM_Parameter> getParameters();

   default LM_Parameter getParameterOrThrow(String name)
   {
      return getParameters().stream().filter(parameter -> requestOrThrow(parameter, NAMEABLE_GET_NAME).equals(name)).findAny().orElseThrow();
   }

   /**
    * Can be annotated using annotations with {@link ElementType#TYPE_USE}
    */
   LM_Return getReturn();

   List<LM_Type> getParameterTypes();

   List<LM_Class> getThrows();

   /**
    * {@link List#of(Object[])}
    */
   boolean isVarArgs();

   /**
    * returns the {@link C_Declared} that surrounds this {@link LM_Executable}
    */
   LM_Declared getSurrounding();

   /**
    * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenerics"}
    */
   List<LM_Generic> getGenerics();

   /**
    * The receiver represents the instance the method is called on. This language feature is barely used, it makes it possible to annotate "this".
    * {@snippet file = "ReceiverUsageTest.java" region = "ReceiverUsageTest.method"}
    */
   Optional<LM_Declared> getReceiverType();

   /**
    * The receiver represents the instance the method is called on. This language feature is barely used, it makes it possible to annotate "this".
    * {@snippet file = "ReceiverUsageTest.java" region = "ReceiverUsageTest.method"}
    */
   Optional<LM_Receiver> getReceiver();
}
