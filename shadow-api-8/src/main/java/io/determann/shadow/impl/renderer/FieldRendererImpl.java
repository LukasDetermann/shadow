package io.determann.shadow.impl.renderer;

import io.determann.shadow.api.renderer.FieldRenderer;
import io.determann.shadow.api.shadow.Field;
import io.determann.shadow.impl.ShadowApiImpl;

import java.util.stream.Collectors;

public class FieldRendererImpl implements FieldRenderer
{
   private final Context context;
   private final Field field;

   public FieldRendererImpl(Field field)
   {
      this.context = ((ShadowApiImpl) field.getApi()).getRenderingContext();
      this.field = field;
   }

   public static String declaration(Context context, Field field)
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
      sb.append(ShadowRendererImpl.type(context, field.getType()));
      sb.append(' ');
      sb.append(field.getSimpleName());
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
