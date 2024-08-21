package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.type.*;

import static io.determann.shadow.api.Operations.SHADOW_GET_KIND;
import static io.determann.shadow.api.Provider.requestOrThrow;

public class ShadowRendererImpl
{
   public static String type(RenderingContextWrapper context, C_Shadow shadow)
   {
      return switch (requestOrThrow(shadow, SHADOW_GET_KIND))
      {
         case BOOLEAN, BYTE, SHORT, INT, LONG, CHAR, FLOAT, DOUBLE -> PrimitiveRendererImpl.type(context, ((C_Primitive) shadow));
         case CLASS -> ClassRendererImpl.type(context, ((C_Class) shadow));
         case INTERFACE -> InterfaceRendererImpl.type(context, ((C_Interface) shadow));
         case ENUM -> EnumRendererImpl.type(context, ((C_Enum) shadow));
         case ANNOTATION -> AnnotationRendererImpl.type(context, ((C_Annotation) shadow));
         case RECORD -> RecordRendererImpl.type(context, ((C_Record) shadow));
         case ARRAY -> ArrayRendererImpl.type(context, ((C_Array) shadow));
         case GENERIC -> GenericRendererImpl.type(context, ((C_Generic) shadow));
         case WILDCARD -> WildcardRendererImpl.type(context, ((C_Wildcard) shadow));
         case INTERSECTION -> IntersectionRendererImpl.type(context, ((C_Intersection) shadow));
         case VOID -> VoidRendererImpl.type();
         case NULL -> NullRendererImpl.type();

         case MODULE, ENUM_CONSTANT, FIELD, PARAMETER -> throw new IllegalArgumentException();
      };
   }

   public static String classDeclaration(RenderingContext context, C_Declared declared)
   {
      return context.renderName(declared) + ".class";
   }
}
