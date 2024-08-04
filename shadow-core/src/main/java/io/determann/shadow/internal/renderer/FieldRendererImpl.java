package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.FieldRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.Operations;
import io.determann.shadow.api.shadow.annotationusage.AnnotationUsage;
import io.determann.shadow.api.shadow.modifier.Modifier;
import io.determann.shadow.api.shadow.structure.Field;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static io.determann.shadow.api.shadow.Operations.*;
import static io.determann.shadow.api.shadow.Provider.requestOrEmpty;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

public class FieldRendererImpl implements FieldRenderer
{
   private final RenderingContextWrapper context;
   private final Field field;

   public FieldRendererImpl(RenderingContext renderingContext, Field field)
   {
      this.context = new RenderingContextWrapper(renderingContext);
      this.field = field;
   }

   public static String declaration(RenderingContextWrapper context, Field field)
   {
      StringBuilder sb = new StringBuilder();

      //noinspection OptionalContainsCollection
      Optional<List<AnnotationUsage>> annotationUsages = requestOrEmpty(field, Operations.ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES);
      if (!annotationUsages.map(List::isEmpty).orElse(true))
      {
         sb.append(annotationUsages.get()
                        .stream()
                        .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + "\n")
                        .collect(Collectors.joining()));
      }
      Set<Modifier> modifiers = requestOrThrow(field, MODIFIABLE_GET_MODIFIERS);
      if (!modifiers.isEmpty())
      {
         sb.append(ModifierRendererImpl.render(modifiers));
         sb.append(' ');
      }
      sb.append(ShadowRendererImpl.type(context, requestOrThrow(field, VARIABLE_GET_TYPE)));
      sb.append(' ');
      sb.append(requestOrThrow(field, NAMEABLE_GET_NAME));
      sb.append(';');
      sb.append('\n');

      return sb.toString();
   }

   @Override
   public String declaration()
   {
      return declaration(context, field);
   }
}
