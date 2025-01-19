package io.determann.shadow.internal.lang_model.shadow.structure;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.adapter.LM_Adapters;
import io.determann.shadow.api.lang_model.shadow.LM_AnnotationUsage;
import io.determann.shadow.api.lang_model.shadow.structure.*;
import io.determann.shadow.api.lang_model.shadow.type.LM_Class;
import io.determann.shadow.api.lang_model.shadow.type.LM_Declared;
import io.determann.shadow.api.lang_model.shadow.type.LM_Generic;
import io.determann.shadow.api.lang_model.shadow.type.LM_Type;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.structure.C_Executable;
import io.determann.shadow.api.shadow.structure.C_Method;
import io.determann.shadow.internal.lang_model.LangModelContextImpl;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.lang_model.LM_Queries.query;
import static io.determann.shadow.api.lang_model.adapter.LM_Adapters.adapt;


public abstract class ExecutableImpl
{
   private final LM_Context context;
   private final ExecutableElement executableElement;


   public ExecutableImpl(LM_Context context, ExecutableElement executableElement)
   {
      this.context = context;
      this.executableElement = executableElement;
   }

   public Set<C_Modifier> getModifiers()
   {
      return LangModelContextImpl.getModifiers(getElement());
   }

   public LM_Return getReturn()
   {
      return new ReturnImpl(getApi(), getMirror().getReturnType());
   }

   public LM_Type getReturnType()
   {
      return adapt(getApi(), getMirror().getReturnType());
   }

   public List<LM_Type> getParameterTypes()
   {
      return getMirror().getParameterTypes()
                        .stream()
                        .map(typeMirror -> LM_Adapters.<LM_Type>adapt(getApi(), typeMirror))
                        .toList();
   }

   public Optional<LM_Declared> getReceiverType()
   {
      TypeMirror receiverType = getMirror().getReceiverType();
      if (receiverType == null || receiverType.getKind().equals(TypeKind.NONE))
      {
         return Optional.empty();
      }
      return Optional.of(adapt(getApi(), ((DeclaredType) receiverType)));
   }

   public Optional<LM_Receiver> getReceiver()
   {
      TypeMirror receiverType = getMirror().getReceiverType();
      if (receiverType == null || receiverType.getKind().equals(TypeKind.NONE))
      {
         return Optional.empty();
      }
      return Optional.of(new ReceiverImpl(getApi(), getMirror().getReceiverType()));
   }

   public List<LM_Class> getThrows()
   {
      return getMirror().getThrownTypes()
                        .stream()
                        .map(typeMirror -> LM_Adapters.<LM_Class>adapt(getApi(), typeMirror))
                        .map(LM_Class.class::cast)
                        .toList();
   }

   public boolean isBridge()
   {
      return adapt(getApi()).toElements().isBridge(getElement());
   }

   public boolean isVarArgs()
   {
      return getElement().isVarArgs();
   }

   public LM_Declared getSurrounding()
   {
      return adapt(getApi(), ((TypeElement) getElement().getEnclosingElement()));
   }

   public ExecutableElement getElement()
   {
      return executableElement;
   }

   public boolean overrides(C_Method method)
   {
      return adapt(getApi()).toElements().overrides(getElement(),
                                                    adapt(((LM_Executable) method)).toExecutableElement(),
                                                    adapt(getSurrounding()).toTypeElement());
   }

   public boolean overwrittenBy(C_Method method)
   {
      return adapt(getApi()).toElements().overrides(adapt((LM_Executable) method).toExecutableElement(),
                                                    getElement(),
                                                    adapt(query(method).getSurrounding()).toTypeElement());
   }

   public boolean sameParameterTypes(C_Method method)
   {
      return adapt(getApi()).toTypes().isSubsignature(getMirror(), adapt((LM_Executable) method).toExecutableType());
   }

   public List<LM_Parameter> getParameters()
   {
      return getElement().getParameters()
                         .stream()
                         .map(VariableElement.class::cast)
                         .map(variableElement -> adapt(getApi(), variableElement))
                         .map(LM_Parameter.class::cast)
                         .toList();
   }

   public List<LM_Generic> getGenerics()
   {
      return getElement().getTypeParameters()
                         .stream()
                         .map(element -> LM_Adapters.adapt(getApi(), element))
                         .toList();
   }

   public LM_Module getModule()
   {
      return adapt(getApi(), adapt(getApi()).toElements().getModuleOf(getElement()));
   }

   public String getName()
   {
      return getElement().getSimpleName().toString();
   }

   public String getJavaDoc()
   {
      return adapt(getApi()).toElements().getDocComment(getElement());
   }

   public List<LM_AnnotationUsage> getAnnotationUsages()
   {
      return adapt(getApi(), adapt(getApi()).toElements().getAllAnnotationMirrors(getElement()));
   }

   public List<LM_AnnotationUsage> getDirectAnnotationUsages()
   {
      return adapt(getApi(), getElement().getAnnotationMirrors());
   }

   public LM_Context getApi()
   {
      return context;
   }

   public ExecutableType getMirror()
   {
      return (ExecutableType) executableElement.asType();
   }

   @Override
   public String toString()
   {
      return getElement().toString();
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getName(),
                          getParameterTypes(),
                          getParameters(),
                          getModifiers());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof C_Executable otherExecutable))
      {
         return false;
      }
      return requestOrEmpty(otherExecutable, NAMEABLE_GET_NAME).map(name -> Objects.equals(getName(), name)).orElse(false) &&
             Objects.equals(getParameters(), requestOrThrow(otherExecutable, EXECUTABLE_GET_PARAMETERS)) &&
             requestOrEmpty(otherExecutable, MODIFIABLE_GET_MODIFIERS).map(modifiers -> Objects.equals(modifiers, getModifiers()))
                                                                      .orElse(false) &&
             Objects.equals(getParameterTypes(), requestOrThrow(otherExecutable, EXECUTABLE_GET_PARAMETER_TYPES));
   }

   public Implementation getImplementation()
   {
      return getApi().getImplementation();
   }
}
