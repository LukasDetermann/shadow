package io.determann.shadow.api.shadow;

import io.determann.shadow.api.Annotationable;
import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.modifier.Modifiable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * any code block. Can be converted into the following using {@link Converter#convert(Declared)}
 *
 * <ul>
 *    <li>{@link Constructor}</li>
 *    <li>{@link Method}</li>
 * </ul>
 */
public interface Executable extends Shadow,
                                    Annotationable,
                                    Modifiable
{
   /**
    * {@code public MyObject(}<b>String param</b>{@code )}
    */
   List<Parameter> getParameters();

   default Parameter getParameterOrThrow(String name)
   {
      return getParameters().stream()
                            .filter(parameter -> parameter.getName().equals(name))
                            .findAny()
                            .orElseThrow(NoSuchElementException::new);
   }

   Shadow getReturnType();

   List<Shadow> getParameterTypes();

   List<Class> getThrows();

   /**
    * {@link java.util.Arrays#asList(Object[])}
    */
   boolean isVarArgs();

   /**
    * returns the {@link Declared} that surrounds this {@link Executable}
    */
   Declared getSurrounding();

   Package getPackage();

   /**
    * {@code List<}<b>T</b>{@code >}
    */
   List<Generic> getGenerics();

   /**
    * The receiver represents the instance the method is called on. This language feature is barely used, but makes it possible to annotate "this".
    * Those annotations can only be accessed by refection.
    * <pre>{@code
    *    public class ReceiverExample {
    *       {
    *          new ReceiverExample().foo();
    *       }
    *       public void foo(ReceiverExample ReceiverExample.this) {
    *       }
    *    }
    * }</pre>
    */
   Optional<Declared> getReceiverType();
}
