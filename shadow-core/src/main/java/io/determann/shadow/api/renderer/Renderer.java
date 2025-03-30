package io.determann.shadow.api.renderer;

import io.determann.shadow.api.shadow.C_AnnotationUsage;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.structure.*;
import io.determann.shadow.api.shadow.type.*;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;
import io.determann.shadow.internal.renderer.*;

import java.util.Set;

public interface Renderer
{
   static TypeRenderer render(C_Type type)
   {
      return new TypeRendererImpl(type);
   }

   static DeclaredRenderer render(C_Declared declared)
   {
      return new DeclaredRendererImpl(declared );
   }

   static AnnotationRenderer render(C_Annotation annotation)
   {
      return new AnnotationRendererImpl(annotation);
   }

   static AnnotationUsageRenderer render(C_AnnotationUsage annotationUsage)
   {
      return new AnnotationUsageRendererImpl(annotationUsage);
   }

   static ArrayRenderer render(C_Array array)
   {
      return new ArrayRendererImpl(array);
   }

   static ClassRenderer render(C_Class clazz)
   {
      return new ClassRendererImpl(clazz);
   }

   static ConstructorRenderer render(C_Constructor constructor)
   {
      return new ConstructorRendererImpl(constructor);
   }

   static EnumRenderer render(C_Enum enumType)
   {
      return new EnumRendererImpl(enumType);
   }

   static EnumConstantRenderer render(C_EnumConstant enumConstant)
   {
      return new EnumConstantRendererImpl(enumConstant);
   }

   static FieldRenderer render(C_Field field)
   {
      return new FieldRendererImpl(field);
   }

   static GenericRenderer render(C_Generic generic)
   {
      return new GenericRendererImpl(generic);
   }

   static InterfaceRenderer render(C_Interface interfaceType)
   {
      return new InterfaceRendererImpl(interfaceType);
   }

   static IntersectionRenderer render(C_Intersection intersection)
   {
      return new IntersectionRendererImpl(intersection);
   }

   static MethodRenderer render(C_Method method)
   {
      return new MethodRendererImpl(method);
   }

   static ModuleRenderer render(C_Module module)
   {
      return new ModuleRendererImpl(module);
   }

   static PackageRenderer render(C_Package packageType)
   {
      return new PackageRendererImpl(packageType);
   }

   static ParameterRenderer render(C_Parameter parameter)
   {
      return new ParameterRendererImpl(parameter);
   }

   static PrimitiveRenderer render(C_Primitive primitive)
   {
      return new PrimitiveRendererImpl(primitive);
   }

   static RecordRenderer render(C_Record recordType)
   {
      return new RecordRendererImpl(recordType);
   }

   static RecordComponentRenderer render(C_RecordComponent recordComponent)
   {
      return new RecordComponentRendererImpl(recordComponent);
   }

   static WildcardRenderer render(C_Wildcard wildcard)
   {
      return new WildcardRendererImpl(wildcard);
   }

   static ModifierRenderer render(C_Modifier... modifiers)
   {
      return new ModifierRendererImpl(modifiers);
   }

   static ModifierRenderer render(Set<C_Modifier> modifiers)
   {
      return new ModifierRendererImpl(modifiers.toArray(C_Modifier[]::new));
   }

   static ReceiverRenderer render(C_Receiver receiver)
   {
      return new ReceiverRendererImpl(receiver);
   }

   static ResultRenderer render(C_Result result)
   {
      return new ResultRendererImpl(result);
   }
}
