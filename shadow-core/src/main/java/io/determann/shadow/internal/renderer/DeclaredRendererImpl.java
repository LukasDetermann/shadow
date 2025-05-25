package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.DeclaredRenderer;
import io.determann.shadow.api.renderer.Renderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.type.*;

public class DeclaredRendererImpl
      implements DeclaredRenderer
{
   private final C_Declared declared;

   public DeclaredRendererImpl(C_Declared declared)
   {
      this.declared = declared;
   }

   @Override
   public String declaration(RenderingContext renderingContext)
   {
      return switch (declared)
      {
         case C_Annotation cAnnotation -> Renderer.render(cAnnotation).declaration(renderingContext);
         case C_Class cClass -> Renderer.render(cClass).declaration(renderingContext);
         case C_Enum cEnum -> Renderer.render(cEnum).declaration(renderingContext);
         case C_Interface cInterface -> Renderer.render(cInterface).declaration(renderingContext);
         case C_Record record -> Renderer.render(record).declaration(renderingContext);
         default -> throw new IllegalStateException("Unexpected value: " + declared);
      };
   }

   @Override
   public String declaration(RenderingContext renderingContext, String content)
   {
      return switch (declared)
      {
         case C_Annotation cAnnotation -> Renderer.render(cAnnotation).declaration(renderingContext, content);
         case C_Class cClass -> Renderer.render(cClass).declaration(renderingContext, content);
         case C_Enum cEnum -> Renderer.render(cEnum).declaration(renderingContext, content);
         case C_Interface cInterface -> Renderer.render(cInterface).declaration(renderingContext, content);
         case C_Record record -> Renderer.render(record).declaration(renderingContext, content);
         default -> throw new IllegalStateException("Unexpected value: " + declared);
      };
   }

   @Override
   public String type(RenderingContext renderingContext)
   {
      return switch (declared)
      {
         case C_Annotation cAnnotation -> Renderer.render(cAnnotation).type(renderingContext);
         case C_Class cClass -> Renderer.render(cClass).type(renderingContext);
         case C_Enum cEnum -> Renderer.render(cEnum).type(renderingContext);
         case C_Interface cInterface -> Renderer.render(cInterface).type(renderingContext);
         case C_Record record -> Renderer.render(record).type(renderingContext);
         default -> throw new IllegalStateException("Unexpected value: " + declared);
      };
   }
}
