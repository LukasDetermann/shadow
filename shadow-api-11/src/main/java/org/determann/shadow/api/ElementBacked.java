package org.determann.shadow.api;

import org.determann.shadow.api.metadata.JdkApi;
import org.determann.shadow.api.modifier.Modifiable;
import org.determann.shadow.api.modifier.Modifier;
import org.determann.shadow.api.shadow.Module;
import org.jetbrains.annotations.UnmodifiableView;

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
   default @UnmodifiableView Set<Modifier> getModifiers()
   {
      return getElement().getModifiers().stream().map(Modifier::mapModifier).collect(toUnmodifiableSet());
   }

   /**
    * returns itself for a module
    */
   default Module getModule()
   {
      return getApi().getShadowFactory().shadowFromElement(getApi().getJdkApiContext().elements().getModuleOf(getElement()));
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
      return getApi().getJdkApiContext().elements().getDocComment(getElement());
   }

   default void logError(String msg)
   {
      getApi().getJdkApiContext().messager().printMessage(Diagnostic.Kind.ERROR, msg, getElement());
   }

   default void logInfo(String msg)
   {
      getApi().getJdkApiContext().messager().printMessage(Diagnostic.Kind.NOTE, msg, getElement());
   }

   default void logWarning(String msg)
   {
      getApi().getJdkApiContext().messager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, msg, getElement());
   }
}
