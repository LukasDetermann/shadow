package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.ModuleRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.directive.*;
import io.determann.shadow.api.shadow.structure.C_Module;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;

public class ModuleRendererImpl implements ModuleRenderer
{
   private final C_Module module;

   public ModuleRendererImpl(C_Module module)
   {
      this.module = module;
   }

   @Override
   public String declaration(RenderingContext renderingContext)
   {
      if (requestOrThrow(module, MODULE_IS_UNNAMED))
      {
         throw new IllegalArgumentException("cant render a unnamed module");
      }

      StringBuilder sb = new StringBuilder();

      sb.append(RenderingSupport.annotations(new RenderingContextWrapper(renderingContext), module, '\n'));

      if (requestOrThrow(module, MODULE_IS_OPEN))
      {
         sb.append("open ");
      }

      sb.append("module ");
      sb.append(requestOrThrow(module, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME));
      sb.append(" {\n");

      Map<Class<? extends C_Directive>, List<C_Directive>> kind = requestOrThrow(module, MODULE_GET_DIRECTIVES)
                                                       .stream()
                                                       .collect(groupingBy(directive -> switch (directive)
                                                       {
                                                          case C_Exports exports -> C_Exports.class;
                                                          case C_Opens opens -> C_Opens.class;
                                                          case C_Provides provides -> C_Provides.class;
                                                          case C_Requires requires -> C_Requires.class;
                                                          case C_Uses uses -> C_Uses.class;
                                                          default -> throw new IllegalStateException("Unexpected value: " + directive);
                                                       }));

      if (kind.containsKey(C_Requires.class))
      {
         List<C_Directive> requires = kind.get(C_Requires.class)
                                          .stream()
                                          .map(C_Requires.class::cast)
                                          .filter(requires1 -> !requestOrThrow(requestOrThrow(requires1, REQUIRES_GET_DEPENDENCY),
                                                                             QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME).equals("java.base"))
                                          .collect(Collectors.toList());
         sb.append(render(requires,
                          C_Requires.class::cast,
                          this::render));
         sb.append('\n');
      }
      if (kind.containsKey(C_Exports.class))
      {
         sb.append(render(kind.get(C_Exports.class),
                          C_Exports.class::cast,
                          this::render));
         sb.append('\n');
      }
      if (kind.containsKey(C_Opens.class))
      {
         sb.append(render(kind.get(C_Opens.class),
                          C_Opens.class::cast,
                          this::render));
         sb.append('\n');
      }
      if (kind.containsKey(C_Uses.class))
      {
         sb.append(render(kind.get(C_Uses.class),
                          C_Uses.class::cast,
                          this::render));
         sb.append('\n');
      }
      if (kind.containsKey(C_Provides.class))
      {
         sb.append(render(kind.get(C_Provides.class),
                          C_Provides.class::cast,
                          this::render));
      }

      sb.append(" }");
      sb.append('\n');

      return sb.toString();
   }

   private <DIRECTIVE> String render(List<C_Directive> directives,
                                     Function<C_Directive, DIRECTIVE> convert,
                                     Function<DIRECTIVE, String> renderer)
   {
      return directives.stream()
                       .map(convert)
                       .map(renderer)
                       .map(s -> s + ';')
                       .collect(joining("\n"))
             + '\n';
   }

   private String render(C_Requires requires)
   {
      StringBuilder sb = new StringBuilder();
      sb.append("requires ");

      if (requestOrThrow(requires, REQUIRES_IS_TRANSITIVE))
      {
         sb.append("transitive ");
      }
      if (requestOrThrow(requires, REQUIRES_IS_STATIC))
      {
         sb.append("static ");
      }
      sb.append(requestOrThrow(requestOrThrow(requires, REQUIRES_GET_DEPENDENCY), QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME));
      return sb.toString();
   }

   private String render(C_Exports exports)
   {
      StringBuilder sb = new StringBuilder();

      if (requestOrThrow(requestOrThrow(exports, EXPORTS_GET_PACKAGE), PACKAGE_IS_UNNAMED))
      {
         throw new IllegalArgumentException("cant render a unnamed packageName");
      }

      sb.append("exports ");
      sb.append(requestOrThrow(requestOrThrow(exports, EXPORTS_GET_PACKAGE), QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME));

      if (!requestOrThrow(exports, EXPORTS_TO_ALL))
      {
         sb.append(" to ");
         sb.append(requestOrThrow(exports, EXPORTS_GET_TARGET_MODULES).stream()
                          .map(module1 -> requestOrThrow(module1, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME))
                          .collect(joining(", ")));
      }

      return sb.toString();
   }

   private String render(C_Opens opens)
   {
      StringBuilder sb = new StringBuilder();

      if (requestOrThrow(requestOrThrow(opens, OPENS_GET_PACKAGE), PACKAGE_IS_UNNAMED))
      {
         throw new IllegalArgumentException("cant render a unnamed packageName");
      }

      sb.append("opens ");
      sb.append(requestOrThrow(requestOrThrow(opens, OPENS_GET_PACKAGE), QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME));

      if (!requestOrThrow(opens, OPENS_TO_ALL))
      {
         sb.append(" to ");
         sb.append(requestOrThrow(opens, OPENS_GET_TARGET_MODULES).stream()
                        .map(module1 -> requestOrThrow(module1, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME))
                        .collect(joining(", ")));
      }

      return sb.toString();
   }

   private String render(C_Uses uses)
   {
      return "uses " + requestOrThrow(requestOrThrow(uses,  USES_GET_SERVICE), QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME);
   }

   private String render(C_Provides provides)
   {
      StringBuilder sb = new StringBuilder();
      sb.append("provides ");
      sb.append(requestOrThrow(requestOrThrow(provides, PROVIDES_GET_SERVICE), QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME));

      if (!requestOrThrow(provides, PROVIDES_GET_IMPLEMENTATIONS).isEmpty())
      {
         sb.append(" with ");
         sb.append(requestOrThrow(provides, PROVIDES_GET_IMPLEMENTATIONS)
                           .stream()
                           .map(declared -> requestOrThrow(declared, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME))
                           .collect(joining(", ")));
      }
      return sb.toString();
   }
}
