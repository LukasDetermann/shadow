package io.determann.shadow.internal.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.Modifier;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;
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

import static io.determann.shadow.api.annotation_processing.adapter.Adapters.adapt;
import static java.util.Optional.ofNullable;


public abstract class ExecutableImpl
{
   private final Ap.Context context;
   private final ExecutableElement executableElement;


   protected ExecutableImpl(Ap.Context context, ExecutableElement executableElement)
   {
      this.context = context;
      this.executableElement = executableElement;
   }

   public Set<Modifier> getModifiers()
   {
      return ApContextImpl.getModifiers(getElement());
   }

   public Ap.Result getResult()
   {
      return new ResultImpl(getApi(), getMirror().getReturnType());
   }

   public Ap.Type getReturnType()
   {
      return adapt(getApi(), getMirror().getReturnType());
   }

   public List<Ap.Type> getParameterTypes()
   {
      return getMirror().getParameterTypes()
                        .stream()
                        .map(typeMirror -> Adapters.<Ap.Type>adapt(getApi(), typeMirror))
                        .toList();
   }

   public Optional<Ap.Declared> getReceiverType()
   {
      TypeMirror receiverType = getMirror().getReceiverType();
      if (receiverType == null || receiverType.getKind().equals(TypeKind.NONE))
      {
         return Optional.empty();
      }
      return Optional.of(adapt(getApi(), ((DeclaredType) receiverType)));
   }

   public Optional<Ap.Receiver> getReceiver()
   {
      TypeMirror receiverType = getMirror().getReceiverType();
      if (receiverType == null || receiverType.getKind().equals(TypeKind.NONE))
      {
         return Optional.empty();
      }
      return Optional.of(new ReceiverImpl(getApi(), getMirror().getReceiverType()));
   }

   public List<Ap.Class> getThrows()
   {
      return getMirror().getThrownTypes()
                        .stream()
                        .map(typeMirror -> Adapters.<Ap.Class>adapt(getApi(), typeMirror))
                        .map(Ap.Class.class::cast)
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

   public Ap.Declared getSurrounding()
   {
      return adapt(getApi(), ((TypeElement) getElement().getEnclosingElement()));
   }

   public ExecutableElement getElement()
   {
      return executableElement;
   }

   public boolean overrides(Ap.Method method)
   {
      return adapt(getApi()).toElements().overrides(getElement(),
                                                    adapt(method).toExecutableElement(),
                                                    adapt(getSurrounding()).toTypeElement());
   }

   public boolean overwrittenBy(Ap.Method method)
   {
      return adapt(getApi()).toElements().overrides(adapt(method).toExecutableElement(),
                                                    getElement(),
                                                    adapt(method.getSurrounding()).toTypeElement());
   }

   public boolean sameParameterTypes(Ap.Method method)
   {
      return adapt(getApi()).toTypes().isSubsignature(getMirror(), adapt(method).toExecutableType());
   }

   public List<Ap.Parameter> getParameters()
   {
      return getElement().getParameters()
                         .stream()
                         .map(VariableElement.class::cast)
                         .map(variableElement -> adapt(getApi(), variableElement))
                         .map(Ap.Parameter.class::cast)
                         .toList();
   }

   public List<Ap.Generic> getGenericDeclarations()
   {
      return getElement().getTypeParameters()
                         .stream()
                         .map(element -> adapt(getApi(), element))
                         .toList();
   }

   public Ap.Module getModule()
   {
      return adapt(getApi(), adapt(getApi()).toElements().getModuleOf(getElement()));
   }

   public String getName()
   {
      return getElement().getSimpleName().toString();
   }

   public Optional<String> getJavaDoc()
   {
      return ofNullable(adapt(getApi()).toElements().getDocComment(getElement()));
   }

   public List<Ap.AnnotationUsage> getAnnotationUsages()
   {
      return adapt(getApi(), adapt(getApi()).toElements().getAllAnnotationMirrors(getElement()));
   }

   public List<Ap.AnnotationUsage> getDirectAnnotationUsages()
   {
      return adapt(getApi(), getElement().getAnnotationMirrors());
   }

   public Ap.Context getApi()
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
      if (!(other instanceof Ap.Executable otherExecutable))
      {
         return false;
      }
      return Objects.equals(getName(), otherExecutable.getName()) &&
             Objects.equals(getParameters(), otherExecutable.getParameters()) &&
             Objects.equals(getModifiers(), otherExecutable.getModifiers());
   }
}
