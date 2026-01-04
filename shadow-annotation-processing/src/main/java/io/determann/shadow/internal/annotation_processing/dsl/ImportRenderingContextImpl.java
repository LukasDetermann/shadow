package io.determann.shadow.internal.annotation_processing.dsl;

import io.determann.shadow.api.annotation_processing.dsl.Dsl;
import io.determann.shadow.api.annotation_processing.dsl.RenderingConfiguration;
import io.determann.shadow.api.annotation_processing.dsl.import_.ImportRenderable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static java.util.Objects.requireNonNull;

public class ImportRenderingContextImpl
{
   private static final String JAVA_LANG = "java.lang";

   private final HashMap<String, QualifiedName> imports = new HashMap<>();
   private final Set<String> noPackage = new HashSet<>();
   private final Set<String> samePackage = new HashSet<>();
   private final Set<String> javaLangNoImport = new HashSet<>();
   private final RenderingConfiguration configuration;

   private String currentPackageName = null;

   ImportRenderingContextImpl(RenderingConfiguration configuration)
   {
      this.configuration = configuration;
   }

   public String renderName(@Nullable String packageName, @NotNull String simpleName)
   {
      requireNonNull(simpleName);

      // 0. disabled
      if (!configuration.isAutoImportEnabled())
      {
         if (packageName == null)
         {
            return simpleName;
         }
         return packageName + '.' + simpleName;
      }

      QualifiedName imported = imports.get(simpleName);

      // 1. no package
      if (packageName == null)
      {
         // 1.1 known type
         if (noPackage.contains(simpleName))
         {
            return simpleName;
         }
         // 1.2 collides with java lang type
         if (javaLangNoImport.contains(simpleName))
         {
            throw new IllegalStateException(javaLangMsg(simpleName));
         }
         // 1.3 collides with imported type
         if (imported != null)
         {
            throw new IllegalStateException(importedMsg(simpleName, imported));
         }
         // 1.4 collides with type from current package
         if (samePackage.contains(simpleName))
         {
            throw new IllegalStateException(samePackageMsg(simpleName, new QualifiedName(currentPackageName, simpleName)));
         }
         // 1.4 simple name without package
         noPackage.add(simpleName);
         return simpleName;
      }

      QualifiedName name = new QualifiedName(packageName, simpleName);

      // 2. same package
      if (currentPackageName != null && currentPackageName.equals(packageName))
      {
         // 2.1 known type
         if (samePackage.contains(simpleName))
         {
            return simpleName;
         }
         // 2.2 collides with javaLang imported type
         if (javaLangNoImport.contains(simpleName))
         {
            return name.qualifiedName();
         }
         // 2.3 collides with imported type
         if (imported != null)
         {
            return name.qualifiedName();
         }
         // 2.4 collides with noPackage
         if (noPackage.contains(simpleName))
         {
            return name.qualifiedName();
         }
         // 2.5 simple type from same package
         samePackage.add(simpleName);
         return simpleName;
      }

      // 3. java.lang
      if (JAVA_LANG.equals(packageName))
      {
         // 3.1 known type without import
         if (javaLangNoImport.contains(simpleName))
         {
            return name.typeName();
         }
         // 3.2 collides with imported
         if (imported != null)
         {
            return name.qualifiedName();
         }
         // 3.3 collides with noPackage
         if (noPackage.contains(simpleName))
         {
            return name.qualifiedName();
         }
         // 3.4 collides with samePackage
         if (samePackage.contains(simpleName))
         {
            return name.qualifiedName();
         }
         // 3.5 new java.lang type
         javaLangNoImport.add(simpleName);
         return name.typeName();
      }

      // 4. normal qualified name
      // 4.1 known simple name
      if (imported != null)
      {
         // 4.2 with import
         if (imported.equals(name))
         {
            return name.typeName();
         }
         // 4.2 without import
         return name.qualifiedName();
      }
      // 4.3 new qualified type
      imports.put(simpleName, name);
      return name.typeName();
   }

   void setCurrentPackageName(String currentPackageName)
   {
      this.currentPackageName = currentPackageName;
   }

   private static String samePackageMsg(String simpleName, QualifiedName imported)
   {
      return "Cannot use type from unnamed package \"" +
             simpleName +
             "\" it clashes with the used type from the same package \"" +
             imported.qualifiedName() +
             "\". Use RenderingContextBuilder#withoutAutomaticImports to disable auto importing.";
   }

   private static String importedMsg(String simpleName, QualifiedName imported)
   {
      return "Cannot use type from unnamed package \"" +
             simpleName +
             "\" it clashes with the already imported type \"" +
             imported.qualifiedName() +
             "\". Use RenderingContextBuilder#withoutAutomaticImports to disable auto importing.";
   }

   private static String javaLangMsg(String simpleName)
   {
      return "Cannot use type from unnamed package \"" +
             simpleName +
             "\" it clashes with the already used type \"java.lang." +
             simpleName +
             "\". Use RenderingContextBuilder#withoutAutomaticImports to disable auto importing.";
   }

   public List<ImportRenderable> getImports()
   {
      if (!configuration.isAutoImportEnabled())
      {
         return Collections.emptyList();
      }

      return imports.values()
                    .stream()
                    .map(QualifiedName::importableName)
                    .distinct()
                    .map(Dsl::import_)
                    .toList();
   }
}
