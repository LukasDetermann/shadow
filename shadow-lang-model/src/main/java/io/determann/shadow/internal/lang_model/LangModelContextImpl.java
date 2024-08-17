package io.determann.shadow.internal.lang_model;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelConstants;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.LangModelContextImplementation;
import io.determann.shadow.api.lang_model.shadow.structure.ModuleLangModel;
import io.determann.shadow.api.lang_model.shadow.structure.PackageLangModel;
import io.determann.shadow.api.lang_model.shadow.structure.RecordComponentLangModel;
import io.determann.shadow.api.lang_model.shadow.type.*;
import io.determann.shadow.api.shadow.Provider;
import io.determann.shadow.api.shadow.structure.Field;
import io.determann.shadow.api.shadow.structure.Module;
import io.determann.shadow.api.shadow.structure.Parameter;
import io.determann.shadow.api.shadow.structure.RecordComponent;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Record;
import io.determann.shadow.api.shadow.type.*;
import io.determann.shadow.internal.lang_model.shadow.structure.RecordComponentImpl;

import javax.lang.model.element.PackageElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.lang_model.LangModelAdapter.*;
import static io.determann.shadow.api.lang_model.LangModelQueries.query;
import static io.determann.shadow.api.shadow.Operations.*;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

public class LangModelContextImpl implements LangModelContext,
                                             LangModelContextImplementation
{
   private final Types types;
   private final Elements elements;

   public LangModelContextImpl(Types types, Elements elements)
   {
      this.types = types;
      this.elements = elements;
   }

   @Override
   public List<ModuleLangModel> getModules()
   {
      return elements.getAllModuleElements()
                     .stream()
                     .map(moduleElement -> LangModelAdapter.<ModuleLangModel>generalize(this, moduleElement))
                     .toList();
   }

   @Override
   public Optional<ModuleLangModel> getModule(String name)
   {
      return ofNullable(elements.getModuleElement(name))
            .map(moduleElement -> LangModelAdapter.generalize(this, moduleElement));
   }
   
   @Override
   public ModuleLangModel getModuleOrThrow(String name)
   {
      return getModule(name).orElseThrow();
   }

   @Override
   public List<PackageLangModel> getPackages(String qualifiedName)
   {
      return elements.getAllPackageElements(qualifiedName)
                     .stream()
                     .map(packageElement -> generalizePackage(this, packageElement))
                     .toList();
   }

   @Override
   public List<PackageLangModel> getPackages()
   {
      return elements.getAllModuleElements()
                     .stream()
                     .flatMap(moduleElement -> moduleElement.getEnclosedElements().stream())
                     .map(PackageElement.class::cast)
                     .map(packageElement -> generalizePackage(this, packageElement))
                     .toList();
   }

   @Override
   public Optional<PackageLangModel> getPackage(String qualifiedModuleName, String qualifiedPackageName)
   {
      return getPackage(getModuleOrThrow(qualifiedModuleName), qualifiedPackageName);
   }

   @Override
   public PackageLangModel getPackageOrThrow(String qualifiedModuleName, String qualifiedPackageName)
   {
      return getPackage(qualifiedModuleName, qualifiedPackageName).orElseThrow();
   }

   @Override
   public Optional<PackageLangModel> getPackage(Module module, String qualifiedPackageName)
   {
      return ofNullable(elements.getPackageElement(particularElement((ModuleLangModel) module), qualifiedPackageName))
            .map(packageElement -> generalizePackage(this, packageElement));
   }

   @Override
   public PackageLangModel getPackageOrThrow(Module module, String qualifiedPackageName)
   {
      return getPackage(module, qualifiedPackageName).orElseThrow();
   }

   @Override
   public Optional<DeclaredLangModel> getDeclared(String qualifiedName)
   {
      return ofNullable(elements.getTypeElement(qualifiedName))
            .map(typeElement -> LangModelAdapter.generalize(this, typeElement));
   }

   @Override
   public List<DeclaredLangModel> getDeclared()
   {
      return getPackages()
            .stream()
            .flatMap(packageShadow -> requestOrThrow(packageShadow, PACKAGE_GET_DECLARED_LIST) .stream())
            .map(DeclaredLangModel.class::cast)
            .toList();
   }

   @Override
   public LangModelConstants getConstants()
   {
      return new LangModelConstantsImpl(this);
   }


   @Override
   public ClassLangModel erasure(Class aClass)
   {
      return erasureImpl(particularType((DeclaredLangModel) aClass));
   }

   @Override
   public InterfaceLangModel erasure(Interface anInterface)
   {
      return erasureImpl(particularType((DeclaredLangModel) anInterface));
   }

   @Override
   public RecordLangModel erasure(Record aRecord)
   {
      return erasureImpl(particularType((DeclaredLangModel) aRecord));
   }

   @Override
   public ArrayLangModel erasure(Array array)
   {
      return erasureImpl(particularType((DeclaredLangModel) array));
   }

   @Override
   public ShadowLangModel erasure(Wildcard wildcard)
   {
      return erasureImpl(particularType((DeclaredLangModel) wildcard));
   }

   @Override
   public ShadowLangModel erasure(Generic generic)
   {
      return erasureImpl(particularType((DeclaredLangModel) generic));
   }

   @Override
   public ShadowLangModel erasure(Intersection intersection)
   {
      return erasureImpl(particularType((DeclaredLangModel) intersection));
   }

   @Override
   public RecordComponentLangModel erasure(RecordComponent recordComponent)
   {
      return erasureImpl(((RecordComponentImpl) recordComponent).getMirror());
   }

   @Override
   public ShadowLangModel erasure(Parameter parameter)
   {
      return erasureImpl(particularType((DeclaredLangModel) parameter));
   }

   @Override
   public ShadowLangModel erasure(Field field)
   {
      return erasureImpl(particularType((DeclaredLangModel) field));
   }

   private <S extends Shadow> S erasureImpl(TypeMirror typeMirror)
   {
      return LangModelAdapter.generalize(this, types.erasure(typeMirror));
   }

   @Override
   public ClassLangModel interpolateGenerics(Class aClass)
   {
      return LangModelAdapter.generalize(this, types.capture(particularType((DeclaredLangModel) aClass)));
   }

   @Override
   public InterfaceLangModel interpolateGenerics(Interface anInterface)
   {
      return LangModelAdapter.generalize(this, types.capture(particularType((DeclaredLangModel) anInterface)));
   }

   @Override
   public RecordLangModel interpolateGenerics(Record aRecord)
   {
      return LangModelAdapter.generalize(this, types.capture(particularType((DeclaredLangModel) aRecord)));
   }

   @Override
   public ClassLangModel withGenerics(Class aClass, Shadow... generics)
   {
      List<? extends Generic> generics1 = requestOrThrow(aClass, CLASS_GET_GENERICS);
      if (generics.length == 0 || generics1.size() != generics.length)
      {
         throw new IllegalArgumentException(requestOrThrow(aClass, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME) +
                                            " has " +
                                            generics1.size() +
                                            " generics. " +
                                            generics.length +
                                            " are provided");
      }
      Optional<Declared> outerType = Provider.requestOrEmpty(aClass, CLASS_GET_OUTER_TYPE);
      if (outerType.isPresent() &&
          (outerType.get() instanceof Interface anInterface &&
          !requestOrThrow(anInterface, INTERFACE_GET_GENERICS).isEmpty() ||
          outerType.get() instanceof Class aClass1 && !requestOrThrow(aClass1, CLASS_GET_GENERIC_TYPES).isEmpty()))
      {
         throw new IllegalArgumentException("cant add generics to " +
                                            requestOrThrow(aClass, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME) +
                                            " when the class is not static and the outer class has generics");
      }
      TypeMirror[] typeMirrors = stream(generics)
            .map(ShadowLangModel.class::cast)
            .map(LangModelAdapter::particularType)
            .toArray(TypeMirror[]::new);

      return LangModelAdapter.generalize(this, types.getDeclaredType(particularElement(((ClassLangModel) aClass)), typeMirrors));
   }

   @Override
   public InterfaceLangModel withGenerics(Interface anInterface, Shadow... generics)
   {
      List<? extends Generic> generics1 = requestOrThrow(anInterface, INTERFACE_GET_GENERICS);
      if (generics.length == 0 || generics1.size() != generics.length)
      {
         throw new IllegalArgumentException(requestOrThrow(anInterface, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME) +
                                            " has " +
                                            generics1.size() +
                                            " generics. " +
                                            generics.length +
                                            " are provided");
      }
      TypeMirror[] typeMirrors = stream(generics)
            .map(ShadowLangModel.class::cast)
            .map(LangModelAdapter::particularType)
            .toArray(TypeMirror[]::new);

      return LangModelAdapter.generalize(this, types.getDeclaredType(particularElement(((InterfaceLangModel) anInterface)), typeMirrors));
   }

   @Override
   public RecordLangModel withGenerics(Record aRecord, Shadow... generics)
   {
      if (generics.length == 0 || query(aRecord).getGenerics().size() != generics.length)
      {
         throw new IllegalArgumentException(requestOrThrow( aRecord, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME) +
                                            " has " +
                                            query(aRecord).getGenerics().size() +
                                            " generics. " +
                                            generics.length +
                                            " are provided");
      }
      TypeMirror[] typeMirrors = stream(generics)
            .map(ShadowLangModel.class::cast)
            .map(LangModelAdapter::particularType)
            .toArray(TypeMirror[]::new);

      return LangModelAdapter.generalize(this, types.getDeclaredType(particularElement(((RecordLangModel) aRecord)), typeMirrors));
   }

   @Override
   public ArrayLangModel asArray(Array array)
   {
      return LangModelAdapter.generalize(this, types.getArrayType(particularType((ArrayLangModel) array)));
   }

   @Override
   public ArrayLangModel asArray(Primitive primitive)
   {
      return LangModelAdapter.generalize(this, types.getArrayType(particularType((PrimitiveLangModel) primitive)));
   }

   @Override
   public ArrayLangModel asArray(Declared declared)
   {
      return LangModelAdapter.generalize(this, types.getArrayType(particularType((DeclaredLangModel) declared)));
   }

   @Override
   public ArrayLangModel asArray(Intersection intersection)
   {
      return LangModelAdapter.generalize(this, types.getArrayType(particularType((IntersectionLangModel) intersection)));
   }

   @Override
   public WildcardLangModel asExtendsWildcard(Array array)
   {
      return LangModelAdapter.generalize(this, types.getWildcardType(particularType((ArrayLangModel) array), null));
   }

   @Override
   public WildcardLangModel asSuperWildcard(Array array)
   {
      return LangModelAdapter.generalize(this, types.getWildcardType(null, particularType((ArrayLangModel) array)));
   }

   @Override
   public WildcardLangModel asExtendsWildcard(Declared declared)
   {
      return LangModelAdapter.generalize(this, types.getWildcardType(particularType((DeclaredLangModel) declared), null));
   }

   @Override
   public WildcardLangModel asSuperWildcard(Declared declared)
   {
      return LangModelAdapter.generalize(this, types.getWildcardType(null, particularType((DeclaredLangModel) declared)));
   }

   @Override
   public Types getTypes()
   {
      return types;
   }

   @Override
   public Elements getElements()
   {
      return elements;
   }
}
