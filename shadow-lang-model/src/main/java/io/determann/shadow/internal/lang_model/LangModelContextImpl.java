package io.determann.shadow.internal.lang_model;

import io.determann.shadow.api.Provider;
import io.determann.shadow.api.lang_model.LM_Adapter;
import io.determann.shadow.api.lang_model.LM_Constants;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.LM_ContextImplementation;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Module;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Package;
import io.determann.shadow.api.lang_model.shadow.structure.LM_RecordComponent;
import io.determann.shadow.api.lang_model.shadow.type.*;
import io.determann.shadow.api.lang_model.shadow.type.primitive.LM_Primitive;
import io.determann.shadow.api.shadow.structure.C_Field;
import io.determann.shadow.api.shadow.structure.C_Module;
import io.determann.shadow.api.shadow.structure.C_Parameter;
import io.determann.shadow.api.shadow.structure.C_RecordComponent;
import io.determann.shadow.api.shadow.type.*;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;
import io.determann.shadow.internal.lang_model.shadow.structure.RecordComponentImpl;

import javax.lang.model.element.PackageElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.lang_model.LM_Adapter.*;
import static io.determann.shadow.api.lang_model.LM_Queries.query;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

public class LangModelContextImpl implements LM_Context,
                                             LM_ContextImplementation
{
   private final Types types;
   private final Elements elements;

   public LangModelContextImpl(Types types, Elements elements)
   {
      this.types = types;
      this.elements = elements;
   }

   @Override
   public List<LM_Module> getModules()
   {
      return elements.getAllModuleElements()
                     .stream()
                     .map(moduleElement -> LM_Adapter.generalize(this, moduleElement))
                     .toList();
   }

   @Override
   public Optional<LM_Module> getModule(String name)
   {
      return ofNullable(elements.getModuleElement(name))
            .map(moduleElement -> LM_Adapter.generalize(this, moduleElement));
   }
   
   @Override
   public LM_Module getModuleOrThrow(String name)
   {
      return getModule(name).orElseThrow();
   }

   @Override
   public List<LM_Package> getPackages(String qualifiedName)
   {
      return elements.getAllPackageElements(qualifiedName)
                     .stream()
                     .map(packageElement -> generalizePackage(this, packageElement))
                     .toList();
   }

   @Override
   public List<LM_Package> getPackages()
   {
      return elements.getAllModuleElements()
                     .stream()
                     .flatMap(moduleElement -> moduleElement.getEnclosedElements().stream())
                     .map(PackageElement.class::cast)
                     .map(packageElement -> generalizePackage(this, packageElement))
                     .toList();
   }

   @Override
   public Optional<LM_Package> getPackage(String qualifiedModuleName, String qualifiedPackageName)
   {
      return getPackage(getModuleOrThrow(qualifiedModuleName), qualifiedPackageName);
   }

   @Override
   public LM_Package getPackageOrThrow(String qualifiedModuleName, String qualifiedPackageName)
   {
      return getPackage(qualifiedModuleName, qualifiedPackageName).orElseThrow();
   }

   @Override
   public Optional<LM_Package> getPackage(C_Module module, String qualifiedPackageName)
   {
      return ofNullable(elements.getPackageElement(particularElement((LM_Module) module), qualifiedPackageName))
            .map(packageElement -> generalizePackage(this, packageElement));
   }

   @Override
   public LM_Package getPackageOrThrow(C_Module module, String qualifiedPackageName)
   {
      return getPackage(module, qualifiedPackageName).orElseThrow();
   }

   @Override
   public Optional<LM_Declared> getDeclared(String qualifiedName)
   {
      return ofNullable(elements.getTypeElement(qualifiedName))
            .map(typeElement -> LM_Adapter.generalize(this, typeElement));
   }

   @Override
   public List<LM_Declared> getDeclared()
   {
      return getPackages()
            .stream()
            .flatMap(packageType -> requestOrThrow(packageType, PACKAGE_GET_DECLARED_LIST) .stream())
            .map(LM_Declared.class::cast)
            .toList();
   }

   @Override
   public LM_Constants getConstants()
   {
      return new LangModelConstantsImpl(this);
   }


   @Override
   public LM_Class erasure(C_Class aClass)
   {
      return erasureImpl(particularType((LM_Declared) aClass));
   }

   @Override
   public LM_Interface erasure(C_Interface anInterface)
   {
      return erasureImpl(particularType((LM_Declared) anInterface));
   }

   @Override
   public LM_Record erasure(C_Record aRecord)
   {
      return erasureImpl(particularType((LM_Declared) aRecord));
   }

   @Override
   public LM_Array erasure(C_Array array)
   {
      return erasureImpl(particularType((LM_Declared) array));
   }

   @Override
   public LM_Type erasure(C_Wildcard wildcard)
   {
      return erasureImpl(particularType((LM_Declared) wildcard));
   }

   @Override
   public LM_Type erasure(C_Generic generic)
   {
      return erasureImpl(particularType((LM_Declared) generic));
   }

   @Override
   public LM_Type erasure(C_Intersection intersection)
   {
      return erasureImpl(particularType((LM_Declared) intersection));
   }

   @Override
   public LM_RecordComponent erasure(C_RecordComponent recordComponent)
   {
      return erasureImpl(((RecordComponentImpl) recordComponent).getMirror());
   }

   @Override
   public LM_Type erasure(C_Parameter parameter)
   {
      return erasureImpl(particularType((LM_Declared) parameter));
   }

   @Override
   public LM_Type erasure(C_Field field)
   {
      return erasureImpl(particularType((LM_Declared) field));
   }

   private <S extends C_Type> S erasureImpl(TypeMirror typeMirror)
   {
      return LM_Adapter.generalize(this, types.erasure(typeMirror));
   }

   @Override
   public LM_Class interpolateGenerics(C_Class aClass)
   {
      return LM_Adapter.generalize(this, types.capture(particularType((LM_Declared) aClass)));
   }

   @Override
   public LM_Interface interpolateGenerics(C_Interface anInterface)
   {
      return LM_Adapter.generalize(this, types.capture(particularType((LM_Declared) anInterface)));
   }

   @Override
   public LM_Record interpolateGenerics(C_Record aRecord)
   {
      return LM_Adapter.generalize(this, types.capture(particularType((LM_Declared) aRecord)));
   }

   @Override
   public LM_Class withGenerics(C_Class aClass, C_Type... generics)
   {
      List<? extends C_Generic> generics1 = requestOrThrow(aClass, CLASS_GET_GENERICS);
      if (generics.length == 0 || generics1.size() != generics.length)
      {
         throw new IllegalArgumentException(requestOrThrow(aClass, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME) +
                                            " has " +
                                            generics1.size() +
                                            " generics. " +
                                            generics.length +
                                            " are provided");
      }
      Optional<C_Declared> outerType = Provider.requestOrEmpty(aClass, CLASS_GET_OUTER_TYPE);
      if (outerType.isPresent() &&
          (outerType.get() instanceof C_Interface anInterface &&
           !requestOrThrow(anInterface, INTERFACE_GET_GENERICS).isEmpty() ||
           outerType.get() instanceof C_Class aClass1 && !requestOrThrow(aClass1, CLASS_GET_GENERIC_TYPES).isEmpty()))
      {
         throw new IllegalArgumentException("cant add generics to " +
                                            requestOrThrow(aClass, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME) +
                                            " when the class is not static and the outer class has generics");
      }
      TypeMirror[] typeMirrors = stream(generics)
            .map(LM_Type.class::cast)
            .map(LM_Adapter::particularType)
            .toArray(TypeMirror[]::new);

      return LM_Adapter.generalize(this, types.getDeclaredType(particularElement(((LM_Class) aClass)), typeMirrors));
   }

   @Override
   public LM_Interface withGenerics(C_Interface anInterface, C_Type... generics)
   {
      List<? extends C_Generic> generics1 = requestOrThrow(anInterface, INTERFACE_GET_GENERICS);
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
            .map(LM_Type.class::cast)
            .map(LM_Adapter::particularType)
            .toArray(TypeMirror[]::new);

      return LM_Adapter.generalize(this, types.getDeclaredType(particularElement(((LM_Interface) anInterface)), typeMirrors));
   }

   @Override
   public LM_Record withGenerics(C_Record aRecord, C_Type... generics)
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
            .map(LM_Type.class::cast)
            .map(LM_Adapter::particularType)
            .toArray(TypeMirror[]::new);

      return LM_Adapter.generalize(this, types.getDeclaredType(particularElement(((LM_Record) aRecord)), typeMirrors));
   }

   @Override
   public LM_Array asArray(C_Array array)
   {
      return LM_Adapter.generalize(this, types.getArrayType(particularType((LM_Array) array)));
   }

   @Override
   public LM_Array asArray(C_Primitive primitive)
   {
      return LM_Adapter.generalize(this, types.getArrayType(particularType((LM_Primitive) primitive)));
   }

   @Override
   public LM_Array asArray(C_Declared declared)
   {
      return LM_Adapter.generalize(this, types.getArrayType(particularType((LM_Declared) declared)));
   }

   @Override
   public LM_Array asArray(C_Intersection intersection)
   {
      return LM_Adapter.generalize(this, types.getArrayType(particularType((LM_Intersection) intersection)));
   }

   @Override
   public LM_Wildcard asExtendsWildcard(C_Array array)
   {
      return LM_Adapter.generalize(this, types.getWildcardType(particularType((LM_Array) array), null));
   }

   @Override
   public LM_Wildcard asSuperWildcard(C_Array array)
   {
      return LM_Adapter.generalize(this, types.getWildcardType(null, particularType((LM_Array) array)));
   }

   @Override
   public LM_Wildcard asExtendsWildcard(C_Declared declared)
   {
      return LM_Adapter.generalize(this, types.getWildcardType(particularType((LM_Declared) declared), null));
   }

   @Override
   public LM_Wildcard asSuperWildcard(C_Declared declared)
   {
      return LM_Adapter.generalize(this, types.getWildcardType(null, particularType((LM_Declared) declared)));
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
