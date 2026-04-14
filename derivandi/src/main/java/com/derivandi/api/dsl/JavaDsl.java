package com.derivandi.api.dsl;

import com.derivandi.api.Ap;
import com.derivandi.api.dsl.annotation.AnnotationCopyrightHeaderStep;
import com.derivandi.api.dsl.annotation.AnnotationOuterStep;
import com.derivandi.api.dsl.annotation_usage.AnnotationUsageNameStep;
import com.derivandi.api.dsl.annotation_usage.AnnotationUsageRenderable;
import com.derivandi.api.dsl.annotation_usage.AnnotationUsageTypeStep;
import com.derivandi.api.dsl.annotation_value.AnnotationValueRenderable;
import com.derivandi.api.dsl.class_.ClassCopyrightHeaderStep;
import com.derivandi.api.dsl.class_.ClassOuterStep;
import com.derivandi.api.dsl.constructor.ConstructorJavaDocStep;
import com.derivandi.api.dsl.enum_.EnumCopyrightHeaderStep;
import com.derivandi.api.dsl.enum_.EnumOuterStep;
import com.derivandi.api.dsl.enum_constant.EnumConstantJavaDocStep;
import com.derivandi.api.dsl.exports.ExportsPackageStep;
import com.derivandi.api.dsl.exports.ExportsRenderable;
import com.derivandi.api.dsl.field.FieldJavaDocStep;
import com.derivandi.api.dsl.generic.GenericAnnotateStep;
import com.derivandi.api.dsl.generic.GenericRenderable;
import com.derivandi.api.dsl.import_.ImportRenderable;
import com.derivandi.api.dsl.import_.ImportStaticStep;
import com.derivandi.api.dsl.interface_.InterfaceCopyrightHeaderStep;
import com.derivandi.api.dsl.interface_.InterfaceOuterStep;
import com.derivandi.api.dsl.method.MethodJavaDocStep;
import com.derivandi.api.dsl.module.ModuleCopyrightHeaderStep;
import com.derivandi.api.dsl.opens.OpensPackageStep;
import com.derivandi.api.dsl.opens.OpensRenderable;
import com.derivandi.api.dsl.package_.PackageJavaDocStep;
import com.derivandi.api.dsl.package_.PackageRenderable;
import com.derivandi.api.dsl.parameter.ParameterAnnotateStep;
import com.derivandi.api.dsl.parameter.ParameterRenderable;
import com.derivandi.api.dsl.provides.ProvidesServiceStep;
import com.derivandi.api.dsl.receiver.ReceiverAnnotateStep;
import com.derivandi.api.dsl.record.RecordCopyrightHeaderStep;
import com.derivandi.api.dsl.record.RecordOuterStep;
import com.derivandi.api.dsl.record_component.RecordComponentAnnotateStep;
import com.derivandi.api.dsl.requires.RequiresModifierStep;
import com.derivandi.api.dsl.result.ResultAnnotateStep;
import com.derivandi.api.dsl.uses.UsesRenderable;
import com.derivandi.internal.dsl.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

public interface JavaDsl
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

   /// {@snippet class = "com.derivandi.dsl.ConstructorDslTest" region = "api"}
   @Contract(value = " -> new", pure = true)
   static ConstructorJavaDocStep constructor()
   {
      return new ConstructorDsl();
   }

   /// {@snippet class = "com.derivandi.dsl.MethodDslTest" region = "api"}
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

   /// {@snippet class = "com.derivandi.dsl.ModuleDslTest" region = api}
   @Contract(value = " -> new", pure = true)
   static ModuleCopyrightHeaderStep moduleInfo()
   {
      return new ModuleDsl();
   }

   /// {@snippet class = "com.derivandi.dsl.DirectiveDslTest" region = "exports-api-simple-string"}
   ///
   /// @see #exports()
   @Contract(value = "_ -> new", pure = true)
   static ExportsRenderable exports(String packageName)
   {
      return exports().package_(packageName);
   }

   /// {@snippet class = "com.derivandi.dsl.DirectiveDslTest" region = "exports-api-simple-type"}
   ///
   /// @see #exports()
   @Contract(value = "_ -> new", pure = true)
   static ExportsRenderable exports(PackageRenderable aPackage)
   {
      return exports().package_(aPackage);
   }

   /// {@snippet class = "com.derivandi.dsl.DirectiveDslTest" region = "exports-api"}
   @Contract(value = " -> new", pure = true)
   static ExportsPackageStep exports()
   {
      return new ExportsDsl();
   }

   /// {@snippet class = "com.derivandi.dsl.DirectiveDslTest" region = "opens-api-simple-type"}
   ///
   /// @see #opens()
   @Contract(value = "_ -> new", pure = true)
   static OpensRenderable opens(PackageRenderable aPackage)
   {
      return opens().package_(aPackage);
   }

   /// {@snippet class = "com.derivandi.dsl.DirectiveDslTest" region = "opens-api-simple-string"}
   ///
   /// @see #opens()
   @Contract(value = "_ -> new", pure = true)
   static OpensRenderable opens(String packageName)
   {
      return opens().package_(packageName);
   }

   /// {@snippet class = "com.derivandi.dsl.DirectiveDslTest" region = "opens-api"}
   @Contract(value = " -> new", pure = true)
   static OpensPackageStep opens()
   {
      return new OpensDsl();
   }

   /// {@snippet class = "com.derivandi.dsl.DirectiveDslTest" region = "requires-api"}
   @Contract(value = " -> new", pure = true)
   static RequiresModifierStep requires()
   {
      return new RequiresDsl();
   }

   /// {@snippet class = "com.derivandi.dsl.DirectiveDslTest" region = provides-api}
   @Contract(value = " -> new", pure = true)
   static ProvidesServiceStep provides()
   {
      return new ProvidesDsl();
   }

   /// {@snippet class = "com.derivandi.dsl.DirectiveDslTest" region = "uses-api-string"}
   @Contract(value = "_ -> new", pure = true)
   static UsesRenderable uses(String serviceName)
   {
      return renderingContext -> "uses " + serviceName + ';';
   }

   /// {@snippet class = "com.derivandi.dsl.DirectiveDslTest" region = "uses-api-type"}
   @Contract(value = "_ -> new", pure = true)
   static UsesRenderable uses(Ap.Declared service)
   {
      return uses(service.getQualifiedName());
   }

   @Contract(value = " -> new", pure = true)
   static PackageJavaDocStep packageInfo()
   {
      return new PackageDsl();
   }

   /// {@snippet class = "com.derivandi.dsl.ParameterDslTest" region = "api"}
   @Contract(value = " -> new", pure = true)
   static ParameterAnnotateStep parameter()
   {
      return new ParameterDsl();
   }

   /// {@snippet class = "com.derivandi.dsl.ParameterDslTest" region = "short-api"}
   ///
   /// @see #parameter()
   @Contract(value = "_, _ -> new", pure = true)
   static ParameterRenderable parameter(String type, String name)
   {
      return new ParameterDsl()
            .type(type)
            .name(name);
   }

   /// {@snippet class = "com.derivandi.dsl.ParameterDslTest" region = "short-api"}
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

   /// {@snippet class = com.derivandi.dsl.ResultDslTest region = "api"}
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

   /// {@snippet class = "com.derivandi.dsl.AnnotationUsageDslTest" region = "api"}
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
   static AnnotationValueRenderable annotationValue(Ap.EnumConstant value)
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
   static AnnotationValueRenderable annotationValue(Ap.Type value)
   {
      requireNonNull(value);
      return renderingContext ->
            switch (value)
            {
               case Ap.Array array -> array.renderType(renderingContext);
               case Ap.Declared declared -> declared.getQualifiedName();
               case Ap.Generic unused -> throw new IllegalArgumentException("generic can not be used as an Annotation Value " + value);
               case Ap.Null unused -> throw new IllegalArgumentException("null can not be used as an Annotation Value " + value);
               case Ap.Void unused -> "void";
               case Ap.Wildcard unused -> throw new IllegalArgumentException("Wildcard can not be used as an Annotation Value " + value);
               case Ap.Primitive primitive -> primitive.renderType(renderingContext);
            }
            + ".class";
   }

   /// {@code Dsl.annotationValue(cAnnotationUsage).render(DEFAULT)} -> {@code @org.example.MyAnnotation}
   @Contract(value = "_ -> new", pure = true)
   static AnnotationValueRenderable annotationValue(Ap.AnnotationUsage value)
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

   /// {@snippet class = "com.derivandi.dsl.GenericDslTest" region = "api"}
   ///
   /// Recursive generics are only possible to declare with a workaround
   ///
   /// {@snippet class = "com.derivandi.dsl.GenericDslTest" region = "recursive"}
   @Contract(value = " -> new", pure = true)
   static GenericAnnotateStep generic()
   {
      return new GenericDsl();
   }

   /// {@snippet class = "com.derivandi.dsl.FieldDslTest" region = "api"}
   @Contract(value = " -> new", pure = true)
   static FieldJavaDocStep field()
   {
      return new FieldDsl();
   }

   /// a [javax.annotation.processing.Generated] annotation
   @Contract(value = "_, _ -> new", pure = true)
   static AnnotationUsageRenderable generated(String generatorName)
   {
      return generated(generatorName, null);
   }

   /// a [javax.annotation.processing.Generated] annotation
   @Contract(value = "_, _ -> new", pure = true)
   static AnnotationUsageRenderable generated(String generatorName, @Nullable String comment)
   {
      AnnotationUsageNameStep step = annotationUsage()
            .type("javax.annotation.processing.Generated")
            .name("value")
            .value(annotationValue(generatorName))
            .name("date")
            .value(annotationValue(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));

      if (comment == null)
      {
         return step;
      }
      return step.name("comments")
                 .value(annotationValue(comment));
   }
}