package io.determann.shadow.api.dsl;

import io.determann.shadow.api.dsl.annotation.AnnotationCopyrightHeaderStep;
import io.determann.shadow.api.dsl.annotation.AnnotationJavaDocStep;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageTypeStep;
import io.determann.shadow.api.dsl.annotation_value.AnnotationValueRenderable;
import io.determann.shadow.api.dsl.class_.ClassCopyrightHeaderStep;
import io.determann.shadow.api.dsl.class_.ClassJavaDocStep;
import io.determann.shadow.api.dsl.constructor.ConstructorJavaDocStep;
import io.determann.shadow.api.dsl.enum_.EnumCopyrightHeaderStep;
import io.determann.shadow.api.dsl.enum_.EnumJavaDocStep;
import io.determann.shadow.api.dsl.enum_constant.EnumConstantJavaDocStep;
import io.determann.shadow.api.dsl.exports.ExportsPackageStep;
import io.determann.shadow.api.dsl.exports.ExportsRenderable;
import io.determann.shadow.api.dsl.field.FieldJavaDocStep;
import io.determann.shadow.api.dsl.field.FieldRenderable;
import io.determann.shadow.api.dsl.generic.GenericAnnotateStep;
import io.determann.shadow.api.dsl.generic.GenericRenderable;
import io.determann.shadow.api.dsl.interface_.InterfaceCopyrightHeaderStep;
import io.determann.shadow.api.dsl.interface_.InterfaceJavaDocStep;
import io.determann.shadow.api.dsl.method.MethodJavaDocStep;
import io.determann.shadow.api.dsl.module.ModuleCopyrightHeaderStep;
import io.determann.shadow.api.dsl.opens.OpensPackageStep;
import io.determann.shadow.api.dsl.opens.OpensRenderable;
import io.determann.shadow.api.dsl.package_.PackageJavaDocStep;
import io.determann.shadow.api.dsl.parameter.ParameterAnnotateStep;
import io.determann.shadow.api.dsl.parameter.ParameterRenderable;
import io.determann.shadow.api.dsl.provides.ProvidesServiceStep;
import io.determann.shadow.api.dsl.receiver.ReceiverAnnotateStep;
import io.determann.shadow.api.dsl.record.RecordCopyrightHeaderStep;
import io.determann.shadow.api.dsl.record.RecordJavaDocStep;
import io.determann.shadow.api.dsl.record_component.RecordComponentAnnotateStep;
import io.determann.shadow.api.dsl.requires.RequiresModifierStep;
import io.determann.shadow.api.dsl.result.ResultAnnotateStep;
import io.determann.shadow.api.dsl.uses.UsesRenderable;
import io.determann.shadow.api.shadow.C_AnnotationUsage;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.structure.C_EnumConstant;
import io.determann.shadow.api.shadow.structure.C_Package;
import io.determann.shadow.api.shadow.type.*;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;
import io.determann.shadow.internal.dsl.*;

import java.util.List;

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

   static RecordCopyrightHeaderStep record()
   {
      return new RecordDsl();
   }

   static RecordJavaDocStep innerRecord()
   {
      return new RecordDsl();
   }

   static InterfaceCopyrightHeaderStep interface_()
   {
      return new InterfaceDsl();
   }

   static InterfaceJavaDocStep innerInterface()
   {
      return new InterfaceDsl();
   }

   static EnumCopyrightHeaderStep enum_()
   {
      return new EnumDsl();
   }

   static EnumJavaDocStep innerEnum()
   {
      return new EnumDsl();
   }

   static EnumConstantJavaDocStep enumConstant()
   {
      return new EnumConstantDsl();
   }

   /// {@snippet file = "ModuleDslTest.java" region = api}
   static ModuleCopyrightHeaderStep moduleInfo()
   {
      return new ModuleDsl();
   }

   /// {@snippet file = "DirectiveDslTest.java" region = "exports-api-simple-string"}
   ///
   /// @see #exports()
   static ExportsRenderable exports(String packageName)
   {
      return exports().package_(packageName);
   }

   /// {@snippet file = "DirectiveDslTest.java" region = "exports-api-simple-type"}
   ///
   /// @see #exports()
   static ExportsRenderable exports(C_Package aPackage)
   {
      return exports().package_(aPackage);
   }

   /// {@snippet file = "DirectiveDslTest.java" region = "exports-api"}
   static ExportsPackageStep exports()
   {
      return new ExportsDsl();
   }

   /// {@snippet file = "DirectiveDslTest.java" region = "opens-api-simple-type"}
   ///
   /// @see #opens()
   static OpensRenderable opens(C_Package aPackage)
   {
      return opens().package_(aPackage);
   }

   /// {@snippet file = "DirectiveDslTest.java" region = "opens-api-simple-string"}
   ///
   /// @see #opens()
   static OpensRenderable opens(String packageName)
   {
      return opens().package_(packageName);
   }

   /// {@snippet file = "DirectiveDslTest.java" region = "opens-api"}
   static OpensPackageStep opens()
   {
      return new OpensDsl();
   }

   /// {@snippet file = "DirectiveDslTest.java" region = "requires-api"}
   static RequiresModifierStep requires()
   {
      return new RequiresDsl();
   }

   /// {@snippet file = "DirectiveDslTest.java" region = provides-api}
   static ProvidesServiceStep provides()
   {
      return new ProvidesDsl();
   }

   /// {@snippet file = "DirectiveDslTest.java" region = "uses-api-string"}
   static UsesRenderable uses(String serviceName)
   {
      return renderingContext -> "uses " + serviceName + ';';
   }

   /// {@snippet file = "DirectiveDslTest.java" region = "uses-api-type"}
   static UsesRenderable uses(C_Declared service)
   {
      return uses(requestOrThrow(service, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME));
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
   ///
   /// @see #parameter()
   static ParameterRenderable parameter(String type, String name)
   {
      return new ParameterDsl()
            .type(type)
            .name(name);
   }

   /// {@snippet file = "ParameterDslTest.java" region = "short-api"}
   ///
   /// @see #parameter()
   static ParameterRenderable parameter(VariableTypeRenderable type, String name)
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
      return value::renderDeclaration;
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
               case C_Array array -> array.renderType(renderingContext);
               case C_Declared declared -> requestOrThrow(declared, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME);
               case C_Generic _ -> throw new IllegalArgumentException("generic can not be used as an Annotation Value " + value);
               case C_Null _ -> throw new IllegalArgumentException("null can not be used as an Annotation Value " + value);
               case C_Void _ -> "void";
               case C_Wildcard _ -> throw new IllegalArgumentException("Wildcard can not be used as an Annotation Value " + value);
               case C_Primitive primitive -> primitive.renderType(renderingContext);
               default -> throw new IllegalArgumentException("Can not be used as an Annotation Value " + value);
            }
            + ".class";
   }

   /// {@code Dsl.annotationValue(cAnnotationUsage).render(DEFAULT)} -> {@code @org.example.MyAnnotation}
   static AnnotationValueRenderable annotationValue(C_AnnotationUsage value)
   {
      requireNonNull(value);
      return value::renderDeclaration;
   }

   /// {@code Dsl.annotationValue(Dsl.annotationValue(1), Dsl.annotationValue(1F)).render(DEFAULT)} -> {@code {1, 1.0F}}
   static AnnotationValueRenderable annotationValue(AnnotationValueRenderable... value)
   {
      requireNonNull(value);
      return annotationValue(List.of(value));
   }

   /// {@code Dsl.annotationValue(List.of(Dsl.annotationValue(1), Dsl.annotationValue(1F))).render(DEFAULT)} -> {@code {1, 1.0F}}
   static AnnotationValueRenderable annotationValue(List<AnnotationValueRenderable> values)
   {
      requireNonNull(values);
      return renderingContext -> '{' +
                                 values.stream()
                                       .map(annotationValue -> annotationValue.render(renderingContext))
                                       .collect(joining(", ")) +
                                 '}';
   }

   /// {@code Dsl.generic("T").render(DEFAULT)} -> {@code T}
   ///
   /// @see #generic()
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
   ///
   /// @see #field()
   static FieldRenderable constant(String type, String name, String initializer)
   {
      return field().private_().static_().final_().type(type).name(name).initializer(initializer);
   }

   /// {@snippet file = "FieldDslTest.java" region = "constantTypeStringString"}
   ///
   /// @see #field()
   static FieldRenderable constant(VariableTypeRenderable type, String name, String initializer)
   {
      return field().private_().static_().final_().type(type).name(name).initializer(initializer);
   }

   /// {@snippet file = "FieldDslTest.java" region = "constantModifierStringStringString"}
   ///
   /// @see #field()
   static FieldRenderable constant(C_Modifier modifier, String type, String name, String initializer)
   {
      return field().modifier(modifier).final_().static_().type(type).name(name).initializer(initializer);
   }

   /// {@snippet file = "FieldDslTest.java" region = "constantModifierTypeStringString"}
   ///
   /// @see #field()
   static FieldRenderable constant(C_Modifier modifier, VariableTypeRenderable type, String name, String initializer)
   {
      return field().modifier(modifier).final_().static_().type(type).name(name).initializer(initializer);
   }

   /// {@snippet file = "FieldDslTest.java" region = "fieldStringString"}
   ///
   /// @see #field()
   static FieldRenderable field(String type, String name)
   {
      return field().private_().type(type).name(name);
   }

   /// {@snippet file = "FieldDslTest.java" region = "fieldTypeString"}
   ///
   /// @see #field()
   static FieldRenderable field(VariableTypeRenderable type, String name)
   {
      return field().private_().type(type).name(name);
   }

   /// {@snippet file = "FieldDslTest.java" region = "fieldStringStringString"}
   ///
   /// @see #field()
   static FieldRenderable field(String type, String name, String initializer)
   {
      return field().private_().type(type).name(name).initializer(initializer);
   }

   /// {@snippet file = "FieldDslTest.java" region = "fieldTypeStringString"}
   ///
   /// @see #field()
   static FieldRenderable field(VariableTypeRenderable type, String name, String initializer)
   {
      return field().private_().type(type).name(name).initializer(initializer);
   }

   /// {@snippet file = "FieldDslTest.java" region = "fieldModifierStringString"}
   ///
   /// @see #field()
   static FieldRenderable field(C_Modifier modifier, String type, String name)
   {
      return field().modifier(modifier).type(type).name(name);
   }

   /// {@snippet file = "FieldDslTest.java" region = "fieldModifierTypeString"}
   ///
   /// @see #field()
   static FieldRenderable field(C_Modifier modifier, VariableTypeRenderable type, String name)
   {
      return field().modifier(modifier).type(type).name(name);
   }

   /// {@snippet file = "FieldDslTest.java" region = "fieldModifierStringStringString"}
   ///
   /// @see #field()
   static FieldRenderable field(C_Modifier modifier, String type, String name, String initializer)
   {
      return field().modifier(modifier).type(type).name(name).initializer(initializer);
   }

   /// {@snippet file = "FieldDslTest.java" region = "fieldModifierTypeStringString"}
   ///
   /// @see #field()
   static FieldRenderable field(C_Modifier modifier, VariableTypeRenderable type, String name, String initializer)
   {
      return field().modifier(modifier).type(type).name(name).initializer(initializer);
   }
}
