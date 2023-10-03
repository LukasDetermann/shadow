package io.determann.shadow.api;

import io.determann.shadow.api.metadata.JdkApi;
import io.determann.shadow.api.modifier.Modifiable;
import io.determann.shadow.api.modifier.Modifier;

import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import java.util.Collections;
import java.util.Set;

import static io.determann.shadow.api.modifier.Modifier.*;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

/**
 * The {@link ShadowApi} is transient to the java annotation processor api. meaning you can get the api used my this api.
 * in this case {@link Element}
 */
public interface ElementBacked<ELEMENT extends Element> extends Modifiable,
                                                                ApiHolder
{
   @JdkApi
   ELEMENT getElement();

   @Override
   default Set<Modifier> getModifiers()
   {
      return getElement().getModifiers()
                         .stream()
                         .map(ElementBacked::mapModifier)
                         .collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
   }

   public static Modifier mapModifier(javax.lang.model.element.Modifier modifier)
   {
      switch (modifier)
      {
         case PUBLIC:
            return PUBLIC;
         case PROTECTED:
            return PROTECTED;
         case PRIVATE:
            return PRIVATE;
         case ABSTRACT:
            return ABSTRACT;
         case STATIC:
            return STATIC;
         case FINAL:
            return FINAL;
         case STRICTFP:
            return STRICTFP;
         case DEFAULT:
            return DEFAULT;
         case TRANSIENT:
            return TRANSIENT;
         case VOLATILE:
            return VOLATILE;
         case SYNCHRONIZED:
            return SYNCHRONIZED;
         case NATIVE:
            return NATIVE;
         default:
            throw new IllegalArgumentException();
      }
   }

   default String getSimpleName()
   {
      return getElement().getSimpleName().toString();
   }

   /**
    * returns the javaDoc or null if none is present
    */
   default String getJavaDoc()
   {
      return getApi().getJdkApiContext().getProcessingEnv().getElementUtils().getDocComment(getElement());
   }

   default void logError(String msg)
   {
      getApi().getJdkApiContext().getProcessingEnv().getMessager().printMessage(Diagnostic.Kind.ERROR, msg, getElement());
   }

   default void logInfo(String msg)
   {
      getApi().getJdkApiContext().getProcessingEnv().getMessager().printMessage(Diagnostic.Kind.NOTE, msg, getElement());
   }

   default void logWarning(String msg)
   {
      getApi().getJdkApiContext().getProcessingEnv().getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, msg, getElement());
   }
}
