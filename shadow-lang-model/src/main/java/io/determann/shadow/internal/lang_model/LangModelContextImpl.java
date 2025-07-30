package io.determann.shadow.internal.lang_model;

import io.determann.shadow.api.C;
import io.determann.shadow.api.Modifier;
import io.determann.shadow.api.lang_model.Constants;
import io.determann.shadow.api.lang_model.ContextImplementation;
import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.api.lang_model.adapter.Adapters;
import io.determann.shadow.api.query.Implementation;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static io.determann.shadow.api.lang_model.adapter.Adapters.adapt;
import static io.determann.shadow.api.query.Operations.PACKAGE_GET_DECLARED_LIST;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static java.util.Optional.ofNullable;

public class LangModelContextImpl implements LM.Context,
                                             ContextImplementation
{
   public static final String IMPLEMENTATION_NAME = "io.determann.shadow-lang-model";
   private final Types types;
   private final Elements elements;
   private final Implementation implementation;

   public LangModelContextImpl(Types types, Elements elements)
   {
      this.types = types;
      this.elements = elements;
      implementation = new LangModelImplementation(IMPLEMENTATION_NAME, this);
   }

   @Override
   public Implementation getImplementation()
   {
      return implementation;
   }

   @Override
   public List<LM.Module> getModules()
   {
      return elements.getAllModuleElements()
                     .stream()
                     .map(moduleElement -> Adapters.adapt(this, moduleElement))
                     .toList();
   }

   @Override
   public Optional<LM.Module> getModule(String name)
   {
      return ofNullable(elements.getModuleElement(name))
            .map(moduleElement -> Adapters.adapt(this, moduleElement));
   }
   
   @Override
   public LM.Module getModuleOrThrow(String name)
   {
      return getModule(name).orElseThrow();
   }

   @Override
   public List<LM.Package> getPackage(String qualifiedName)
   {
      return elements.getAllPackageElements(qualifiedName)
                     .stream()
                     .map(packageElement -> Adapters.adapt(this, packageElement))
                     .toList();
   }

   @Override
   public List<LM.Package> getPackages()
   {
      return elements.getAllModuleElements()
                     .stream()
                     .flatMap(moduleElement -> moduleElement.getEnclosedElements().stream())
                     .map(PackageElement.class::cast)
                     .map(packageElement -> Adapters.adapt(this, packageElement))
                     .toList();
   }

   @Override
   public Optional<LM.Package> getPackage(String qualifiedModuleName, String qualifiedPackageName)
   {
      return getPackage(getModuleOrThrow(qualifiedModuleName), qualifiedPackageName);
   }

   @Override
   public LM.Package getPackageOrThrow(String qualifiedModuleName, String qualifiedPackageName)
   {
      return getPackage(qualifiedModuleName, qualifiedPackageName).orElseThrow();
   }

   @Override
   public Optional<LM.Package> getPackage(C.Module module, String qualifiedPackageName)
   {
      return ofNullable(elements.getPackageElement(adapt((LM.Module) module).toModuleElement(), qualifiedPackageName))
            .map(packageElement -> Adapters.adapt(this, packageElement));
   }

   @Override
   public LM.Package getPackageOrThrow(C.Module module, String qualifiedPackageName)
   {
      return getPackage(module, qualifiedPackageName).orElseThrow();
   }

   @Override
   public Optional<LM.Declared> getDeclared(String qualifiedName)
   {
      return ofNullable(elements.getTypeElement(qualifiedName))
            .map(typeElement -> Adapters.adapt(this, typeElement));
   }

   @Override
   public List<LM.Declared> getDeclared()
   {
      return getPackages()
            .stream()
            .flatMap(packageType -> requestOrThrow(packageType, PACKAGE_GET_DECLARED_LIST) .stream())
            .map(LM.Declared.class::cast)
            .toList();
   }

   @Override
   public Constants getConstants()
   {
      return new LangModelConstantsImpl(this);
   }

   public static Set<Modifier> getModifiers(Element element)
   {
      Set<Modifier> result = element.getModifiers().stream().map(LangModelContextImpl::mapModifier).collect(Collectors.toSet());
      if ((element.getKind().isExecutable() || element.getKind().isDeclaredType() || element.getKind().isVariable()) &&
          !result.contains(Modifier.PUBLIC) &&
          !result.contains(Modifier.PROTECTED) &&
          !result.contains(Modifier.PRIVATE))
      {
         result.add(Modifier.PACKAGE_PRIVATE);
      }
      return Collections.unmodifiableSet(result);
   }

   private static Modifier mapModifier(javax.lang.model.element.Modifier modifier)
   {
      return switch (modifier)
      {
         case PUBLIC -> Modifier.PUBLIC;
         case PROTECTED -> Modifier.PROTECTED;
         case PRIVATE -> Modifier.PRIVATE;
         case ABSTRACT -> Modifier.ABSTRACT;
         case STATIC -> Modifier.STATIC;
         case SEALED -> Modifier.SEALED;
         case NON_SEALED -> Modifier.NON_SEALED;
         case FINAL -> Modifier.FINAL;
         case STRICTFP -> Modifier.STRICTFP;
         case DEFAULT -> Modifier.DEFAULT;
         case TRANSIENT -> Modifier.TRANSIENT;
         case VOLATILE -> Modifier.VOLATILE;
         case SYNCHRONIZED -> Modifier.SYNCHRONIZED;
         case NATIVE -> Modifier.NATIVE;
      };
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
