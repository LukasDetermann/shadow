package io.determann.shadow.internal.lang_model;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelConstants;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Record;
import io.determann.shadow.api.shadow.*;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.converter.Converter.convert;
import static io.determann.shadow.api.lang_model.LangModelAdapter.particularElement;
import static io.determann.shadow.api.lang_model.LangModelAdapter.particularType;
import static io.determann.shadow.meta_meta.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.meta_meta.Provider.requestOrThrow;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

public class LangModelContextImpl implements LangModelContext
{
   private final Types types;
   private final Elements elements;


   public LangModelContextImpl(Types types, Elements elements)
   {
      this.types = types;
      this.elements = elements;
   }

   @Override
   public List<Module> getModules()
   {
      return elements.getAllModuleElements()
                     .stream()
                     .map(moduleElement -> LangModelAdapter.<Module>generalize(this, moduleElement))
                     .toList();
   }

   @Override
   public Optional<Module> getModule(String name)
   {
      return ofNullable(elements.getModuleElement(name))
            .map(moduleElement -> LangModelAdapter.generalize(this, moduleElement));
   }
   
   @Override
   public Module getModuleOrThrow(String name)
   {
      return getModule(name).orElseThrow();
   }

   @Override
   public List<Package> getPackages(String qualifiedName)
   {
      return elements.getAllPackageElements(qualifiedName)
                     .stream()
                     .map(packageElement -> LangModelAdapter.<Package>generalize(this, packageElement))
                     .toList();
   }

   @Override
   public List<Package> getPackages()
   {
      return elements.getAllModuleElements()
                     .stream()
                     .flatMap(moduleElement -> moduleElement.getEnclosedElements().stream())
                     .map(packageElement -> LangModelAdapter.<Package>generalize(this, packageElement))
                     .toList();
   }

   @Override
   public Optional<Package> getPackage(String qualifiedModuleName, String qualifiedPackageName)
   {
      return getPackage(getModuleOrThrow(qualifiedModuleName), qualifiedPackageName);
   }

   @Override
   public Package getPackageOrThrow(String qualifiedModuleName, String qualifiedPackageName)
   {
      return getPackage(qualifiedModuleName, qualifiedPackageName).orElseThrow();
   }

   @Override
   public Optional<Package> getPackage(Module module, String qualifiedPackageName)
   {
      return ofNullable(elements
                                          .getPackageElement(particularElement(module),
                                                             qualifiedPackageName))
            .map(packageElement -> LangModelAdapter.generalize(this, packageElement));
   }

   @Override
   public Package getPackageOrThrow(Module module, String qualifiedPackageName)
   {
      return getPackage(module, qualifiedPackageName).orElseThrow();
   }

   @Override
   public Optional<Declared> getDeclared(String qualifiedName)
   {
      return ofNullable(elements.getTypeElement(qualifiedName))
            .map(typeElement -> LangModelAdapter.generalize(this, typeElement));
   }

   @Override
   public List<Declared> getDeclared()
   {
      return getPackages()
            .stream()
            .flatMap(packageShadow -> packageShadow.getContent().stream())
            .toList();
   }

   @Override
   public LangModelConstants getConstants()
   {
      return new LangModelConstantsImpl(this);
   }


   @Override
   public Class erasure(Class aClass)
   {
      return erasureImpl(particularType(aClass));
   }

   @Override
   public Interface erasure(Interface anInterface)
   {
      return erasureImpl(particularType(anInterface));
   }

   @Override
   public Record erasure(Record aRecord)
   {
      return erasureImpl(particularType(aRecord));
   }

   @Override
   public Array erasure(Array array)
   {
      return erasureImpl(particularType(array));
   }

   @Override
   public Shadow erasure(Wildcard wildcard)
   {
      return erasureImpl(particularType(wildcard));
   }

   @Override
   public Shadow erasure(Generic generic)
   {
      return erasureImpl(particularType(generic));
   }

   @Override
   public Shadow erasure(Intersection intersection)
   {
      return erasureImpl(particularType(intersection));
   }

   @Override
   public RecordComponent erasure(RecordComponent recordComponent)
   {
      return erasureImpl(particularType(recordComponent));
   }

   @Override
   public Shadow erasure(Parameter parameter)
   {
      return erasureImpl(particularType(parameter));
   }

   @Override
   public Shadow erasure(Field field)
   {
      return erasureImpl(particularType(field));
   }

   private <S extends Shadow> S erasureImpl(TypeMirror typeMirror)
   {
      return LangModelAdapter.generalize(this, types.erasure(typeMirror));
   }

   @Override
   public Class interpolateGenerics(Class aClass)
   {
      return LangModelAdapter.generalize(this, types.capture(particularType(aClass)));
   }

   @Override
   public Interface interpolateGenerics(Interface anInterface)
   {
      return LangModelAdapter.generalize(this, types.capture(particularType(anInterface)));
   }

   @Override
   public Record interpolateGenerics(Record aRecord)
   {
      return LangModelAdapter.generalize(this, types.capture(particularType(aRecord)));
   }

   @Override
   public Class withGenerics(Class aClass, Shadow... generics)
   {
      if (generics.length == 0 || aClass.getGenerics().size() != generics.length)
      {
         throw new IllegalArgumentException(requestOrThrow(aClass, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME) +
                                            " has " +
                                            aClass.getGenerics().size() +
                                            " generics. " +
                                            generics.length +
                                            " are provided");
      }
      if (aClass.getOuterType().flatMap(typeMirrorShadow -> convert(typeMirrorShadow)
                      .toInterface()
                      .map(anInterface -> !anInterface.getGenerics().isEmpty())
                      .or(() -> convert(typeMirrorShadow).toClass().map(aClass1 -> !aClass1.getGenericTypes().isEmpty())))
                .orElse(false))
      {
         throw new IllegalArgumentException("cant add generics to " +
                                            requestOrThrow(aClass, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME) +
                                            " when the class is not static and the outer class has generics");
      }
      TypeMirror[] typeMirrors = stream(generics)
            .map(LangModelAdapter::particularType)
            .toArray(TypeMirror[]::new);

      return LangModelAdapter.generalize(this, types.getDeclaredType(particularElement(aClass), typeMirrors));
   }

   @Override
   public Interface withGenerics(Interface anInterface, Shadow... generics)
   {
      if (generics.length == 0 || anInterface.getGenerics().size() != generics.length)
      {
         throw new IllegalArgumentException(requestOrThrow(anInterface, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME) +
                                            " has " +
                                            anInterface.getGenerics().size() +
                                            " generics. " +
                                            generics.length +
                                            " are provided");
      }
      TypeMirror[] typeMirrors = stream(generics)
            .map(LangModelAdapter::particularType)
            .toArray(TypeMirror[]::new);

      return LangModelAdapter.generalize(this, types.getDeclaredType(particularElement(anInterface), typeMirrors));
   }

   @Override
   public Record withGenerics(Record aRecord, Shadow... generics)
   {
      if (generics.length == 0 || aRecord.getGenerics().size() != generics.length)
      {
         throw new IllegalArgumentException(requestOrThrow( aRecord, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME) +
                                            " has " +
                                            aRecord.getGenerics().size() +
                                            " generics. " +
                                            generics.length +
                                            " are provided");
      }
      TypeMirror[] typeMirrors = stream(generics)
            .map(LangModelAdapter::particularType)
            .toArray(TypeMirror[]::new);

      return LangModelAdapter.generalize(this, types.getDeclaredType(particularElement(aRecord), typeMirrors));
   }

   @Override
   public Array asArray(Array array)
   {
      return LangModelAdapter.generalize(this, types.getArrayType(particularType(array)));
   }

   @Override
   public Array asArray(Primitive primitive)
   {
      return LangModelAdapter.generalize(this, types.getArrayType(particularType(primitive)));
   }

   @Override
   public Array asArray(Declared declared)
   {
      return LangModelAdapter.generalize(this, types.getArrayType(particularType(declared)));
   }

   @Override
   public Array asArray(Intersection intersection)
   {
      return LangModelAdapter.generalize(this, types.getArrayType(particularType(intersection)));
   }

   @Override
   public Wildcard asExtendsWildcard(Array array)
   {
      return LangModelAdapter.generalize(this, types.getWildcardType(particularType(array), null));
   }

   @Override
   public Wildcard asSuperWildcard(Array array)
   {
      return LangModelAdapter.generalize(this, types.getWildcardType(null, particularType(array)));
   }

   @Override
   public Wildcard asExtendsWildcard(Declared declared)
   {
      return LangModelAdapter.generalize(this, types.getWildcardType(particularType(declared), null));
   }

   @Override
   public Wildcard asSuperWildcard(Declared declared)
   {
      return LangModelAdapter.generalize(this, types.getWildcardType(null, particularType(declared)));
   }

   public Types getTypes()
   {
      return types;
   }

   public Elements getElements()
   {
      return elements;
   }
}
