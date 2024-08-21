package io.determann.shadow.internal.lang_model.shadow.structure;

import io.determann.shadow.api.Provider;
import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.shadow.AnnotationUsageLangModel;
import io.determann.shadow.api.lang_model.shadow.structure.*;
import io.determann.shadow.api.lang_model.shadow.type.ClassLangModel;
import io.determann.shadow.api.lang_model.shadow.type.DeclaredLangModel;
import io.determann.shadow.api.lang_model.shadow.type.GenericLangModel;
import io.determann.shadow.api.lang_model.shadow.type.ShadowLangModel;
import io.determann.shadow.api.shadow.modifier.Modifier;
import io.determann.shadow.api.shadow.structure.Executable;
import io.determann.shadow.api.shadow.structure.Method;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.lang_model.LangModelAdapter.*;
import static io.determann.shadow.api.lang_model.LangModelQueries.query;
import static io.determann.shadow.internal.lang_model.LangModelProvider.IMPLEMENTATION_NAME;


public class ExecutableImpl implements ConstructorLangModel,
                                       MethodLangModel
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
   public ReturnLangModel getReturn()
   {
      return new ReturnImpl(getApi(), getMirror().getReturnType());
   }

   @Override
   public ShadowLangModel getReturnType()
   {
      return generalize(getApi(), getMirror().getReturnType());
   }

   @Override
   public List<ShadowLangModel> getParameterTypes()
   {
      return getMirror().getParameterTypes()
                        .stream()
                        .map(typeMirror -> LangModelAdapter.<ShadowLangModel>generalize(getApi(), typeMirror))
                        .toList();
   }

   @Override
   public Optional<DeclaredLangModel> getReceiverType()
   {
      TypeMirror receiverType = getMirror().getReceiverType();
      if (receiverType == null || receiverType.getKind().equals(javax.lang.model.type.TypeKind.NONE))
      {
         return Optional.empty();
      }
      return Optional.of(LangModelAdapter.generalize(getApi(), receiverType));
   }

   @Override
   public Optional<ReceiverLangModel> getReceiver()
   {
      TypeMirror receiverType = getMirror().getReceiverType();
      if (receiverType == null || receiverType.getKind().equals(javax.lang.model.type.TypeKind.NONE))
      {
         return Optional.empty();
      }
      return Optional.of(new ReceiverImpl(getApi(), getMirror().getReceiverType()));
   }

   @Override
   public List<ClassLangModel> getThrows()
   {
      return getMirror().getThrownTypes()
                        .stream()
                        .map(typeMirror -> LangModelAdapter.<ClassLangModel>generalize(getApi(), typeMirror))
                        .map(ClassLangModel.class::cast)
                        .toList();
   }

   @Override
   public boolean isBridge()
   {
      return getElements(getApi()).isBridge(getElement());
   }

   @Override
   public boolean isVarArgs()
   {
      return getElement().isVarArgs();
   }

   @Override
   public DeclaredLangModel getSurrounding()
   {
      return generalize(getApi(), getElement().getEnclosingElement());
   }

   public ExecutableElement getElement()
   {
      return executableElement;
   }

   @Override
   public boolean overrides(Method method)
   {
      return getElements(getApi()).overrides(getElement(),
                                             particularElement(((MethodLangModel) method)),
                                             particularElement(getSurrounding()));
   }

   @Override
   public boolean overwrittenBy(Method method)
   {
      return getElements(getApi()).overrides(particularElement((MethodLangModel)method),
                                             getElement(),
                                             particularElement(query(method).getSurrounding()));
   }

   @Override
   public boolean sameParameterTypes(Method method)
   {
      return LangModelAdapter.getTypes(getApi()).isSubsignature(getMirror(), LangModelAdapter.particularType((MethodLangModel)method));
   }

   @Override
   public List<ParameterLangModel> getParameters()
   {
      return getElement().getParameters()
                         .stream()
                         .map(variableElement -> LangModelAdapter.<ParameterLangModel>generalize(getApi(), variableElement))
                         .toList();
   }

   @Override
   public List<GenericLangModel> getGenerics()
   {
      return getElement().getTypeParameters()
                         .stream()
                         .map(element -> LangModelAdapter.<GenericLangModel>generalize(getApi(), element))
                         .toList();
   }

   @Override
   public PackageLangModel getPackage()
   {
      return generalizePackage(getApi(), getElements(getApi()).getPackageOf(getElement()));
   }

   @Override
   public ModuleLangModel getModule()
   {
      return generalize(getApi(), getElements(getApi()).getModuleOf(getElement()));
   }

   @Override
   public String getName()
   {
      return getElement().getSimpleName().toString();
   }

   @Override
   public String getJavaDoc()
   {
      return getElements(getApi()).getDocComment(getElement());
   }

   @Override
   public List<AnnotationUsageLangModel> getAnnotationUsages()
   {
      return generalize(getApi(), getElements(getApi()).getAllAnnotationMirrors(getElement()));
   }

   @Override
   public List<AnnotationUsageLangModel> getDirectAnnotationUsages()
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
      return Provider.requestOrEmpty(otherExecutable, NAMEABLE_GET_NAME).map(name -> Objects.equals(getName(), name)).orElse(false) &&
             Objects.equals(getParameters(), requestOrThrow(otherExecutable, EXECUTABLE_GET_PARAMETERS)) &&
             requestOrEmpty(otherExecutable, MODIFIABLE_GET_MODIFIERS).map(modifiers -> Objects.equals(modifiers, getModifiers())).orElse(false) &&
             Objects.equals(getParameterTypes(), requestOrThrow(otherExecutable, EXECUTABLE_GET_PARAMETER_TYPES));
   }

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}
