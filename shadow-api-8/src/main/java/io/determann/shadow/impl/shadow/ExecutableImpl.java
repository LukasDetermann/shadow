package io.determann.shadow.impl.shadow;

import io.determann.shadow.api.MirrorAdapter;
import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.converter.ShadowConverter;
import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.*;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import java.util.*;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;


public class ExecutableImpl extends ShadowImpl<ExecutableType> implements Constructor,
                                                                          Method
{
   private final ExecutableElement executableElement;


   public ExecutableImpl(ShadowApi shadowApi, ExecutableElement executableElement)
   {
      super(shadowApi, (ExecutableType) executableElement.asType());
      this.executableElement = executableElement;
   }

   @Override
   public Set<Modifier> getModifiers()
   {
      return MirrorAdapter.getModifiers(getElement());
   }

   @Override
   public Shadow getReturnType()
   {
      return getApi().getShadowFactory().shadowFromType(getMirror().getReturnType());
   }

   @Override
   public List<Shadow> getParameterTypes()
   {
      return getMirror().getParameterTypes()
                        .stream()
                        .map(typeMirror -> getApi().getShadowFactory().<Shadow>shadowFromType(typeMirror))
                        .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public Optional<Declared> getReceiverType()
   {
      TypeMirror receiverType = getMirror().getReceiverType();
      if (receiverType == null || receiverType.getKind().equals(javax.lang.model.type.TypeKind.NONE))
      {
         return Optional.empty();
      }
      return Optional.of(getApi().getShadowFactory().shadowFromType(receiverType));
   }

   @Override
   public List<Class> getThrows()
   {
      return getMirror().getThrownTypes()
                        .stream()
                        .map(typeMirror -> getApi().getShadowFactory().<Shadow>shadowFromType(typeMirror))
                        .map(ShadowApi::convert)
                        .map(ShadowConverter::toClassOrThrow)
                        .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public boolean isVarArgs()
   {
      return getElement().isVarArgs();
   }

   @Override
   public Declared getSurrounding()
   {
      return getApi().getShadowFactory().shadowFromElement(getElement().getEnclosingElement());
   }

   public ExecutableElement getElement()
   {
      return executableElement;
   }

   @Override
   public TypeKind getTypeKind()
   {
      switch (getElement().getKind())
      {
         case CONSTRUCTOR:
            return TypeKind.CONSTRUCTOR;
         case METHOD:
            return TypeKind.METHOD;
         default:
            throw new IllegalStateException();
      }
   }

   @Override
   public boolean overrides(Method method)
   {
      return getApi().getJdkApiContext()
                     .getProcessingEnv()
                     .getElementUtils()
                     .overrides(getElement(), MirrorAdapter.getElement(method), MirrorAdapter.getElement(getSurrounding()));
   }

   @Override
   public boolean overwrittenBy(Method method)
   {
      return getApi().getJdkApiContext()
                     .getProcessingEnv()
                     .getElementUtils()
                     .overrides(MirrorAdapter.getElement(method), getElement(), MirrorAdapter.getElement(method.getSurrounding()));
   }

   @Override
   public boolean sameParameterTypes(Method method)
   {
      return getApi().getJdkApiContext().getProcessingEnv().getTypeUtils().isSubsignature(getMirror(), MirrorAdapter.getType(method));
   }

   @Override
   public List<Parameter> getParameters()
   {
      return getElement().getParameters()
                         .stream()
                         .map(variableElement -> getApi().getShadowFactory().<Parameter>shadowFromElement(variableElement))
                         .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public Parameter getParameterOrThrow(String name)
   {
      return getParameters().stream()
                            .filter(parameter -> parameter.getSimpleName().equals(name))
                            .findAny()
                            .orElseThrow(NoSuchElementException::new);
   }

   @Override
   public List<Generic> getFormalGenerics()
   {
      return getElement().getTypeParameters()
                         .stream()
                         .map(element -> getApi().getShadowFactory().<Generic>shadowFromElement(element))
                         .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public Package getPackage()
   {
      return getApi().getShadowFactory()
                     .shadowFromElement(getApi().getJdkApiContext().getProcessingEnv().getElementUtils().getPackageOf(getElement()));
   }

   @Override
   public String getSimpleName()
   {
      return MirrorAdapter.getSimpleName(getElement());
   }

   @Override
   public String getJavaDoc()
   {
      return MirrorAdapter.getJavaDoc(getApi(), getElement());
   }

   @Override
   public void logError(String msg)
   {
      MirrorAdapter.logError(getApi(), getElement(), msg);
   }

   @Override
   public void logInfo(String msg)
   {
      MirrorAdapter.logInfo(getApi(), getElement(), msg);
   }

   @Override
   public void logWarning(String msg)
   {
      MirrorAdapter.logWarning(getApi(), getElement(), msg);
   }

   @Override
   public List<AnnotationUsage> getAnnotationUsages()
   {
      return MirrorAdapter.getAnnotationUsages(getApi(), getElement());
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return MirrorAdapter.getDirectAnnotationUsages(getApi(), getElement());
   }

   @Override
   public String toString()
   {
      return getElement().toString();
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getTypeKind(),
                          getSimpleName(),
                          getParameterTypes(),
                          getSurrounding());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (other == null || !getClass().equals(other.getClass()))
      {
         return false;
      }
      ExecutableImpl otherExecutable = (ExecutableImpl) other;
      return Objects.equals(getSimpleName(), otherExecutable.getSimpleName()) &&
             Objects.equals(getTypeKind(), otherExecutable.getTypeKind()) &&
             Objects.equals(getParameterTypes(), otherExecutable.getParameterTypes()) &&
             Objects.equals(getSurrounding(), otherExecutable.getSurrounding());
   }
}
