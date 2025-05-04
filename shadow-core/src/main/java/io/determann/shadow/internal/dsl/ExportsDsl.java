package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.exports.ExportsPackageStep;
import io.determann.shadow.api.dsl.exports.ExportsTargetStep;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.structure.C_Module;
import io.determann.shadow.api.shadow.structure.C_Package;

import java.util.ArrayList;
import java.util.List;

import static io.determann.shadow.api.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.internal.dsl.DslSupport.addStrings;
import static io.determann.shadow.internal.dsl.DslSupport.setString;

public class ExportsDsl
      implements ExportsPackageStep,
                 ExportsTargetStep
{
   private String packageName;
   private final List<String> to = new ArrayList<>();

   public ExportsDsl()
   {
   }

   private ExportsDsl(ExportsDsl other)
   {
      this.packageName = other.packageName;
      this.to.addAll(other.to);
   }

   @Override
   public ExportsTargetStep package_(String packageName)
   {
      return setString(new ExportsDsl(this),
                       (exportsDsl, s) -> exportsDsl.packageName = s,
                       packageName);
   }

   @Override
   public ExportsTargetStep package_(C_Package aPackage)
   {
      return setString(new ExportsDsl(this),
                       (exportsDsl, s) -> exportsDsl.packageName = s,
                       (cPackage) -> requestOrThrow(cPackage, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME),
                       aPackage);
   }

   @Override
   public ExportsTargetStep to(String... moduleNames)
   {
      return addStrings(new ExportsDsl(this),
                 exportsDsl -> exportsDsl.to::add,
                 moduleNames);
   }

   @Override
   public ExportsTargetStep to(C_Module... modules)
   {
      return addStrings(new ExportsDsl(this),
                 exportsDsl -> exportsDsl.to::add,
                 ( module) -> requestOrThrow(module, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME),
                 modules);
   }

   @Override
   public String render(RenderingContext renderingContext)
   {
      StringBuilder sb = new StringBuilder();
      sb.append("exports ");
      sb.append(packageName);

      if (to.isEmpty())
      {
         sb.append(';');
         return sb.toString();
      }
      if (to.size() == 1)
      {
         sb.append(" to ");
         sb.append(to.get(0));
         sb.append(';');
         return sb.toString();
      }
      sb.append(" to\n");
      sb.append(String.join("\n", to));
      sb.append(';');
      return sb.toString();
   }
}
