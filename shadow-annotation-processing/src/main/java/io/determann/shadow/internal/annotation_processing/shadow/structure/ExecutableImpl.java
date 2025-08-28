package io.determann.shadow.internal.annotation_processing.shadow.structure;

import io.determann.shadow.api.C;
import io.determann.shadow.api.Modifier;
import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.internal.annotation_processing.ApContextImpl;

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

import static io.determann.shadow.api.annotation_processing.Queries.query;
import static io.determann.shadow.api.annotation_processing.adapter.Adapters.adapt;
import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrEmpty;
import static io.determann.shadow.api.query.Provider.requestOrThrow;


public abstract class ExecutableImpl
{
   private final AP.Context context;
   private final ExecutableElement executableElement;


   protected ExecutableImpl(AP.Context context, ExecutableElement executableElement)
   {
      this.context = context;
      this.executableElement = executableElement;
   }

   public Set<Modifier> getModifiers()
   {
      return ApContextImpl.getModifiers(getElement());
   }

   public AP.Result getResult()
   {
      return new ResultImpl(getApi(), getMirror().getReturnType());
   }

   public AP.Type getReturnType()
   {
      return adapt(getApi(), getMirror().getReturnType());
   }

   public List<AP.Type> getParameterTypes()
   {
      return getMirror().getParameterTypes()
                        .stream()
                        .map(typeMirror -> Adapters.<AP.Type>adapt(getApi(), typeMirror))
                        .toList();
   }

   public Optional<AP.Declared> getReceiverType()
   {
      TypeMirror receiverType = getMirror().getReceiverType();
      if (receiverType == null || receiverType.getKind().equals(TypeKind.NONE))
      {
         return Optional.empty();
      }
      return Optional.of(adapt(getApi(), ((DeclaredType) receiverType)));
   }

   public Optional<AP.Receiver> getReceiver()
   {
      TypeMirror receiverType = getMirror().getReceiverType();
      if (receiverType == null || receiverType.getKind().equals(TypeKind.NONE))
      {
         return Optional.empty();
      }
      return Optional.of(new ReceiverImpl(getApi(), getMirror().getReceiverType()));
   }

   public List<AP.Class> getThrows()
   {
      return getMirror().getThrownTypes()
                        .stream()
                        .map(typeMirror -> Adapters.<AP.Class>adapt(getApi(), typeMirror))
                        .map(AP.Class.class::cast)
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

   public AP.Declared getSurrounding()
   {
      return adapt(getApi(), ((TypeElement) getElement().getEnclosingElement()));
   }

   public ExecutableElement getElement()
   {
      return executableElement;
   }

   public boolean overrides(C.Method method)
   {
      return adapt(getApi()).toElements().overrides(getElement(),
                                                    adapt(((AP.Executable) method)).toExecutableElement(),
                                                    adapt(getSurrounding()).toTypeElement());
   }

   public boolean overwrittenBy(C.Method method)
   {
      return adapt(getApi()).toElements().overrides(adapt((AP.Executable) method).toExecutableElement(),
                                                    getElement(),
                                                    adapt(query(method).getSurrounding()).toTypeElement());
   }

   public boolean sameParameterTypes(C.Method method)
   {
      return adapt(getApi()).toTypes().isSubsignature(getMirror(), adapt((AP.Executable) method).toExecutableType());
   }

   public List<AP.Parameter> getParameters()
   {
      return getElement().getParameters()
                         .stream()
                         .map(VariableElement.class::cast)
                         .map(variableElement -> adapt(getApi(), variableElement))
                         .map(AP.Parameter.class::cast)
                         .toList();
   }

   public List<AP.Generic> getGenerics()
   {
      return getElement().getTypeParameters()
                         .stream()
                         .map(element -> adapt(getApi(), element))
                         .toList();
   }

   public AP.Module getModule()
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

   public List<AP.AnnotationUsage> getAnnotationUsages()
   {
      return adapt(getApi(), adapt(getApi()).toElements().getAllAnnotationMirrors(getElement()));
   }

   public List<AP.AnnotationUsage> getDirectAnnotationUsages()
   {
      return adapt(getApi(), getElement().getAnnotationMirrors());
   }

   public AP.Context getApi()
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
      if (!(other instanceof C.Executable otherExecutable))
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
