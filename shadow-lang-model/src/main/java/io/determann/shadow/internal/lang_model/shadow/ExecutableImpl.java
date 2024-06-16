package io.determann.shadow.internal.lang_model.shadow;

import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.converter.TypeConverter;
import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.query.ConstructorLangModel;
import io.determann.shadow.api.lang_model.query.MethodLangMethod;
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

import static io.determann.shadow.api.lang_model.LangModelAdapter.generalize;
import static io.determann.shadow.api.lang_model.LangModelQueries.query;
import static io.determann.shadow.internal.lang_model.LangModelProvider.IMPLEMENTATION_NAME;
import static io.determann.shadow.meta_meta.Operations.*;
import static io.determann.shadow.meta_meta.Provider.request;
import static io.determann.shadow.meta_meta.Provider.requestOrThrow;


public class ExecutableImpl implements ConstructorLangModel,
                                       MethodLangMethod
{
   private final LangModelContext context;
   private final ExecutableElement executableElement;


   public ExecutableImpl(LangModelContext context, ExecutableElement executableElement)
   {
      this.context = context;
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
      return LangModelAdapter.generalize(getApi(), getMirror().getReturnType());
   }

   @Override
   public List<Shadow> getParameterTypes()
   {
      return getMirror().getParameterTypes()
                        .stream()
                        .map(typeMirror -> LangModelAdapter.<Shadow>generalize(getApi(), typeMirror))
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
      return Optional.of(LangModelAdapter.generalize(getApi(), receiverType));
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
                        .map(typeMirror -> LangModelAdapter.<Shadow>generalize(getApi(), typeMirror))
                        .map(Converter::convert)
                        .map(TypeConverter::toClassOrThrow)
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
      return LangModelAdapter.generalize(getApi(), getElement().getEnclosingElement());
   }

   public ExecutableElement getElement()
   {
      return executableElement;
   }

   @Override
   public boolean overrides(Method method)
   {
      return LangModelAdapter.getElements(getApi())
                             .overrides(getElement(),
                                        LangModelAdapter.particularElement(method),
                                        LangModelAdapter.particularElement(getSurrounding()));
   }

   @Override
   public boolean overwrittenBy(Method method)
   {
      return LangModelAdapter.getElements(getApi())
                             .overrides(LangModelAdapter.particularElement(method),
                                        getElement(),
                                        LangModelAdapter.particularElement(query(method).getSurrounding()));
   }

   @Override
   public boolean sameParameterTypes(Method method)
   {
      return LangModelAdapter.getTypes(getApi()).isSubsignature(getMirror(), LangModelAdapter.particularType(method));
   }

   @Override
   public List<Parameter> getParameters()
   {
      return getElement().getParameters()
                         .stream()
                         .map(variableElement -> LangModelAdapter.<Parameter>generalize(getApi(), variableElement))
                         .toList();
   }

   @Override
   public List<Generic> getGenerics()
   {
      return getElement().getTypeParameters()
                         .stream()
                         .map(element -> LangModelAdapter.<Generic>generalize(getApi(), element))
                         .toList();
   }

   @Override
   public Package getPackage()
   {
      return LangModelAdapter
            .generalize(getApi(), LangModelAdapter.getElements(getApi()).getPackageOf(getElement()));
   }

   @Override
   public Module getModule()
   {
      return LangModelAdapter.generalize(getApi(), LangModelAdapter.getElements(getApi()).getModuleOf(getElement()));
   }

   @Override
   public String getName()
   {
      return getElement().getSimpleName().toString();
   }

   @Override
   public String getJavaDoc()
   {
      return LangModelAdapter.getElements(getApi()).getDocComment(getElement());
   }

   @Override
   public List<AnnotationUsage> getAnnotationUsages()
   {
      return generalize(getApi(), LangModelAdapter.getElements(getApi()).getAllAnnotationMirrors(getElement()));
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return generalize(getApi(), getElement().getAnnotationMirrors());
   }

   public LangModelContext getApi()
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
      if (!(other instanceof Executable otherExecutable))
      {
         return false;
      }
      return request(otherExecutable, NAMEABLE_NAME).map(name -> Objects.equals(getName(), name)).orElse(false) &&
             Objects.equals(getParameters(), requestOrThrow(otherExecutable, EXECUTABLE_GET_PARAMETERS)) &&
             Objects.equals(getModifiers(), otherExecutable.getModifiers()) &&
             Objects.equals(getParameterTypes(), requestOrThrow(otherExecutable, EXECUTABLE_GET_PARAMETER_TYPES));
   }

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}
