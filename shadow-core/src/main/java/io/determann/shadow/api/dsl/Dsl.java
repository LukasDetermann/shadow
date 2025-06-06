package io.determann.shadow.api.dsl;

import io.determann.shadow.api.dsl.annotation.AnnotationCopyrightHeaderStep;
import io.determann.shadow.api.dsl.annotation.AnnotationJavaDocStep;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageTypeStep;
import io.determann.shadow.api.dsl.annotation_value.AnnotationValueRenderable;
import io.determann.shadow.api.dsl.class_.ClassCopyrightHeaderStep;
import io.determann.shadow.api.dsl.class_.ClassJavaDocStep;
import io.determann.shadow.api.dsl.constructor.ConstructorJavaDocStep;
import io.determann.shadow.api.dsl.enum_constant.EnumConstantJavaDocStep;
import io.determann.shadow.api.dsl.exports.ExportsPackageStep;
import io.determann.shadow.api.dsl.field.FieldJavaDocStep;
import io.determann.shadow.api.dsl.field.FieldRenderable;
import io.determann.shadow.api.dsl.field.FieldType;
import io.determann.shadow.api.dsl.generic.GenericAnnotateStep;
import io.determann.shadow.api.dsl.generic.GenericRenderable;
import io.determann.shadow.api.dsl.method.MethodJavaDocStep;
import io.determann.shadow.api.dsl.module.ModuleJavaDocStep;
import io.determann.shadow.api.dsl.opens.OpensPackageStep;
import io.determann.shadow.api.dsl.package_.PackageJavaDocStep;
import io.determann.shadow.api.dsl.parameter.ParameterAnnotateStep;
import io.determann.shadow.api.dsl.parameter.ParameterRenderable;
import io.determann.shadow.api.dsl.provides.ProvidesServiceStep;
import io.determann.shadow.api.dsl.receiver.ReceiverAnnotateStep;
import io.determann.shadow.api.dsl.record_component.RecordComponentAnnotateStep;
import io.determann.shadow.api.dsl.requires.RequiresModifierStep;
import io.determann.shadow.api.dsl.result.ResultAnnotateStep;
import io.determann.shadow.api.dsl.uses.UsesServiceStep;
import io.determann.shadow.api.renderer.Renderer;
import io.determann.shadow.api.shadow.C_AnnotationUsage;
import io.determann.shadow.api.shadow.C_AnnotationValue;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.structure.C_EnumConstant;
import io.determann.shadow.api.shadow.type.*;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;
import io.determann.shadow.internal.dsl.*;

import java.util.Arrays;

import static io.determann.shadow.api.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

public interface Dsl
{
   /// {@snippet file = "ConstructorDslTest.java" region = "api"}
   static ConstructorJavaDocStep constructor()
   {
      return new ConstructorDsl();
   }

   /// {@snippet file = "MethodDslTest.java" region = "api"}
   static MethodJavaDocStep method()
   {
      return new MethodDsl();
   }

   static ClassCopyrightHeaderStep class_()
   {
      return new ClassDsl();
   }

   static ClassJavaDocStep innerClass()
   {
      return new ClassDsl();
   }

   static EnumConstantJavaDocStep enumConstant()
   {
      return new EnumConstantDsl();
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

   /// {@snippet file = "ParameterDslTest.java" region = "api"}
   static ParameterAnnotateStep parameter()
   {
      return new ParameterDsl();
   }

   /// {@snippet file = "ParameterDslTest.java" region = "short-api"}
   static ParameterRenderable parameter(String type, String name)
   {
      return new ParameterDsl()
            .type(type)
            .name(name);
   }

   static ReceiverAnnotateStep receiver()
   {
      return new ReceiverDsl();
   }

   static RecordComponentAnnotateStep recordComponent()
   {
      return new RecordComponentDsl();
   }

   /// {@snippet file = "ResultDslTest.java" region = "api"}
   static ResultAnnotateStep result()
   {
      return new ResultDsl();
   }

   static AnnotationCopyrightHeaderStep annotation()
   {
      return new AnnotationDsl();
   }

   static AnnotationJavaDocStep innerAnnotation()
   {
      return new AnnotationDsl();
   }

   /// {@snippet file = "AnnotationUsageDslTest.java" region = "api"}
   static AnnotationUsageTypeStep annotationUsage()
   {
      return new AnnotationUsageDsl();
   }

   /// {@code Dsl.annotationValue("value").render(DEFAULT)} -> {@code "value"}
   static AnnotationValueRenderable annotationValue(String value)
   {
      requireNonNull(value);
      return renderingContext -> '\"' + value + '\"';
   }

   /// {@code Dsl.annotationValue(true).render(DEFAULT)} -> {@code true}
   static AnnotationValueRenderable annotationValue(boolean value)
   {
      return renderingContext -> String.valueOf(value);
   }

   /// {@code Dsl.annotationValue((byte) 1).render(DEFAULT)} -> {@code 1}
   static AnnotationValueRenderable annotationValue(byte value)
   {
      return renderingContext -> Byte.toString(value);
   }

   /// {@code Dsl.annotationValue((short) 2).render(DEFAULT)} -> {@code 2}
   static AnnotationValueRenderable annotationValue(short value)
   {
      return renderingContext -> Short.toString(value);
   }

   /// {@code Dsl.annotationValue(3).render(DEFAULT)} -> {@code 3}
   static AnnotationValueRenderable annotationValue(int value)
   {
      return renderingContext -> Integer.toString(value);
   }

   /// {@code Dsl.annotationValue(3L).render(DEFAULT)} -> {@code 3L}
   static AnnotationValueRenderable annotationValue(long value)
   {
      return renderingContext -> Long.toString(value) + 'L';
   }

   /// {@code Dsl.annotationValue('c').render(DEFAULT)} -> {@code 'c'}
   static AnnotationValueRenderable annotationValue(char value)
   {
      return renderingContext -> '\'' + Character.toString(value) + '\'';
   }

   /// {@code Dsl.annotationValue(4F).render(DEFAULT)} -> {@code 4.0F}
   static AnnotationValueRenderable annotationValue(float value)
   {
      return renderingContext -> Float.toString(value) + 'F';
   }

   /// {@code Dsl.annotationValue(5D).render(DEFAULT)} -> {@code 5.0D}
   static AnnotationValueRenderable annotationValue(double value)
   {
      return renderingContext -> Double.toString(value) + 'D';
   }

   /// {@code Dsl.annotationValue(cEnumConstant).render(DEFAULT)} -> {@code org.example.Enum.CONSTANT}
   static AnnotationValueRenderable annotationValue(C_EnumConstant value)
   {
      requireNonNull(value);
      return renderingContext -> Renderer.render(value).invocation(renderingContext);
   }

   /// {@code Dsl.annotationValue(Enum.CONSTANT).render(DEFAULT)} -> {@code org.example.Enum.CONSTANT}
   static AnnotationValueRenderable annotationValue(Enum<?> value)
   {
      requireNonNull(value);
      return renderingContext -> value.getClass().getName() + '.' + value.name();
   }

   /// {@code Dsl.annotationValue(String.class).render(DEFAULT)} -> {@code java.lang.String.class}
   static AnnotationValueRenderable annotationValue(Class<?> value)
   {
      requireNonNull(value);
      return renderingContext -> value.getName() + ".class";
   }

   /// {@code Dsl.annotationValue(cArray).render(DEFAULT)} -> {@code boolean[].class}
   static AnnotationValueRenderable annotationValue(C_Type value)
   {
      requireNonNull(value);
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

   /// {@code Dsl.annotationValue(cAnnotationUsage).render(DEFAULT)} -> {@code @org.example.MyAnnotation}
   static AnnotationValueRenderable annotationValue(C_AnnotationUsage value)
   {
      requireNonNull(value);
      return renderingContext -> Renderer.render(value).declaration(renderingContext);
   }

   /// {@code Dsl.annotationValue(cInt, cFloat).render(DEFAULT)} -> {@code {1, 1.0F}}
   static AnnotationValueRenderable annotationValue(C_AnnotationValue... value)
   {
      requireNonNull(value);
      return renderingContext -> '{' +
                                 Arrays.stream(value)
                                       .map(annotationValue -> Renderer.render(annotationValue).declaration(renderingContext))
                                       .collect(joining(", ")) +
                                 '}';
   }

   /// {@code Dsl.annotationValue(Dsl.annotationValue(1), Dsl.annotationValue(1F)).render(DEFAULT)} -> {@code {1, 1.0F}}
   static AnnotationValueRenderable annotationValue(AnnotationValueRenderable... value)
   {
      requireNonNull(value);
      return renderingContext -> '{' +
                                 Arrays.stream(value)
                                       .map(annotationValue -> annotationValue.render(renderingContext))
                                       .collect(joining(", ")) +
                                 '}';
   }

   /// {@code Dsl.generic("T").render(DEFAULT)} -> {@code T}
   static GenericRenderable generic(String name)
   {
      return generic().name(name);
   }

   /// {@snippet file = "GenericDslTest.java" region = "api"}
   ///
   /// Recursive generics are only possible to declare with a workaround
   ///
   /// {@snippet file = "GenericDslTest.java" region = "recursive"}
   static GenericAnnotateStep generic()
   {
      return new GenericDsl();
   }

   /// {@snippet file = "FieldDslTest.java" region = "api"}
   static FieldJavaDocStep field()
   {
      return new FieldDsl();
   }

   /// {@snippet file = "FieldDslTest.java" region = "constantStringStringString"}
   static FieldRenderable constant(String type, String name, String initializer)
   {
      return field().private_().static_().final_().type(type).name(name).initializer(initializer);
   }

   /// {@snippet file = "FieldDslTest.java" region = "constantTypeStringString"}
   static FieldRenderable constant(C_Array type, String name, String initializer)
   {
      return field().private_().static_().final_().type(type).name(name).initializer(initializer);
   }

   /// {@snippet file = "FieldDslTest.java" region = "constantTypeStringString"}
   static FieldRenderable constant(C_Declared type, String name, String initializer)
   {
      return field().private_().static_().final_().type(type).name(name).initializer(initializer);
   }

   /// {@snippet file = "FieldDslTest.java" region = "constantTypeStringString"}
   static FieldRenderable constant(C_Generic type, String name, String initializer)
   {
      return field().private_().static_().final_().type(type).name(name).initializer(initializer);
   }

   /// {@snippet file = "FieldDslTest.java" region = "constantTypeStringString"}
   static FieldRenderable constant(C_Primitive type, String name, String initializer)
   {
      return field().private_().static_().final_().type(type).name(name).initializer(initializer);
   }

   /// {@snippet file = "FieldDslTest.java" region = "constantModifierStringStringString"}
   static FieldRenderable constant(C_Modifier modifier, String type, String name, String initializer)
   {
      return field().modifier(modifier).final_().static_().type(type).name(name).initializer(initializer);
   }

   /// {@snippet file = "FieldDslTest.java" region = "constantModifierTypeStringString"}
   static FieldRenderable constant(C_Modifier modifier, C_Array type, String name, String initializer)
   {
      return field().modifier(modifier).final_().static_().type(type).name(name).initializer(initializer);
   }

   /// {@snippet file = "FieldDslTest.java" region = "constantModifierTypeStringString"}
   static FieldRenderable constant(C_Modifier modifier, C_Declared type, String name, String initializer)
   {
      return field().modifier(modifier).final_().static_().type(type).name(name).initializer(initializer);
   }

   /// {@snippet file = "FieldDslTest.java" region = "constantModifierTypeStringString"}
   static FieldRenderable constant(C_Modifier modifier, C_Generic type, String name, String initializer)
   {
      return field().modifier(modifier).final_().static_().type(type).name(name).initializer(initializer);
   }

   /// {@snippet file = "FieldDslTest.java" region = "constantModifierTypeStringString"}
   static FieldRenderable constant(C_Modifier modifier, C_Primitive type, String name, String initializer)
   {
      return field().modifier(modifier).final_().static_().type(type).name(name).initializer(initializer);
   }

   /// {@snippet file = "FieldDslTest.java" region = "fieldStringString"}
   static FieldRenderable field(String type, String name)
   {
      return field().private_().type(type).name(name);
   }

   /// {@snippet file = "FieldDslTest.java" region = "fieldTypeString"}
   static FieldRenderable field(C_Array type, String name)
   {
      return field().private_().type(type).name(name);
   }

   /// {@snippet file = "FieldDslTest.java" region = "fieldTypeString"}
   static FieldRenderable field(C_Declared type, String name)
   {
      return field().private_().type(type).name(name);
   }

   /// {@snippet file = "FieldDslTest.java" region = "fieldTypeString"}
   static FieldRenderable field(C_Generic type, String name)
   {
      return field().private_().type(type).name(name);
   }

   /// {@snippet file = "FieldDslTest.java" region = "fieldTypeString"}
   static FieldRenderable field(C_Primitive type, String name)
   {
      return field().private_().type(type).name(name);
   }

   /// {@snippet file = "FieldDslTest.java" region = "fieldStringStringString"}
   static FieldRenderable field(String type, String name, String initializer)
   {
      return field().private_().type(type).name(name).initializer(initializer);
   }

   /// {@snippet file = "FieldDslTest.java" region = "fieldTypeStringString"}
   static FieldRenderable field(C_Array type, String name, String initializer)
   {
      return field().private_().type(type).name(name).initializer(initializer);
   }

   /// {@snippet file = "FieldDslTest.java" region = "fieldTypeStringString"}
   static FieldRenderable field(C_Declared type, String name, String initializer)
   {
      return field().private_().type(type).name(name).initializer(initializer);
   }

   /// {@snippet file = "FieldDslTest.java" region = "fieldTypeStringString"}
   static FieldRenderable field(C_Generic type, String name, String initializer)
   {
      return field().private_().type(type).name(name).initializer(initializer);
   }

   /// {@snippet file = "FieldDslTest.java" region = "fieldTypeStringString"}
   static FieldRenderable field(C_Primitive type, String name, String initializer)
   {
      return field().private_().type(type).name(name).initializer(initializer);
   }

   /// {@snippet file = "FieldDslTest.java" region = "fieldModifierStringString"}
   static FieldRenderable field(C_Modifier modifier, String type, String name)
   {
      return field().modifier(modifier).type(type).name(name);
   }

   /// {@snippet file = "FieldDslTest.java" region = "fieldModifierTypeString"}
   static FieldRenderable field(C_Modifier modifier, C_Array type, String name)
   {
      return field().modifier(modifier).type(type).name(name);
   }

   /// {@snippet file = "FieldDslTest.java" region = "fieldModifierTypeString"}
   static FieldRenderable field(C_Modifier modifier, C_Declared type, String name)
   {
      return field().modifier(modifier).type(type).name(name);
   }

   /// {@snippet file = "FieldDslTest.java" region = "fieldModifierTypeString"}
   static FieldRenderable field(C_Modifier modifier, C_Generic type, String name)
   {
      return field().modifier(modifier).type(type).name(name);
   }

   /// {@snippet file = "FieldDslTest.java" region = "fieldModifierTypeString"}
   static FieldRenderable field(C_Modifier modifier, C_Primitive type, String name)
   {
      return field().modifier(modifier).type(type).name(name);
   }

   /// {@snippet file = "FieldDslTest.java" region = "fieldModifierStringStringString"}
   static FieldRenderable field(C_Modifier modifier, String type, String name, String initializer)
   {
      return field().modifier(modifier).type(type).name(name).initializer(initializer);
   }

   /// {@snippet file = "FieldDslTest.java" region = "fieldModifierTypeStringString"}
   static FieldRenderable field(C_Modifier modifier, C_Array type, String name, String initializer)
   {
      return field().modifier(modifier).type(type).name(name).initializer(initializer);
   }

   /// {@snippet file = "FieldDslTest.java" region = "fieldModifierTypeStringString"}
   static FieldRenderable field(C_Modifier modifier, C_Declared type, String name, String initializer)
   {
      return field().modifier(modifier).type(type).name(name).initializer(initializer);
   }

   /// {@snippet file = "FieldDslTest.java" region = "fieldModifierTypeStringString"}
   static FieldRenderable field(C_Modifier modifier, C_Generic type, String name, String initializer)
   {
      return field().modifier(modifier).type(type).name(name).initializer(initializer);
   }

   /// {@snippet file = "FieldDslTest.java" region = "fieldModifierTypeStringString"}
   static FieldRenderable field(C_Modifier modifier, C_Primitive type, String name, String initializer)
   {
      return field().modifier(modifier).type(type).name(name).initializer(initializer);
   }
}
