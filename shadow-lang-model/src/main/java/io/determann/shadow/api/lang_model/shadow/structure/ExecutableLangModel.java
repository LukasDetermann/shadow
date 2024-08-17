package io.determann.shadow.api.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.shadow.AnnotationableLangModel;
import io.determann.shadow.api.lang_model.shadow.DocumentedLangModel;
import io.determann.shadow.api.lang_model.shadow.ModuleEnclosedLangModel;
import io.determann.shadow.api.lang_model.shadow.NameableLangModel;
import io.determann.shadow.api.lang_model.shadow.modifier.ModifiableLangModel;
import io.determann.shadow.api.lang_model.shadow.type.ClassLangModel;
import io.determann.shadow.api.lang_model.shadow.type.DeclaredLangModel;
import io.determann.shadow.api.lang_model.shadow.type.GenericLangModel;
import io.determann.shadow.api.lang_model.shadow.type.ShadowLangModel;
import io.determann.shadow.api.shadow.structure.Constructor;
import io.determann.shadow.api.shadow.structure.Executable;
import io.determann.shadow.api.shadow.structure.Method;
import io.determann.shadow.api.shadow.structure.Parameter;
import io.determann.shadow.api.shadow.type.Declared;

import java.lang.annotation.ElementType;
import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.shadow.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

/**
 * <ul>
 *    <li>{@link Constructor}</li>
 *    <li>{@link Method}</li>
 * </ul>
 */
public interface ExecutableLangModel extends Executable,
                                             AnnotationableLangModel,
                                             NameableLangModel,
                                             ModifiableLangModel,
                                             ModuleEnclosedLangModel,
                                             DocumentedLangModel
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
   List<ParameterLangModel> getParameters();

   default ParameterLangModel getParameterOrThrow(String name)
   {
      return getParameters().stream().filter(parameter -> requestOrThrow(parameter, NAMEABLE_GET_NAME).equals(name)).findAny().orElseThrow();
   }

   /**
    * Can be annotated using annotations with {@link ElementType#TYPE_USE}
    */
   ReturnLangModel getReturn();

   ShadowLangModel getReturnType();

   List<ShadowLangModel> getParameterTypes();

   List<ClassLangModel> getThrows();

   /**
    * {@link List#of(Object[])}
    */
   boolean isVarArgs();

   /**
    * returns the {@link Declared} that surrounds this {@link ExecutableLangModel}
    */
   DeclaredLangModel getSurrounding();

   PackageLangModel getPackage();

   /**
    * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenerics"}
    */
   List<GenericLangModel> getGenerics();

   /**
    * The receiver represents the instance the method is called on. This language feature is barely used, it makes it possible to annotate "this".
    * {@snippet file = "ReceiverUsageTest.java" region = "ReceiverUsageTest.method"}
    */
   Optional<DeclaredLangModel> getReceiverType();

   /**
    * The receiver represents the instance the method is called on. This language feature is barely used, it makes it possible to annotate "this".
    * {@snippet file = "ReceiverUsageTest.java" region = "ReceiverUsageTest.method"}
    */
   Optional<ReceiverLangModel> getReceiver();
}
