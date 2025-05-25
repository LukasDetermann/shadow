package io.determann.shadow.api.dsl;

import io.determann.shadow.api.dsl.annotation.AnnotationJavaDocStep;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageTypeStep;
import io.determann.shadow.api.dsl.annotation_value.AnnotationValueRenderable;
import io.determann.shadow.api.dsl.class_.ClassJavaDocStep;
import io.determann.shadow.api.dsl.constructor.ConstructorJavaDocStep;
import io.determann.shadow.api.dsl.enum_constant.EnumConstantJavaDocStep;
import io.determann.shadow.api.dsl.exports.ExportsPackageStep;
import io.determann.shadow.api.dsl.field.FieldJavaDocStep;
import io.determann.shadow.api.dsl.method.MethodJavaDocStep;
import io.determann.shadow.api.dsl.module.ModuleJavaDocStep;
import io.determann.shadow.api.dsl.opens.OpensPackageStep;
import io.determann.shadow.api.dsl.package_.PackageJavaDocStep;
import io.determann.shadow.api.dsl.parameter.ParameterAnnotateStep;
import io.determann.shadow.api.dsl.provides.ProvidesServiceStep;
import io.determann.shadow.api.dsl.receiver.ReceiverAnnotateStep;
import io.determann.shadow.api.dsl.record_component.RecordComponentAnnotateStep;
import io.determann.shadow.api.dsl.requires.RequiresModifierStep;
import io.determann.shadow.api.dsl.result.ResultAnnotateStep;
import io.determann.shadow.api.dsl.uses.UsesServiceStep;
import io.determann.shadow.api.renderer.Renderer;
import io.determann.shadow.api.shadow.C_AnnotationUsage;
import io.determann.shadow.api.shadow.C_AnnotationValue;
import io.determann.shadow.api.shadow.structure.C_EnumConstant;
import io.determann.shadow.api.shadow.type.*;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;
import io.determann.shadow.internal.dsl.*;

import java.util.List;

import static io.determann.shadow.api.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

public interface Dsl
{
   static ConstructorJavaDocStep constructor()
   {
      return new ConstructorDsl();
   }

   static MethodJavaDocStep method()
   {
      return new MethodDsl();
   }

   static ClassJavaDocStep class_()
   {
      return new ClassDsl();
   }

   static EnumConstantJavaDocStep enumConstant()
   {
      return new EnumConstantDsl();
   }

   static FieldJavaDocStep field()
   {
      return new FieldDsl();
   }

   static ModuleJavaDocStep moduleInfo()
   {
      return new ModuleDsl();
   }

   static ExportsPackageStep exports()
   {
      return new ExportsDsl();
   }

   static OpensPackageStep opens()
   {
      return new OpensDsl();
   }

   static RequiresModifierStep requires()
   {
      return new RequiresDsl();
   }

   static ProvidesServiceStep provides()
   {
      return new ProvidesDsl();
   }

   static UsesServiceStep uses()
   {
      return new UsesDsl();
   }

   static PackageJavaDocStep packageInfo()
   {
      return new PackageDsl();
   }

   static ParameterAnnotateStep parameter()
   {
      return new ParameterDsl();
   }

   static ReceiverAnnotateStep receiver()
   {
      return new ReceiverDsl();
   }

   static RecordComponentAnnotateStep recordComponent()
   {
      return new RecordComponentDsl();
   }

   static ResultAnnotateStep result()
   {
      return new ResultDsl();
   }

   static AnnotationJavaDocStep annotation()
   {
      return new AnnotationDsl();
   }

   static AnnotationUsageTypeStep annotationUsage()
   {
      return new AnnotationUsageDsl();
   }

   static AnnotationValueRenderable annotationValue(String value)
   {
      return renderingContext -> '\"' + value + '\"';
   }

   static AnnotationValueRenderable annotationValue(boolean value)
   {
      return renderingContext -> String.valueOf(value);
   }

   static AnnotationValueRenderable annotationValue(byte value)
   {
      return renderingContext -> Byte.toString(value);
   }

   static AnnotationValueRenderable annotationValue(short value)
   {
      return renderingContext -> Short.toString(value);
   }

   static AnnotationValueRenderable annotationValue(int value)
   {
      return renderingContext -> Integer.toString(value);
   }

   static AnnotationValueRenderable annotationValue(long value)
   {
      return renderingContext -> Long.toString(value) + 'L';
   }

   static AnnotationValueRenderable annotationValue(char value)
   {
      return renderingContext -> '\'' + Character.toString(value) + '\'';
   }

   static AnnotationValueRenderable annotationValue(float value)
   {
      return renderingContext -> Float.toString(value) + 'F';
   }

   static AnnotationValueRenderable annotationValue(double value)
   {
      return renderingContext -> Double.toString(value) + 'D';
   }

   static AnnotationValueRenderable annotationValue(C_EnumConstant value)
   {
      return renderingContext -> Renderer.render(value).invocation(renderingContext);
   }

   static AnnotationValueRenderable annotationValue(Enum<?> value)
   {
      return renderingContext -> value.getClass().getName() + '.' + value.name();
   }

   static AnnotationValueRenderable annotationValue(Class<?> value)
   {
      return renderingContext -> value.getName() + ".class";
   }

   /// @see #annotationValue(Class)
   static AnnotationValueRenderable annotationValue(C_Type value)
   {
      return renderingContext ->
            switch (value)
            {
               case C_Array array -> Renderer.render(array).type(renderingContext);
               case C_Declared declared -> requestOrThrow(declared, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME);
               case C_Generic _ -> throw new IllegalArgumentException("generic can not be used as an Annotation Value " + value);
               case C_Intersection _ -> throw new IllegalArgumentException("Intersection can not be used as an Annotation Value " + value);
               case C_Null _ -> throw new IllegalArgumentException("null can not be used as an Annotation Value " + value);
               case C_Void _ -> "void";
               case C_Wildcard _ -> throw new IllegalArgumentException("Wildcard can not be used as an Annotation Value " + value);
               case C_Primitive primitive -> Renderer.render(primitive).type(renderingContext);
               default -> throw new IllegalArgumentException("Can not be used as an Annotation Value " + value);
            }
            + ".class";
   }

   static AnnotationValueRenderable annotationValue(C_AnnotationUsage value)
   {
      return renderingContext -> Renderer.render(value).declaration(renderingContext);
   }

   static AnnotationValueRenderable annotationValue(C_AnnotationValue... value)
   {
      return annotationValue(asList(value));
   }

   static AnnotationValueRenderable annotationValue(List<? extends C_AnnotationValue> value)
   {
      return renderingContext -> '{' +
                                 value.stream()
                                      .map(C_AnnotationValue.class::cast)
                                      .map(annotationValue -> Renderer.render(annotationValue).declaration(renderingContext))
                                      .collect(joining(", ")) +
                                 '}';
   }
}
