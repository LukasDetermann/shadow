package org.determann.shadow.impl.shadow;

import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.TypeKind;
import org.determann.shadow.api.shadow.Package;
import org.determann.shadow.api.shadow.*;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


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
   public Shadow<TypeMirror> getReturnType()
   {
      return getApi().getShadowFactory().shadowFromType(getMirror().getReturnType());
   }

   @Override
   public List<Shadow<TypeMirror>> getParameterTypes()
   {
      return getMirror().getParameterTypes()
                        .stream()
                        .map(typeMirror -> getApi().getShadowFactory().<Shadow<TypeMirror>>shadowFromType(typeMirror))
                        .toList();
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
   public List<Shadow<TypeMirror>> getThrows()
   {
      return getMirror().getThrownTypes()
                        .stream()
                        .map(typeMirror -> getApi().getShadowFactory().<Shadow<TypeMirror>>shadowFromType(typeMirror))
                        .toList();
   }

   @Override
   public boolean isBridge()
   {
      return getApi().getJdkApiContext().elements().isBridge(getElement());
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

   @Override
   public ExecutableElement getElement()
   {
      return executableElement;
   }

   @Override
   public TypeKind getTypeKind()
   {
      return switch (getElement().getKind())
            {
               case CONSTRUCTOR -> TypeKind.CONSTRUCTOR;
               case METHOD -> TypeKind.METHOD;
               default -> throw new IllegalStateException();
            };
   }

   @Override
   public boolean overrides(Method method)
   {
      return getApi().getJdkApiContext().elements().overrides(getElement(), method.getElement(), getSurrounding().getElement());
   }

   @Override
   public boolean isSubSignatureOf(Method method)
   {
      return getApi().getJdkApiContext().types().isSubsignature(this.getMirror(), method.getMirror());
   }

   @Override
   public List<Parameter> getParameters()
   {
      return getElement().getParameters()
                         .stream()
                         .map(variableElement -> getApi().getShadowFactory().<Parameter>shadowFromElement(variableElement))
                         .toList();
   }

   @Override
   public Parameter getParameter(String name)
   {
      return getParameters().stream().filter(parameter -> parameter.getSimpleName().equals(name)).findAny().orElseThrow();
   }

   @Override
   public List<Generic> getFormalGenerics()
   {
      return getElement().getTypeParameters()
                         .stream()
                         .map(element -> getApi().getShadowFactory().<Generic>shadowFromElement(element))
                         .toList();
   }

   @Override
   public Package getPackage()
   {
      return getApi().getShadowFactory().shadowFromElement(getApi().getJdkApiContext().elements().getPackageOf(getElement()));
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
