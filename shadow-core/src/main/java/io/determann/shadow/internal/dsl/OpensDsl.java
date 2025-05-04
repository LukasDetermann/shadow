package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.opens.OpensPackageStep;
import io.determann.shadow.api.dsl.opens.OpensTargetStep;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.structure.C_Module;
import io.determann.shadow.api.shadow.structure.C_Package;

import java.util.ArrayList;
import java.util.List;

import static io.determann.shadow.api.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.internal.dsl.DslSupport.addStrings;
import static io.determann.shadow.internal.dsl.DslSupport.setString;

public class OpensDsl
      implements OpensPackageStep,
                 OpensTargetStep
{
   private String packageName;
   private final List<String> to = new ArrayList<>();

   public OpensDsl()
   {
   }

   private OpensDsl(OpensDsl other)
   {
      this.packageName = other.packageName;
      this.to.addAll(other.to);
   }

   @Override
   public OpensTargetStep package_(String packageName)
   {
      return setString(new OpensDsl(this),
                       (opensDsl, s) -> opensDsl.packageName = s,
                       packageName);
   }

   @Override
   public OpensTargetStep package_(C_Package aPackage)
   {
      return setString(new OpensDsl(this),
                       (opensDsl, s) -> opensDsl.packageName = s,
                       (cPackage) -> requestOrThrow(cPackage, NAMEABLE_GET_NAME),
                       aPackage);
   }

   @Override
   public OpensTargetStep to(String... moduleNames)
   {
      return addStrings(new OpensDsl(this),
                 exportsDsl -> exportsDsl.to::add,
                 moduleNames);
   }

   @Override
   public OpensTargetStep to(C_Module... modules)
   {
      return addStrings(new OpensDsl(this),
                        opensDsl -> opensDsl.to::add,
                        module -> requestOrThrow(module, NAMEABLE_GET_NAME),
                        modules);
   }

   @Override
   public String render(RenderingContext renderingContext)
   {
      StringBuilder sb = new StringBuilder();
      sb.append("opens ");
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
