package io.determann.shadow.internal.lang_model;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.lang_model.LM_Constants;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.LM_ContextImplementation;
import io.determann.shadow.api.lang_model.adapter.LM_Adapters;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Module;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Package;
import io.determann.shadow.api.lang_model.shadow.type.LM_Declared;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.structure.C_Module;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static io.determann.shadow.api.Operations.PACKAGE_GET_DECLARED_LIST;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.lang_model.adapter.LM_Adapters.adapt;
import static java.util.Optional.ofNullable;

public class LangModelContextImpl implements LM_Context,
                                             LM_ContextImplementation
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
   public List<LM_Module> getModules()
   {
      return elements.getAllModuleElements()
                     .stream()
                     .map(moduleElement -> LM_Adapters.adapt(this, moduleElement))
                     .toList();
   }

   @Override
   public Optional<LM_Module> getModule(String name)
   {
      return ofNullable(elements.getModuleElement(name))
            .map(moduleElement -> LM_Adapters.adapt(this, moduleElement));
   }
   
   @Override
   public LM_Module getModuleOrThrow(String name)
   {
      return getModule(name).orElseThrow();
   }

   @Override
   public List<LM_Package> getPackage(String qualifiedName)
   {
      return elements.getAllPackageElements(qualifiedName)
                     .stream()
                     .map(packageElement -> LM_Adapters.adapt(this, packageElement))
                     .toList();
   }

   @Override
   public List<LM_Package> getPackages()
   {
      return elements.getAllModuleElements()
                     .stream()
                     .flatMap(moduleElement -> moduleElement.getEnclosedElements().stream())
                     .map(PackageElement.class::cast)
                     .map(packageElement -> LM_Adapters.adapt(this, packageElement))
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
      return ofNullable(elements.getPackageElement(adapt((LM_Module) module).toModuleElement(), qualifiedPackageName))
            .map(packageElement -> LM_Adapters.adapt(this, packageElement));
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
            .map(typeElement -> LM_Adapters.adapt(this, typeElement));
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

   public static Set<C_Modifier> getModifiers(Element element)
   {
      Set<C_Modifier> result = element.getModifiers().stream().map(LangModelContextImpl::mapModifier).collect(Collectors.toSet());
      if ((element.getKind().isExecutable() || element.getKind().isDeclaredType() || element.getKind().isVariable()) &&
          !result.contains(C_Modifier.PUBLIC) &&
          !result.contains(C_Modifier.PROTECTED) &&
          !result.contains(C_Modifier.PRIVATE))
      {
         result.add(C_Modifier.PACKAGE_PRIVATE);
      }
      return Collections.unmodifiableSet(result);
   }

   private static C_Modifier mapModifier(Modifier modifier)
   {
      return switch (modifier)
      {
         case PUBLIC -> C_Modifier.PUBLIC;
         case PROTECTED -> C_Modifier.PROTECTED;
         case PRIVATE -> C_Modifier.PRIVATE;
         case ABSTRACT -> C_Modifier.ABSTRACT;
         case STATIC -> C_Modifier.STATIC;
         case SEALED -> C_Modifier.SEALED;
         case NON_SEALED -> C_Modifier.NON_SEALED;
         case FINAL -> C_Modifier.FINAL;
         case STRICTFP -> C_Modifier.STRICTFP;
         case DEFAULT -> C_Modifier.DEFAULT;
         case TRANSIENT -> C_Modifier.TRANSIENT;
         case VOLATILE -> C_Modifier.VOLATILE;
         case SYNCHRONIZED -> C_Modifier.SYNCHRONIZED;
         case NATIVE -> C_Modifier.NATIVE;
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
