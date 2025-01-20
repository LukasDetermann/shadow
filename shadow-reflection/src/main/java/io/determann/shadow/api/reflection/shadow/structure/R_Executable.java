package io.determann.shadow.api.reflection.shadow.structure;

import io.determann.shadow.api.reflection.shadow.R_Annotationable;
import io.determann.shadow.api.reflection.shadow.R_Nameable;
import io.determann.shadow.api.reflection.shadow.modifier.R_Modifiable;
import io.determann.shadow.api.reflection.shadow.type.R_Class;
import io.determann.shadow.api.reflection.shadow.type.R_Declared;
import io.determann.shadow.api.reflection.shadow.type.R_Generic;
import io.determann.shadow.api.reflection.shadow.type.R_Type;
import io.determann.shadow.api.shadow.structure.C_Executable;
import io.determann.shadow.api.shadow.type.C_Declared;

import java.util.List;
import java.util.Optional;

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
    */
   List<R_Parameter> getParameters();

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
