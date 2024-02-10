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
import static io.determann.shadow.api.lang_model.LangModelAdapter.*;
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
                     .map(moduleElement -> LangModelAdapter.<Module>getShadow(this, moduleElement))
                     .toList();
   }

   @Override
   public Optional<Module> getModule(String name)
   {
      return ofNullable(elements.getModuleElement(name))
            .map(moduleElement -> getShadow(this, moduleElement));
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
                     .map(packageElement -> LangModelAdapter.<Package>getShadow(this, packageElement))
                     .toList();
   }

   @Override
   public List<Package> getPackages()
   {
      return elements.getAllModuleElements()
                     .stream()
                     .flatMap(moduleElement -> moduleElement.getEnclosedElements().stream())
                     .map(packageElement -> LangModelAdapter.<Package>getShadow(this, packageElement))
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
                                          .getPackageElement(getElement(module),
                                                             qualifiedPackageName))
            .map(packageElement -> getShadow(this, packageElement));
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
            .map(typeElement -> getShadow(this, typeElement));
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
      return erasureImpl(getType(aClass));
   }

   @Override
   public Interface erasure(Interface anInterface)
   {
      return erasureImpl(getType(anInterface));
   }

   @Override
   public Record erasure(Record aRecord)
   {
      return erasureImpl(getType(aRecord));
   }

   @Override
   public Array erasure(Array array)
   {
      return erasureImpl(getType(array));
   }

   @Override
   public Shadow erasure(Wildcard wildcard)
   {
      return erasureImpl(getType(wildcard));
   }

   @Override
   public Shadow erasure(Generic generic)
   {
      return erasureImpl(getType(generic));
   }

   @Override
   public Shadow erasure(Intersection intersection)
   {
      return erasureImpl(getType(intersection));
   }

   @Override
   public RecordComponent erasure(RecordComponent recordComponent)
   {
      return erasureImpl(getType(recordComponent));
   }

   @Override
   public Shadow erasure(Parameter parameter)
   {
      return erasureImpl(getType(parameter));
   }

   @Override
   public Shadow erasure(Field field)
   {
      return erasureImpl(getType(field));
   }

   private <S extends Shadow> S erasureImpl(TypeMirror typeMirror)
   {
      return getShadow(this, types.erasure(typeMirror));
   }

   @Override
   public Class interpolateGenerics(Class aClass)
   {
      return getShadow(this, types.capture(getType(aClass)));
   }

   @Override
   public Interface interpolateGenerics(Interface anInterface)
   {
      return getShadow(this, types.capture(getType(anInterface)));
   }

   @Override
   public Record interpolateGenerics(Record aRecord)
   {
      return getShadow(this, types.capture(getType(aRecord)));
   }

   @Override
   public Class withGenerics(Class aClass, Shadow... generics)
   {
      if (generics.length == 0 || aClass.getGenerics().size() != generics.length)
      {
         throw new IllegalArgumentException(aClass.getQualifiedName() +
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
                                            aClass.getQualifiedName() +
                                            " when the class is not static and the outer class has generics");
      }
      TypeMirror[] typeMirrors = stream(generics)
            .map(LangModelAdapter::getType)
            .toArray(TypeMirror[]::new);

      return getShadow(this, types.getDeclaredType(getElement(aClass), typeMirrors));
   }

   @Override
   public Interface withGenerics(Interface anInterface, Shadow... generics)
   {
      if (generics.length == 0 || anInterface.getGenerics().size() != generics.length)
      {
         throw new IllegalArgumentException(anInterface.getQualifiedName() +
                                            " has " +
                                            anInterface.getGenerics().size() +
                                            " generics. " +
                                            generics.length +
                                            " are provided");
      }
      TypeMirror[] typeMirrors = stream(generics)
            .map(LangModelAdapter::getType)
            .toArray(TypeMirror[]::new);

      return getShadow(this, types.getDeclaredType(getElement(anInterface), typeMirrors));
   }

   @Override
   public Record withGenerics(Record aRecord, Shadow... generics)
   {
      if (generics.length == 0 || aRecord.getGenerics().size() != generics.length)
      {
         throw new IllegalArgumentException(aRecord.getQualifiedName() +
                                            " has " +
                                            aRecord.getGenerics().size() +
                                            " generics. " +
                                            generics.length +
                                            " are provided");
      }
      TypeMirror[] typeMirrors = stream(generics)
            .map(LangModelAdapter::getType)
            .toArray(TypeMirror[]::new);

      return getShadow(this, types.getDeclaredType(getElement(aRecord), typeMirrors));
   }

   @Override
   public Array asArray(Array array)
   {
      return getShadow(this, types.getArrayType(getType(array)));
   }

   @Override
   public Array asArray(Primitive primitive)
   {
      return getShadow(this, types.getArrayType(getType(primitive)));
   }

   @Override
   public Array asArray(Declared declared)
   {
      return getShadow(this, types.getArrayType(getType(declared)));
   }

   @Override
   public Array asArray(Intersection intersection)
   {
      return getShadow(this, types.getArrayType(getType(intersection)));
   }

   @Override
   public Wildcard asExtendsWildcard(Array array)
   {
      return getShadow(this, types.getWildcardType(getType(array), null));
   }

   @Override
   public Wildcard asSuperWildcard(Array array)
   {
      return getShadow(this, types.getWildcardType(null, getType(array)));
   }

   @Override
   public Wildcard asExtendsWildcard(Declared declared)
   {
      return getShadow(this, types.getWildcardType(getType(declared), null));
   }

   @Override
   public Wildcard asSuperWildcard(Declared declared)
   {
      return getShadow(this, types.getWildcardType(null, getType(declared)));
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
