package io.determann.shadow.impl.lang_model.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.converter.ShadowConverter;
import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.*;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;


public class ExecutableImpl extends ShadowImpl<ExecutableType> implements Constructor,
                                                                          Method
{
   private final ExecutableElement executableElement;


   public ExecutableImpl(LangModelContext context, ExecutableElement executableElement)
   {
      super(context, (ExecutableType) executableElement.asType());
      this.executableElement = executableElement;
   }

   @Override
   public Set<Modifier> getModifiers()
   {
      return LangModelAdapter.getModifiers(getElement());
   }

   @Override
   public Return getReturn()
   {
      return new ReturnImpl(getApi(), getMirror().getReturnType());
   }

   @Override
   public Shadow getReturnType()
   {
      return LangModelAdapter.getShadow(getApi(), getMirror().getReturnType());
   }

   @Override
   public List<Shadow> getParameterTypes()
   {
      return getMirror().getParameterTypes()
                        .stream()
                        .map(typeMirror -> LangModelAdapter.<Shadow>getShadow(getApi(), typeMirror))
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
      return Optional.of(LangModelAdapter.getShadow(getApi(), receiverType));
   }

   @Override
   public Optional<Receiver> getReceiver()
   {
      TypeMirror receiverType = getMirror().getReceiverType();
      if (receiverType == null || receiverType.getKind().equals(javax.lang.model.type.TypeKind.NONE))
      {
         return Optional.empty();
      }
      return Optional.of(new ReceiverImpl(getApi(), getMirror().getReceiverType()));
   }

   @Override
   public List<Class> getThrows()
   {
      return getMirror().getThrownTypes()
                        .stream()
                        .map(typeMirror -> LangModelAdapter.<Shadow>getShadow(getApi(), typeMirror))
                        .map(Converter::convert)
                        .map(ShadowConverter::toClassOrThrow)
                        .toList();
   }

   @Override
   public boolean isBridge()
   {
      return LangModelAdapter.getElements(getApi()).isBridge(getElement());
   }

   @Override
   public boolean isVarArgs()
   {
      return getElement().isVarArgs();
   }

   @Override
   public Declared getSurrounding()
   {
      return LangModelAdapter.getShadow(getApi(), getElement().getEnclosingElement());
   }

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
      return LangModelAdapter.getElements(getApi())
                             .overrides(getElement(), LangModelAdapter.getElement(method), LangModelAdapter.getElement(getSurrounding()));
   }

   @Override
   public boolean overwrittenBy(Method method)
   {
      return LangModelAdapter.getElements(getApi())
                             .overrides(LangModelAdapter.getElement(method), getElement(), LangModelAdapter.getElement(method.getSurrounding()));
   }

   @Override
   public boolean sameParameterTypes(Method method)
   {
      return LangModelAdapter.getTypes(getApi()).isSubsignature(getMirror(), LangModelAdapter.getType(method));
   }

   @Override
   public List<Parameter> getParameters()
   {
      return getElement().getParameters()
                         .stream()
                         .map(variableElement -> LangModelAdapter.<Parameter>getShadow(getApi(), variableElement))
                         .toList();
   }

   @Override
   public List<Generic> getGenerics()
   {
      return getElement().getTypeParameters()
                         .stream()
                         .map(element -> LangModelAdapter.<Generic>getShadow(getApi(), element))
                         .toList();
   }

   @Override
   public Package getPackage()
   {
      return LangModelAdapter
                     .getShadow(getApi(), LangModelAdapter.getElements(getApi()).getPackageOf(getElement()));
   }

   @Override
   public Module getModule()
   {
      return LangModelAdapter.getModule(getApi(), getElement());
   }

   @Override
   public String getName()
   {
      return LangModelAdapter.getName(getElement());
   }

   @Override
   public String getJavaDoc()
   {
      return LangModelAdapter.getJavaDoc(getApi(), getElement());
   }

   @Override
   public List<AnnotationUsage> getAnnotationUsages()
   {
      return LangModelAdapter.getAnnotationUsages(getApi(), getElement());
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return LangModelAdapter.getDirectAnnotationUsages(getApi(), getElement());
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
                          getName(),
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
      if (!(other instanceof Executable otherExecutable))
      {
         return false;
      }
      return Objects.equals(getName(), otherExecutable.getName()) &&
             Objects.equals(getTypeKind(), otherExecutable.getTypeKind()) &&
             Objects.equals(getParameters(), otherExecutable.getParameters()) &&
             Objects.equals(getModifiers(), otherExecutable.getModifiers()) &&
             Objects.equals(getParameterTypes(), otherExecutable.getParameterTypes());
   }
}
