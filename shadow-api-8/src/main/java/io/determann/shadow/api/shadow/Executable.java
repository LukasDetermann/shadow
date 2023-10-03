package io.determann.shadow.api.shadow;

import io.determann.shadow.api.Annotationable;
import io.determann.shadow.api.ShadowApi;

import javax.lang.model.element.ExecutableElement;
import java.util.List;
import java.util.Optional;

/**
 * any code block. Can be converted into the following using {@link ShadowApi#convert(Declared)}
 *
 * <ul>
 *    <li>{@link Constructor}</li>
 *    <li>{@link Method}</li>
 * </ul>
 */
public interface Executable extends Shadow,
                                    Annotationable<ExecutableElement>
{
   /**
    * {@code public MyObject(}<b>String param</b>{@code )}
    */
   List<Parameter> getParameters();

   Parameter getParameterOrThrow(String name);

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
   List<Generic> getFormalGenerics();

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

   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
