package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.dsl.import_.ImportRenderable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class ImportRenderingContextImpl
{
   private static final String JAVA_LANG = "java.lang";

   private final HashMap<String, QualifiedName> imports = new HashMap<>();

   private boolean automaticImports = true;

   ImportRenderingContextImpl(){}

   ImportRenderingContextImpl(ImportRenderingContextImpl other)
   {
      this.automaticImports = other.automaticImports;
   }

   public String renderName(String packageName, String simpleName)
   {
      if (!automaticImports)
      {
         if (packageName == null)
         {
            return simpleName;
         }
         return packageName + '.' + simpleName;
      }

      requireNonNull(simpleName);

      //in case there is a class without package and the same simple name as a java.lang class this breaks
      if (packageName == null || JAVA_LANG.equals(packageName))
      {
         return simpleName;
      }
      QualifiedName name = new QualifiedName(packageName, simpleName);

      QualifiedName importedName = imports.get(simpleName);
      if (importedName == null)
      {
         imports.put(simpleName, name);
         return name.simpleName();
      }
      if (importedName.equals(name))
      {
         return name.simpleName();
      }
      return name.qualifiedName();
   }

   public List<ImportRenderable> getImports()
   {
      if (!automaticImports)
      {
         return Collections.emptyList();
      }
      return imports.values()
                    .stream()
                    .map(qualifiedName -> Dsl.import_(qualifiedName.qualifiedName()))
                    .toList();
   }

   void disableAutomaticImports()
   {
      this.automaticImports = false;
   }

   record QualifiedName(String packageName,
                        String simpleName)
   {

      String qualifiedName()
      {
         return packageName + '.' + simpleName;
      }
   }
}
