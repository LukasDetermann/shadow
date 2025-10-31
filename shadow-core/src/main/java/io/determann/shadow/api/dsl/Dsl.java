package io.determann.shadow.api.dsl;

import io.determann.shadow.api.C;
import io.determann.shadow.api.dsl.annotation.AnnotationCopyrightHeaderStep;
import io.determann.shadow.api.dsl.annotation.AnnotationOuterStep;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageTypeStep;
import io.determann.shadow.api.dsl.annotation_value.AnnotationValueRenderable;
import io.determann.shadow.api.dsl.class_.ClassCopyrightHeaderStep;
import io.determann.shadow.api.dsl.class_.ClassOuterStep;
import io.determann.shadow.api.dsl.constructor.ConstructorJavaDocStep;
import io.determann.shadow.api.dsl.enum_.EnumCopyrightHeaderStep;
import io.determann.shadow.api.dsl.enum_.EnumOuterStep;
import io.determann.shadow.api.dsl.enum_constant.EnumConstantJavaDocStep;
import io.determann.shadow.api.dsl.exports.ExportsPackageStep;
import io.determann.shadow.api.dsl.exports.ExportsRenderable;
import io.determann.shadow.api.dsl.field.FieldJavaDocStep;
import io.determann.shadow.api.dsl.generic.GenericAnnotateStep;
import io.determann.shadow.api.dsl.generic.GenericRenderable;
import io.determann.shadow.api.dsl.import_.ImportRenderable;
import io.determann.shadow.api.dsl.import_.ImportStaticStep;
import io.determann.shadow.api.dsl.interface_.InterfaceCopyrightHeaderStep;
import io.determann.shadow.api.dsl.interface_.InterfaceOuterStep;
import io.determann.shadow.api.dsl.method.MethodJavaDocStep;
import io.determann.shadow.api.dsl.module.ModuleCopyrightHeaderStep;
import io.determann.shadow.api.dsl.opens.OpensPackageStep;
import io.determann.shadow.api.dsl.opens.OpensRenderable;
import io.determann.shadow.api.dsl.package_.PackageJavaDocStep;
import io.determann.shadow.api.dsl.package_.PackageRenderable;
import io.determann.shadow.api.dsl.parameter.ParameterAnnotateStep;
import io.determann.shadow.api.dsl.parameter.ParameterRenderable;
import io.determann.shadow.api.dsl.provides.ProvidesServiceStep;
import io.determann.shadow.api.dsl.receiver.ReceiverAnnotateStep;
import io.determann.shadow.api.dsl.record.RecordCopyrightHeaderStep;
import io.determann.shadow.api.dsl.record.RecordOuterStep;
import io.determann.shadow.api.dsl.record_component.RecordComponentAnnotateStep;
import io.determann.shadow.api.dsl.requires.RequiresModifierStep;
import io.determann.shadow.api.dsl.result.ResultAnnotateStep;
import io.determann.shadow.api.dsl.uses.UsesRenderable;
import io.determann.shadow.internal.dsl.*;
import org.jetbrains.annotations.Contract;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static io.determann.shadow.api.query.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

public interface Dsl
{
   @Contract(value = "-> new", pure = true)
   static ImportStaticStep import_()
   {
      return new ImportDsl();
   }

   @Contract(value = "_ -> new", pure = true)
   static ImportRenderable import_(String name)
   {
      return import_().declared(name);
   }

   /// {@snippet file = "ConstructorDslTest.java" region = "api"}
   @Contract(value = " -> new", pure = true)
   static ConstructorJavaDocStep constructor()
   {
      return new ConstructorDsl();
   }

   /// {@snippet file = "MethodDslTest.java" region = "api"}
   @Contract(value = " -> new", pure = true)
   static MethodJavaDocStep method()
   {
      return new MethodDsl();
   }

   @Contract(value = " -> new", pure = true)
   static ClassCopyrightHeaderStep class_()
   {
      return new ClassDsl();
   }

   @Contract(value = " -> new", pure = true)
   static ClassOuterStep innerClass()
   {
      return new ClassDsl();
   }

   @Contract(value = " -> new", pure = true)
   static RecordCopyrightHeaderStep record()
   {
      return new RecordDsl();
   }

   @Contract(value = " -> new", pure = true)
   static RecordOuterStep innerRecord()
   {
      return new RecordDsl();
   }

   @Contract(value = " -> new", pure = true)
   static InterfaceCopyrightHeaderStep interface_()
   {
      return new InterfaceDsl();
   }

   @Contract(value = " -> new", pure = true)
   static InterfaceOuterStep innerInterface()
   {
      return new InterfaceDsl();
   }

   @Contract(value = " -> new", pure = true)
   static EnumCopyrightHeaderStep enum_()
   {
      return new EnumDsl();
   }

   @Contract(value = " -> new", pure = true)
   static EnumOuterStep innerEnum()
   {
      return new EnumDsl();
   }

   @Contract(value = " -> new", pure = true)
   static EnumConstantJavaDocStep enumConstant()
   {
      return new EnumConstantDsl();
   }

   /// {@snippet file = "ModuleDslTest.java" region = api}
   @Contract(value = " -> new", pure = true)
   static ModuleCopyrightHeaderStep moduleInfo()
   {
      return new ModuleDsl();
   }

   /// {@snippet file = "DirectiveDslTest.java" region = "exports-api-simple-string"}
   ///
   /// @see #exports()
   @Contract(value = "_ -> new", pure = true)
   static ExportsRenderable exports(String packageName)
   {
      return exports().package_(packageName);
   }

   /// {@snippet file = "DirectiveDslTest.java" region = "exports-api-simple-type"}
   ///
   /// @see #exports()
   @Contract(value = "_ -> new", pure = true)
   static ExportsRenderable exports(PackageRenderable aPackage)
   {
      return exports().package_(aPackage);
   }

   /// {@snippet file = "DirectiveDslTest.java" region = "exports-api"}
   @Contract(value = " -> new", pure = true)
   static ExportsPackageStep exports()
   {
      return new ExportsDsl();
   }

   /// {@snippet file = "DirectiveDslTest.java" region = "opens-api-simple-type"}
   ///
   /// @see #opens()
   @Contract(value = "_ -> new", pure = true)
   static OpensRenderable opens(PackageRenderable aPackage)
   {
      return opens().package_(aPackage);
   }

   /// {@snippet file = "DirectiveDslTest.java" region = "opens-api-simple-string"}
   ///
   /// @see #opens()
   @Contract(value = "_ -> new", pure = true)
   static OpensRenderable opens(String packageName)
   {
      return opens().package_(packageName);
   }

   /// {@snippet file = "DirectiveDslTest.java" region = "opens-api"}
   @Contract(value = " -> new", pure = true)
   static OpensPackageStep opens()
   {
      return new OpensDsl();
   }

   /// {@snippet file = "DirectiveDslTest.java" region = "requires-api"}
   @Contract(value = " -> new", pure = true)
   static RequiresModifierStep requires()
   {
      return new RequiresDsl();
   }

   /// {@snippet file = "DirectiveDslTest.java" region = provides-api}
   @Contract(value = " -> new", pure = true)
   static ProvidesServiceStep provides()
   {
      return new ProvidesDsl();
   }

   /// {@snippet file = "DirectiveDslTest.java" region = "uses-api-string"}
   @Contract(value = "_ -> new", pure = true)
   static UsesRenderable uses(String serviceName)
   {
      return renderingContext -> "uses " + serviceName + ';';
   }

   /// {@snippet file = "DirectiveDslTest.java" region = "uses-api-type"}
   @Contract(value = "_ -> new", pure = true)
   static UsesRenderable uses(C.Declared service)
   {
      return uses(requestOrThrow(service, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME));
   }

   @Contract(value = " -> new", pure = true)
   static PackageJavaDocStep packageInfo()
   {
      return new PackageDsl();
   }

   /// {@snippet file = "ParameterDslTest.java" region = "api"}
   @Contract(value = " -> new", pure = true)
   static ParameterAnnotateStep parameter()
   {
      return new ParameterDsl();
   }

   /// {@snippet file = "ParameterDslTest.java" region = "short-api"}
   ///
   /// @see #parameter()
   @Contract(value = "_, _ -> new", pure = true)
   static ParameterRenderable parameter(String type, String name)
   {
      return new ParameterDsl()
            .type(type)
            .name(name);
   }

   /// {@snippet file = "ParameterDslTest.java" region = "short-api"}
   ///
   /// @see #parameter()
   @Contract(value = "_, _ -> new", pure = true)
   static ParameterRenderable parameter(VariableTypeRenderable type, String name)
   {
      return new ParameterDsl()
            .type(type)
            .name(name);
   }

   @Contract(value = " -> new", pure = true)
   static ReceiverAnnotateStep receiver()
   {
      return new ReceiverDsl();
   }

   @Contract(value = " -> new", pure = true)
   static RecordComponentAnnotateStep recordComponent()
   {
      return new RecordComponentDsl();
   }

   /// {@snippet file = "ResultDslTest.java" region = "api"}
   @Contract(value = " -> new", pure = true)
   static ResultAnnotateStep result()
   {
      return new ResultDsl();
   }

   @Contract(value = " -> new", pure = true)
   static AnnotationCopyrightHeaderStep annotation()
   {
      return new AnnotationDsl();
   }

   @Contract(value = " -> new", pure = true)
   static AnnotationOuterStep innerAnnotation()
   {
      return new AnnotationDsl();
   }

   /// {@snippet file = "AnnotationUsageDslTest.java" region = "api"}
   @Contract(value = " -> new", pure = true)
   static AnnotationUsageTypeStep annotationUsage()
   {
      return new AnnotationUsageDsl();
   }

   /// {@code Dsl.annotationValue("value").render(DEFAULT)} -> {@code "value"}
   @Contract(value = "_ -> new", pure = true)
   static AnnotationValueRenderable annotationValue(String value)
   {
      requireNonNull(value);
      return renderingContext -> '\"' + value + '\"';
   }

   /// {@code Dsl.annotationValue(true).render(DEFAULT)} -> {@code true}
   @Contract(value = "_ -> new", pure = true)
   static AnnotationValueRenderable annotationValue(boolean value)
   {
      return renderingContext -> String.valueOf(value);
   }

   /// {@code Dsl.annotationValue((byte) 1).render(DEFAULT)} -> {@code 1}
   @Contract(value = "_ -> new", pure = true)
   static AnnotationValueRenderable annotationValue(byte value)
   {
      return renderingContext -> Byte.toString(value);
   }

   /// {@code Dsl.annotationValue((short) 2).render(DEFAULT)} -> {@code 2}
   @Contract(value = "_ -> new", pure = true)
   static AnnotationValueRenderable annotationValue(short value)
   {
      return renderingContext -> Short.toString(value);
   }

   /// {@code Dsl.annotationValue(3).render(DEFAULT)} -> {@code 3}
   @Contract(value = "_ -> new", pure = true)
   static AnnotationValueRenderable annotationValue(int value)
   {
      return renderingContext -> Integer.toString(value);
   }

   /// {@code Dsl.annotationValue(3L).render(DEFAULT)} -> {@code 3L}
   @Contract(value = "_ -> new", pure = true)
   static AnnotationValueRenderable annotationValue(long value)
   {
      return renderingContext -> Long.toString(value) + 'L';
   }

   /// {@code Dsl.annotationValue('c').render(DEFAULT)} -> {@code 'c'}
   @Contract(value = "_ -> new", pure = true)
   static AnnotationValueRenderable annotationValue(char value)
   {
      return renderingContext -> '\'' + Character.toString(value) + '\'';
   }

   /// {@code Dsl.annotationValue(4F).render(DEFAULT)} -> {@code 4.0F}
   @Contract(value = "_ -> new", pure = true)
   static AnnotationValueRenderable annotationValue(float value)
   {
      return renderingContext -> Float.toString(value) + 'F';
   }

   /// {@code Dsl.annotationValue(5D).render(DEFAULT)} -> {@code 5.0D}
   @Contract(value = "_ -> new", pure = true)
   static AnnotationValueRenderable annotationValue(double value)
   {
      return renderingContext -> Double.toString(value) + 'D';
   }

   /// {@code Dsl.annotationValue(cEnumConstant).render(DEFAULT)} -> {@code org.example.Enum.CONSTANT}
   @Contract(value = "_ -> new", pure = true)
   static AnnotationValueRenderable annotationValue(C.EnumConstant value)
   {
      requireNonNull(value);
      return value::renderDeclaration;
   }

   /// {@code Dsl.annotationValue(Enum.CONSTANT).render(DEFAULT)} -> {@code org.example.Enum.CONSTANT}
   @Contract(value = "_ -> new", pure = true)
   static AnnotationValueRenderable annotationValue(Enum<?> value)
   {
      requireNonNull(value);
      return renderingContext -> value.getClass().getName() + '.' + value.name();
   }

   /// {@code Dsl.annotationValue(String.class).render(DEFAULT)} -> {@code java.lang.String.class}
   @Contract(value = "_ -> new", pure = true)
   static AnnotationValueRenderable annotationValue(Class<?> value)
   {
      requireNonNull(value);
      return renderingContext -> value.getName() + ".class";
   }

   /// {@code Dsl.annotationValue(cArray).render(DEFAULT)} -> {@code boolean[].class}
   @Contract(value = "_ -> new", pure = true)
   static AnnotationValueRenderable annotationValue(C.Type value)
   {
      requireNonNull(value);
      return renderingContext ->
            switch (value)
            {
               case C.Array array -> array.renderType(renderingContext);
               case C.Declared declared -> requestOrThrow(declared, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME);
               case C.Generic unused -> throw new IllegalArgumentException("generic can not be used as an Annotation Value " + value);
               case C.Null unused -> throw new IllegalArgumentException("null can not be used as an Annotation Value " + value);
               case C.Void unused -> "void";
               case C.Wildcard unused -> throw new IllegalArgumentException("Wildcard can not be used as an Annotation Value " + value);
               case C.Primitive primitive -> primitive.renderType(renderingContext);
               default -> throw new IllegalArgumentException("Can not be used as an Annotation Value " + value);
            }
            + ".class";
   }

   /// {@code Dsl.annotationValue(cAnnotationUsage).render(DEFAULT)} -> {@code @org.example.MyAnnotation}
   @Contract(value = "_ -> new", pure = true)
   static AnnotationValueRenderable annotationValue(C.AnnotationUsage value)
   {
      requireNonNull(value);
      return value::renderDeclaration;
   }

   /// {@code Dsl.annotationValue(Dsl.annotationValue(1), Dsl.annotationValue(1F)).render(DEFAULT)} -> {@code {1, 1.0F}}
   @Contract(value = "_ -> new", pure = true)
   static AnnotationValueRenderable annotationValue(AnnotationValueRenderable... value)
   {
      requireNonNull(value);
      return annotationValue(List.of(value));
   }

   /// {@code Dsl.annotationValue(List.of(Dsl.annotationValue(1), Dsl.annotationValue(1F))).render(DEFAULT)} -> {@code {1, 1.0F}}
   @Contract(value = "_ -> new", pure = true)
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
   @Contract(value = "_ -> new", pure = true)
   static GenericRenderable generic(String name)
   {
      return generic().name(name);
   }

   /// {@snippet file = "GenericDslTest.java" region = "api"}
   ///
   /// Recursive generics are only possible to declare with a workaround
   ///
   /// {@snippet file = "GenericDslTest.java" region = "recursive"}
   @Contract(value = " -> new", pure = true)
   static GenericAnnotateStep generic()
   {
      return new GenericDsl();
   }

   /// {@snippet file = "FieldDslTest.java" region = "api"}
   @Contract(value = " -> new", pure = true)
   static FieldJavaDocStep field()
   {
      return new FieldDsl();
   }

   /// a [javax.annotation.processing.Generated] annotation
   @Contract(value = "_, _ -> new", pure = true)
   static AnnotationUsageRenderable generated(String generatorName, String comment)
   {
      return annotationUsage().type("javax.annotation.processing.Generated")
                              .noName()
                              .value(annotationValue(generatorName))
                              .name("date")
                              .value(annotationValue(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                              .name("comments")
                              .value(annotationValue(comment));
   }
}