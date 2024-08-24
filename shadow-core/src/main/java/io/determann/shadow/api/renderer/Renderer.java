package io.determann.shadow.api.renderer;

import io.determann.shadow.api.shadow.C_AnnotationUsage;
import io.determann.shadow.api.shadow.structure.*;
import io.determann.shadow.api.shadow.type.*;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;
import io.determann.shadow.internal.renderer.*;

public interface Renderer
{
   static AnnotationRenderer render(RenderingContext renderingContext, C_Annotation annotation)
   {
      return new AnnotationRendererImpl(renderingContext, annotation);
   }

   static AnnotationUsageRenderer render(RenderingContext renderingContext, C_AnnotationUsage annotationUsage)
   {
      return new AnnotationUsageRendererImpl(renderingContext, annotationUsage);
   }

   static ArrayRenderer render(RenderingContext renderingContext, C_Array array)
   {
      return new ArrayRendererImpl(renderingContext, array);
   }

   static ClassRenderer render(RenderingContext renderingContext, C_Class clazz)
   {
      return new ClassRendererImpl(renderingContext, clazz);
   }

   static ConstructorRenderer render(RenderingContext renderingContext, C_Constructor constructor)
   {
      return new ConstructorRendererImpl(renderingContext, constructor);
   }

   static EnumRenderer render(RenderingContext renderingContext, C_Enum enumType)
   {
      return new EnumRendererImpl(renderingContext, enumType);
   }

   static EnumConstantRenderer render(RenderingContext renderingContext, C_EnumConstant enumConstant)
   {
      return new EnumConstantRendererImpl(renderingContext, enumConstant);
   }

   static FieldRenderer render(RenderingContext renderingContext, C_Field field)
   {
      return new FieldRendererImpl(renderingContext, field);
   }

   static GenericRenderer render(RenderingContext renderingContext, C_Generic generic)
   {
      return new GenericRendererImpl(renderingContext, generic);
   }

   static InterfaceRenderer render(RenderingContext renderingContext, C_Interface interfaceType)
   {
      return new InterfaceRendererImpl(renderingContext, interfaceType);
   }

   static IntersectionRenderer render(RenderingContext renderingContext, C_Intersection intersection)
   {
      return new IntersectionRendererImpl(renderingContext, intersection);
   }

   static MethodRenderer render(RenderingContext renderingContext, C_Method method)
   {
      return new MethodRendererImpl(renderingContext, method);
   }

   static ModuleRenderer render(RenderingContext renderingContext, C_Module module)
   {
      return new ModuleRendererImpl(renderingContext, module);
   }

   static PackageRenderer render(RenderingContext renderingContext, C_Package packageType)
   {
      return new PackageRendererImpl(renderingContext, packageType);
   }

   static ParameterRenderer render(RenderingContext renderingContext, C_Parameter parameter)
   {
      return new ParameterRendererImpl(renderingContext, parameter);
   }

   static PrimitiveRenderer render(RenderingContext renderingContext, C_Primitive primitive)
   {
      return new PrimitiveRendererImpl(renderingContext, primitive);
   }

   static RecordRenderer render(RenderingContext renderingContext, C_Record recordType)
   {
      return new RecordRendererImpl(renderingContext, recordType);
   }

   static RecordComponentRenderer render(RenderingContext renderingContext, C_RecordComponent recordComponent)
   {
      return new RecordComponentRendererImpl(renderingContext, recordComponent);
   }

   static WildcardRenderer render(RenderingContext renderingContext, C_Wildcard wildcard)
   {
      return new WildcardRendererImpl(renderingContext, wildcard);
   }
}
