package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.type.*;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;

public class ShadowRendererImpl
{
   public static String type(RenderingContextWrapper context, C_Shadow shadow)
   {
      return switch (shadow)
      {
         case C_Primitive primitive -> PrimitiveRendererImpl.type(context, primitive);
         case C_Class aClass -> ClassRendererImpl.type(context, aClass);
         case C_Interface anInterface -> InterfaceRendererImpl.type(context, anInterface);
         case C_Enum anEnum -> EnumRendererImpl.type(context, anEnum);
         case C_Annotation annotation -> AnnotationRendererImpl.type(context, annotation);
         case C_Record record -> RecordRendererImpl.type(context, record);
         case C_Array array -> ArrayRendererImpl.type(context, array);
         case C_Generic generic -> GenericRendererImpl.type(context, generic);
         case C_Wildcard wildcard -> WildcardRendererImpl.type(context, wildcard);
         case C_Intersection intersection -> IntersectionRendererImpl.type(context, intersection);
         case C_Void aVoid -> VoidRendererImpl.type();
         case C_Null aNull -> NullRendererImpl.type();
         default -> throw new IllegalArgumentException();
      };
   }

   public static String classDeclaration(RenderingContext context, C_Declared declared)
   {
      return context.renderName(declared) + ".class";
   }
}
