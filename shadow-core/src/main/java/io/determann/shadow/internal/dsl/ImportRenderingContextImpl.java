package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.dsl.RenderingConfiguration;
import io.determann.shadow.api.dsl.import_.ImportRenderable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static java.util.Objects.requireNonNull;

public class ImportRenderingContextImpl
{
   private static final String JAVA_LANG = "java.lang";

   private final HashMap<String, QualifiedName> imports = new HashMap<>();
   private final Set<String> noPackage = new HashSet<>();
   private final Set<String> javaLangNoImport = new HashSet<>();
   private final RenderingConfiguration configuration;

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
         // 1.2 collides with imported type
         if (javaLangNoImport.contains(simpleName))
         {
            throw new IllegalStateException(javaLangMsg(simpleName));
         }
         // 1.3 collides with imported type
         if (imported != null)
         {
            throw new IllegalStateException(importedMsg(simpleName, imported));
         }
         // 1.4 unknown simple name
         noPackage.add(simpleName);
         return simpleName;
      }

      QualifiedName name = new QualifiedName(packageName, simpleName);

      // 2. java.lang
      if (JAVA_LANG.equals(packageName))
      {
         // 2.1 known type without import
         if (javaLangNoImport.contains(simpleName))
         {
            return name.simpleName();
         }
         // 2.2 collides with imported
         if (imported != null)
         {
            return name.qualifiedName();
         }
         // 2.3 collides with noPackage
         if (noPackage.contains(simpleName))
         {
            return name.qualifiedName();
         }
         // 2.4 new java.lang type
         javaLangNoImport.add(simpleName);
         return name.simpleName();
      }

      // 3. normal qualified name
      // 3.1 known simple name
      if (imported != null)
      {
         // 3.2 with import
         if (imported.equals(name))
         {
            return name.simpleName();
         }
         // 3.2 without import
         return name.qualifiedName();
      }
      // 3.3 new qualified type
      imports.put(simpleName, name);
      return name.simpleName();
   }

   private static String importedMsg(String simpleName, QualifiedName imported)
   {
      return "Cannot import type from unnamed package \"" +
             simpleName +
             "\" it clashes with the already imported type \"" +
             imported.qualifiedName() +
             "\". Use RenderingContextBuilder#withoutAutomaticImports to disable auto importing.";
   }

   private static String javaLangMsg(String simpleName)
   {
      return "Cannot import type from unnamed package \"" +
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
                    .map(qualifiedName -> Dsl.import_(qualifiedName.qualifiedName()))
                    .toList();
   }

   private record QualifiedName(String packageName,
                                String simpleName)
   {
      String qualifiedName()
      {
         return packageName + '.' + simpleName;
      }
   }
}
