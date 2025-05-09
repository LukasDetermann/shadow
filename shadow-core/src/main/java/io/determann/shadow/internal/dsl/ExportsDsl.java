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
import static io.determann.shadow.internal.dsl.DslSupport.addArray;
import static io.determann.shadow.internal.dsl.DslSupport.setType;

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
      return setType(new ExportsDsl(this), packageName, (exportsDsl, s) -> exportsDsl.packageName = s);
   }

   @Override
   public ExportsTargetStep package_(C_Package aPackage)
   {
      return setType(new ExportsDsl(this),
                     aPackage,
                     (cPackage) -> requestOrThrow(cPackage, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME),
                     (exportsDsl, s) -> exportsDsl.packageName = s);
   }

   @Override
   public ExportsTargetStep to(String... moduleNames)
   {
      return addArray(new ExportsDsl(this), moduleNames, exportsDsl -> exportsDsl.to::add);
   }

   @Override
   public ExportsTargetStep to(C_Module... modules)
   {
      return addArray(new ExportsDsl(this),
                      modules,
                      (module) -> requestOrThrow(module, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME),
                      exportsDsl -> exportsDsl.to::add);
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
