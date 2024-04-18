package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.renderer.RecordRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.Record;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static io.determann.shadow.meta_meta.Operations.NAME;
import static io.determann.shadow.meta_meta.Provider.requestOrThrow;
import static java.util.stream.Collectors.joining;

public class RecordRendererImpl implements RecordRenderer
{
   private final RenderingContextWrapper context;
   private final Record aRecord;

   public RecordRendererImpl(RenderingContext renderingContext, Record aRecord)
   {
      this.context = new RenderingContextWrapper(renderingContext);
      this.aRecord = aRecord;
   }

   public static String declaration(RenderingContextWrapper context, Record aRecord, String content)
   {
      StringBuilder sb = new StringBuilder();

      Set<Modifier> modifiers = new HashSet<>(aRecord.getModifiers());
      modifiers.remove(Modifier.FINAL);
      modifiers.remove(Modifier.STATIC);

      if (!aRecord.getDirectAnnotationUsages().isEmpty())
      {
         sb.append(aRecord.getDirectAnnotationUsages()
                          .stream()
                          .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + "\n")
                          .collect(Collectors.joining()));
      }
      if (!modifiers.isEmpty())
      {
         sb.append(ModifierRendererImpl.render(modifiers));
         sb.append(' ');
      }
      sb.append("record");
      sb.append(' ');
      sb.append(requestOrThrow(aRecord, NAME));
      if (!aRecord.getGenerics().isEmpty())
      {
         sb.append('<');
         sb.append(aRecord.getGenerics().stream().map(shadow -> ShadowRendererImpl.type(context, shadow)).collect(joining(", ")));
         sb.append('>');
      }

      sb.append('(');
      if (!aRecord.getRecordComponents().isEmpty())
      {
         sb.append(aRecord.getRecordComponents()
                          .stream()
                          .map(component -> RecordComponentRendererImpl.declaration(context, component))
                          .collect(joining(", ")));
      }
      sb.append(')');

      if (!aRecord.getDirectInterfaces().isEmpty())
      {
         sb.append(' ');
         sb.append("implements");
         sb.append(' ');
         sb.append(aRecord.getDirectInterfaces()
                          .stream()
                          .map(anInterface -> InterfaceRendererImpl.type(context, anInterface))
                          .collect(joining(", ")));
      }
      sb.append(' ');
      sb.append('{');
      if (!content.isEmpty())
      {
         sb.append('\n');
         sb.append(content);
         if (!content.endsWith("\n"))
         {
            sb.append('\n');
         }
      }
      sb.append('}');
      sb.append('\n');

      return sb.toString();
   }

   public static String type(RenderingContextWrapper context, Record aRecord)
   {
      StringBuilder sb = new StringBuilder();
      sb.append(context.renderName(aRecord));
      if (!aRecord.getGenericTypes().isEmpty())
      {
         sb.append('<');
         sb.append(aRecord.getGenericTypes().stream().map(shadow -> ShadowRendererImpl.type(context, shadow)).collect(joining(", ")));
         sb.append('>');
      }
      return sb.toString();
   }

   @Override
   public String declaration()
   {
      return declaration(context, aRecord, "");
   }

   @Override
   public String declaration(String content)
   {
      return declaration(context, aRecord, content);
   }

   @Override
   public String type()
   {
      return type(context, aRecord);
   }
}
