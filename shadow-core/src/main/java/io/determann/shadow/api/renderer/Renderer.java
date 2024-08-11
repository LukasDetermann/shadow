package io.determann.shadow.api.renderer;

import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.structure.Module;
import io.determann.shadow.api.shadow.structure.Package;
import io.determann.shadow.api.shadow.structure.*;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Enum;
import io.determann.shadow.api.shadow.type.Record;
import io.determann.shadow.api.shadow.type.*;
import io.determann.shadow.internal.renderer.*;

public interface Renderer
{
   static AnnotationRenderer render(RenderingContext renderingContext, Annotation annotation)
   {
      return new AnnotationRendererImpl(renderingContext, annotation);
   }

   static AnnotationUsageRenderer render(RenderingContext renderingContext, AnnotationUsage annotationUsage)
   {
      return new AnnotationUsageRendererImpl(renderingContext, annotationUsage);
   }

   static ArrayRenderer render(RenderingContext renderingContext, Array array)
   {
      return new ArrayRendererImpl(renderingContext, array);
   }

   static ClassRenderer render(RenderingContext renderingContext, Class clazz)
   {
      return new ClassRendererImpl(renderingContext, clazz);
   }

   static ConstructorRenderer render(RenderingContext renderingContext, Constructor constructor)
   {
      return new ConstructorRendererImpl(renderingContext, constructor);
   }

   static EnumRenderer render(RenderingContext renderingContext, Enum enumType)
   {
      return new EnumRendererImpl(renderingContext, enumType);
   }

   static EnumConstantRenderer render(RenderingContext renderingContext, EnumConstant enumConstant)
   {
      return new EnumConstantRendererImpl(renderingContext, enumConstant);
   }

   static FieldRenderer render(RenderingContext renderingContext, Field field)
   {
      return new FieldRendererImpl(renderingContext, field);
   }

   static GenericRenderer render(RenderingContext renderingContext, Generic generic)
   {
      return new GenericRendererImpl(renderingContext, generic);
   }

   static InterfaceRenderer render(RenderingContext renderingContext, Interface interfaceType)
   {
      return new InterfaceRendererImpl(renderingContext, interfaceType);
   }

   static IntersectionRenderer render(RenderingContext renderingContext, Intersection intersection)
   {
      return new IntersectionRendererImpl(renderingContext, intersection);
   }

   static MethodRenderer render(RenderingContext renderingContext, Method method)
   {
      return new MethodRendererImpl(renderingContext, method);
   }

   static ModuleRenderer render(RenderingContext renderingContext, Module module)
   {
      return new ModuleRendererImpl(renderingContext, module);
   }

   static PackageRenderer render(RenderingContext renderingContext, Package packageType)
   {
      return new PackageRendererImpl(renderingContext, packageType);
   }

   static ParameterRenderer render(RenderingContext renderingContext, Parameter parameter)
   {
      return new ParameterRendererImpl(renderingContext, parameter);
   }

   static PrimitiveRenderer render(RenderingContext renderingContext, Primitive primitive)
   {
      return new PrimitiveRendererImpl(renderingContext, primitive);
   }

   static RecordRenderer render(RenderingContext renderingContext, Record recordType)
   {
      return new RecordRendererImpl(renderingContext, recordType);
   }

   static RecordComponentRenderer render(RenderingContext renderingContext, RecordComponent recordComponent)
   {
      return new RecordComponentRendererImpl(renderingContext, recordComponent);
   }

   static WildcardRenderer render(RenderingContext renderingContext, Wildcard wildcard)
   {
      return new WildcardRendererImpl(renderingContext, wildcard);
   }
}
