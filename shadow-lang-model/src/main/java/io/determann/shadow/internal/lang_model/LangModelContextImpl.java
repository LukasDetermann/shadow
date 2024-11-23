package io.determann.shadow.internal.lang_model;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.lang_model.LM_Adapter;
import io.determann.shadow.api.lang_model.LM_Constants;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.LM_ContextImplementation;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Module;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Package;
import io.determann.shadow.api.lang_model.shadow.type.LM_Declared;
import io.determann.shadow.api.shadow.structure.C_Module;

import javax.lang.model.element.PackageElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.Operations.PACKAGE_GET_DECLARED_LIST;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.lang_model.LM_Adapter.generalizePackage;
import static io.determann.shadow.api.lang_model.LM_Adapter.particularElement;
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
