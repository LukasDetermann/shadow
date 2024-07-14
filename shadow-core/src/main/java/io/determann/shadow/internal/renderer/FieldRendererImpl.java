package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.FieldRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.structure.Field;

import java.util.stream.Collectors;

import static io.determann.shadow.api.shadow.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.shadow.Operations.VARIABLE_GET_TYPE;
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

      if (!field.getDirectAnnotationUsages().isEmpty())
      {
         sb.append(field.getDirectAnnotationUsages()
                        .stream()
                        .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + "\n")
                        .collect(Collectors.joining()));
      }
      if (!field.getModifiers().isEmpty())
      {
         sb.append(ModifierRendererImpl.render(field.getModifiers()));
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
