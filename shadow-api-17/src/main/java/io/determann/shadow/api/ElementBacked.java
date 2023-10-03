package io.determann.shadow.api;

import io.determann.shadow.api.metadata.JdkApi;
import io.determann.shadow.api.modifier.Modifiable;
import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.shadow.Module;

import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import java.util.Set;

import static java.util.stream.Collectors.toUnmodifiableSet;

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
      return getElement().getModifiers().stream().map(ElementBacked::mapModifier).collect(toUnmodifiableSet());
   }

   public static Modifier mapModifier(javax.lang.model.element.Modifier modifier)
   {
      return switch (modifier)
      {
         case PUBLIC -> Modifier.PUBLIC;
         case PROTECTED -> Modifier.PROTECTED;
         case PRIVATE -> Modifier.PRIVATE;
         case ABSTRACT -> Modifier.ABSTRACT;
         case STATIC -> Modifier.STATIC;
         case SEALED -> Modifier.SEALED;
         case NON_SEALED -> Modifier.NON_SEALED;
         case FINAL -> Modifier.FINAL;
         case STRICTFP -> Modifier.STRICTFP;
         case DEFAULT -> Modifier.DEFAULT;
         case TRANSIENT -> Modifier.TRANSIENT;
         case VOLATILE -> Modifier.VOLATILE;
         case SYNCHRONIZED -> Modifier.SYNCHRONIZED;
         case NATIVE -> Modifier.NATIVE;
      };
   }

   /**
    * returns itself for a module
    */
   default Module getModule()
   {
      return getApi().getShadowFactory()
                     .shadowFromElement(getApi().getJdkApiContext().getProcessingEnv().getElementUtils().getModuleOf(getElement()));
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
