package io.determann.shadow.api.shadow;

import io.determann.shadow.api.Annotationable;
import io.determann.shadow.api.Documented;
import io.determann.shadow.api.ModuleEnclosed;
import io.determann.shadow.api.Nameable;
import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.modifier.Modifiable;

import java.lang.annotation.ElementType;
import java.util.List;
import java.util.Optional;

import static io.determann.shadow.meta_meta.Operations.NAMEABLE_NAME;
import static io.determann.shadow.meta_meta.Provider.requestOrThrow;

/**
 * any code block. Can be converted into the following using {@link Converter#convert(Declared)}
 *
 * <ul>
 *    <li>{@link Constructor}</li>
 *    <li>{@link Method}</li>
 * </ul>
 */
public interface Executable extends Annotationable,
                                    Nameable,
                                    Modifiable,
                                    ModuleEnclosed,
                                    Documented
{
   /**
    * {@snippet :
    *  public MyObject(String param){}//@highlight substring="String param"
    *}
    * Returns the formal parameters, meaning everything but the Receiver.
    * <p>
    * there is a bug in {@link java.lang.reflect.Executable#getParameters()} for {@link java.lang.reflect.Constructor}s. For
    * {@link Constructor}s with more than one {@link Parameter} of the {@link #getReceiverType()} a Receiver will be returned.
    */
   List<Parameter> getParameters();

   default Parameter getParameterOrThrow(String name)
   {
      return getParameters().stream().filter(parameter -> requestOrThrow(parameter, NAMEABLE_NAME).equals(name)).findAny().orElseThrow();
   }

   /**
    * Can be annotated using annotations with {@link ElementType#TYPE_USE}
    */
   Return getReturn();

   Shadow getReturnType();

   List<Shadow> getParameterTypes();

   List<Class> getThrows();

   /**
    * {@link List#of(Object[])}
    */
   boolean isVarArgs();

   /**
    * returns the {@link Declared} that surrounds this {@link Executable}
    */
   Declared getSurrounding();

   Package getPackage();

   /**
    * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenerics"}
    */
   List<Generic> getGenerics();

   /**
    * The receiver represents the instance the method is called on. This language feature is barely used, it makes it possible to annotate "this".
    * {@snippet file = "ReceiverUsageTest.java" region = "ReceiverUsageTest.method"}
    */
   Optional<Declared> getReceiverType();

   /**
    * The receiver represents the instance the method is called on. This language feature is barely used, it makes it possible to annotate "this".
    * {@snippet file = "ReceiverUsageTest.java" region = "ReceiverUsageTest.method"}
    */
   Optional<Receiver> getReceiver();
}
