package io.determann.shadow.api;

import io.determann.shadow.api.dsl.*;
import io.determann.shadow.api.dsl.annotation.AnnotationRenderable;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageNameStep;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.dsl.annotation_value.AnnotationValueRenderable;
import io.determann.shadow.api.dsl.array.ArrayRenderable;
import io.determann.shadow.api.dsl.class_.*;
import io.determann.shadow.api.dsl.constructor.*;
import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.dsl.enum_.*;
import io.determann.shadow.api.dsl.enum_constant.EnumConstantRenderable;
import io.determann.shadow.api.dsl.exports.ExportsRenderable;
import io.determann.shadow.api.dsl.field.FieldInitializationStep;
import io.determann.shadow.api.dsl.field.FieldRenderable;
import io.determann.shadow.api.dsl.generic.GenericExtendsStep;
import io.determann.shadow.api.dsl.generic.GenericRenderable;
import io.determann.shadow.api.dsl.interface_.*;
import io.determann.shadow.api.dsl.method.MethodReceiverStep;
import io.determann.shadow.api.dsl.method.MethodRenderable;
import io.determann.shadow.api.dsl.module.ModuleInfoRenderable;
import io.determann.shadow.api.dsl.module.ModuleNameRenderable;
import io.determann.shadow.api.dsl.opens.OpensRenderable;
import io.determann.shadow.api.dsl.package_.PackageInfoRenderable;
import io.determann.shadow.api.dsl.package_.PackageRenderable;
import io.determann.shadow.api.dsl.parameter.*;
import io.determann.shadow.api.dsl.provides.ProvidesRenderable;
import io.determann.shadow.api.dsl.receiver.ReceiverRenderable;
import io.determann.shadow.api.dsl.record.*;
import io.determann.shadow.api.dsl.record_component.RecordComponentRenderable;
import io.determann.shadow.api.dsl.requires.RequiresNameStep;
import io.determann.shadow.api.dsl.requires.RequiresRenderable;
import io.determann.shadow.api.dsl.result.ResultRenderable;
import io.determann.shadow.api.dsl.uses.UsesRenderable;
import io.determann.shadow.api.query.ImplementationDefined;
import io.determann.shadow.api.query.Operations;
import io.determann.shadow.api.query.Provider;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.determann.shadow.api.dsl.Dsl.*;
import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrEmpty;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;

public interface C
{
   interface Type
         extends ImplementationDefined,
                 TypeRenderable {}

   /**
    * {@snippet id = "test":
    *  List<? extends Number>//@highlight substring="? extends Number"
    *}
    * or
    * {@snippet :
    *  List<? super Number>//@highlight substring="? super Number"
    *}
    */
   interface Wildcard
         extends Type,
                 Erasable
   {
      @Override
      default String renderName(RenderingContext renderingContext)
      {
         Optional<Type> superType = Provider.requestOrEmpty(this, WILDCARD_GET_SUPER);
         if (superType.isPresent())
         {
            return "? super " + superType.get().renderName(renderingContext);
         }
         Optional<Type> extendsType = Provider.requestOrEmpty(this, WILDCARD_GET_EXTENDS);
         if (extendsType.isPresent())
         {
            return "? extends " + extendsType.get();
         }
         return "?";
      }
   }

   interface Void
         extends Type
   {
      @Override
      default String renderName(RenderingContext renderingContext)
      {
         return "void";
      }
   }

   /// a type that can be used as method/ constructor parameter or field
   interface VariableType
         extends Type,
                 VariableTypeRenderable {}

   interface Null
         extends Type
   {
      @Override
      default String renderName(RenderingContext renderingContext)
      {
         return "null";
      }
   }

   interface Array
         extends ReferenceType,
                 Erasable,
                 ArrayRenderable
   {
      @Override
      default String renderType(RenderingContext renderingContext)
      {
         return Provider.requestOrThrow(this, Operations.ARRAY_GET_COMPONENT_TYPE)
                        .renderName(renderingContext) + "[]";
      }

      @Override
      default String renderName(RenderingContext renderingContext)
      {
         return renderType(renderingContext);
      }
   }

   interface ReferenceType
         extends VariableType,
                 ReferenceTypeRenderable {}

   /**
    * Anything that can be a file.
    */
   interface Declared
         extends ReferenceType,
                 Annotationable,
                 AccessModifiable,
                 StrictfpModifiable,
                 Nameable,
                 QualifiedNameable,
                 ModuleEnclosed,
                 Documented,
                 DeclaredRenderable {}

   interface Record
         extends Declared,
                 StaticModifiable,
                 FinalModifiable,
                 Erasable,
                 RecordRenderable
   {
      @Override
      default String renderDeclaration(RenderingContext renderingContext)
      {
         RecordAnnotateStep annotateStep = Dsl.record().package_(requestOrThrow(this, DECLARED_GET_PACKAGE));
         RecordModifierStep modifierStep = requestOrEmpty(this, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).map(annotateStep::annotate)
                                                                                                            .orElse(annotateStep);
         RecordNameStep nameStep = requestOrEmpty(this, MODIFIABLE_GET_MODIFIERS).map(modifierStep::modifier).orElse(modifierStep);
         RecordRecordComponentStep recordComponentStep = nameStep.name(requestOrThrow(this, NAMEABLE_GET_NAME));
         RecordGenericStep genericStep = Provider.requestOrEmpty(this, RECORD_GET_RECORD_COMPONENTS)
                                                 .map(recordComponentStep::component)
                                                 .orElse(recordComponentStep);
         RecordImplementsStep implementsStep = Provider.requestOrEmpty(this, RECORD_GET_GENERICS).map(genericStep::generic).orElse(genericStep);
         RecordBodyStep bodyStep = requestOrEmpty(this, DECLARED_GET_DIRECT_INTERFACES).map(implementsStep::implements_).orElse(implementsStep);
         bodyStep = requestOrEmpty(this, DECLARED_GET_FIELDS).map(bodyStep::field).orElse(bodyStep);
         bodyStep = requestOrEmpty(this, DECLARED_GET_METHODS).map(bodyStep::method).orElse(bodyStep);
         bodyStep = Provider.requestOrEmpty(this, RECORD_GET_CONSTRUCTORS).map(bodyStep::constructor).orElse(bodyStep);
         return bodyStep.renderDeclaration(renderingContext);
      }

      @Override
      default String renderQualifiedName(RenderingContext renderingContext)
      {
         return requestOrThrow(this, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME);
      }

      @Override
      default String renderType(RenderingContext renderingContext)
      {
         return record().package_(requestOrThrow(this, DECLARED_GET_PACKAGE))
                        .name(requestOrThrow(this, NAMEABLE_GET_NAME))
                        .generic(Provider.requestOrEmpty(this, RECORD_GET_GENERICS).orElse(emptyList()))
                        .renderType(renderingContext);
      }

      @Override
      default String renderName(RenderingContext renderingContext)
      {
         return renderQualifiedName(renderingContext);
      }
   }

   interface Annotation
         extends Declared,
                 StaticModifiable,
                 AnnotationRenderable
   {
      @Override
      default String renderDeclaration(RenderingContext renderingContext)
      {
         return annotation().package_(requestOrThrow(this, DECLARED_GET_PACKAGE))
                            .annotate(requestOrEmpty(this, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).orElse(emptyList()))
                            .modifier(requestOrEmpty(this, MODIFIABLE_GET_MODIFIERS).orElse(emptySet()))
                            .name(requestOrThrow(this, NAMEABLE_GET_NAME))
                            .field(requestOrEmpty(this, DECLARED_GET_FIELDS).orElse(emptyList()))
                            .method(requestOrEmpty(this, DECLARED_GET_METHODS).orElse(emptyList()))
                            .renderDeclaration(renderingContext);
      }

      @Override
      default String renderType(RenderingContext renderingContext)
      {
         return renderQualifiedName(renderingContext);
      }

      @Override
      default String renderName(RenderingContext renderingContext)
      {
         return renderQualifiedName(renderingContext);
      }

      @Override
      default String renderQualifiedName(RenderingContext renderingContext)
      {
         return requestOrThrow(this, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME);
      }
   }

   interface Class
         extends Declared,
                 AbstractModifiable,
                 StaticModifiable,
                 Sealable,
                 FinalModifiable,
                 Erasable,
                 ClassRenderable
   {
      @Override
      default String renderDeclaration(RenderingContext renderingContext)
      {
         ClassAnnotateStep annotateStep = class_().package_(requestOrThrow(this, DECLARED_GET_PACKAGE));
         ClassModifierStep modifierStep = requestOrEmpty(this, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).map(annotateStep::annotate)
                                                                                                           .orElse(annotateStep);
         ClassNameStep nameStep = requestOrEmpty(this, MODIFIABLE_GET_MODIFIERS).map(modifierStep::modifier).orElse(modifierStep);
         ClassGenericStep genericStep = nameStep.name(requestOrThrow(this, NAMEABLE_GET_NAME));
         ClassExtendsStep extendsStep = Provider.requestOrEmpty(this, CLASS_GET_GENERICS).map(genericStep::generic).orElse(genericStep);
         ClassImplementsStep implementsStep = Provider.requestOrEmpty(this, CLASS_GET_SUPER_CLASS)
                                                      .map(extendsStep::extends_)
                                                      .orElse(extendsStep);
         ClassPermitsStep permitsStep = requestOrEmpty(this, DECLARED_GET_DIRECT_INTERFACES).map(implementsStep::implements_)
                                                                                            .orElse(implementsStep);
         ClassBodyStep bodyStep = Provider.requestOrEmpty(this, CLASS_GET_PERMITTED_SUB_CLASSES).map(permitsStep::permits).orElse(permitsStep);
         bodyStep = requestOrEmpty(this, DECLARED_GET_FIELDS).map(bodyStep::field).orElse(bodyStep);
         bodyStep = requestOrEmpty(this, DECLARED_GET_METHODS).map(bodyStep::method).orElse(bodyStep);
         bodyStep = Provider.requestOrEmpty(this, CLASS_GET_CONSTRUCTORS).map(bodyStep::constructor).orElse(bodyStep);
         return bodyStep.renderDeclaration(renderingContext);
      }

      @Override
      default String renderQualifiedName(RenderingContext renderingContext)
      {
         return requestOrThrow(this, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME);
      }

      @Override
      default String renderType(RenderingContext renderingContext)
      {
         return class_().package_(requestOrThrow(this, DECLARED_GET_PACKAGE))
                        .name(requestOrThrow(this, NAMEABLE_GET_NAME))
                        .generic(Provider.requestOrEmpty(this, CLASS_GET_GENERICS).orElse(emptyList()))
                        .renderType(renderingContext);
      }

      @Override
      default String renderName(RenderingContext renderingContext)
      {
         return renderQualifiedName(renderingContext);
      }
   }

   interface Interface
         extends Declared,
                 AbstractModifiable,
                 StaticModifiable,
                 Sealable,
                 Erasable,
                 InterfaceRenderable
   {
      @Override
      default String renderDeclaration(RenderingContext renderingContext)
      {
         InterfaceImportStep annotateStep = interface_().package_(requestOrThrow(this, DECLARED_GET_PACKAGE));
         InterfaceModifierStep modifierStep = requestOrEmpty(this, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).map(annotateStep::annotate)
                                                                                                               .orElse(annotateStep);
         InterfaceNameStep nameStep = requestOrEmpty(this, MODIFIABLE_GET_MODIFIERS).map(modifierStep::modifier).orElse(modifierStep);
         InterfaceGenericStep genericStep = nameStep.name(requestOrThrow(this, NAMEABLE_GET_NAME));
         InterfaceExtendsStep extendsStep = Provider.requestOrEmpty(this, INTERFACE_GET_GENERICS).map(genericStep::generic).orElse(genericStep);
         InterfacePermitsStep permitsStep = requestOrEmpty(this, DECLARED_GET_DIRECT_INTERFACES).map(extendsStep::extends_).orElse(extendsStep);
         InterfaceBodyStep bodyStep = Provider.requestOrEmpty(this, INTERFACE_GET_PERMITTED_SUB_TYPES)
                                              .map(permitsStep::permits)
                                              .orElse(permitsStep);
         bodyStep = requestOrEmpty(this, DECLARED_GET_FIELDS).map(bodyStep::field).orElse(bodyStep);
         bodyStep = requestOrEmpty(this, DECLARED_GET_METHODS).map(bodyStep::method).orElse(bodyStep);
         return bodyStep.renderDeclaration(renderingContext);
      }

      @Override
      default String renderQualifiedName(RenderingContext renderingContext)
      {
         return requestOrThrow(this, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME);
      }

      @Override
      default String renderType(RenderingContext renderingContext)
      {
         return class_().package_(requestOrThrow(this, DECLARED_GET_PACKAGE))
                        .name(requestOrThrow(this, NAMEABLE_GET_NAME))
                        .generic(Provider.requestOrEmpty(this, INTERFACE_GET_GENERICS).orElse(emptyList()))
                        .renderType(renderingContext);
      }

      @Override
      default String renderName(RenderingContext renderingContext)
      {
         return renderQualifiedName(renderingContext);
      }
   }

   interface Enum
         extends Declared,
                 StaticModifiable,
                 EnumRenderable
   {
      @Override
      default String renderDeclaration(RenderingContext renderingContext)
      {
         EnumAnnotateStep annotateStep = enum_().package_(requestOrThrow(this, DECLARED_GET_PACKAGE));
         EnumModifierStep modifierStep = requestOrEmpty(this, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).map(annotateStep::annotate)
                                                                                                          .orElse(annotateStep);
         EnumNameStep nameStep = requestOrEmpty(this, MODIFIABLE_GET_MODIFIERS).map(modifierStep::modifier).orElse(modifierStep);
         EnumImplementsStep implementsStep = nameStep.name(requestOrThrow(this, NAMEABLE_GET_NAME));
         EnumEnumConstantStep enumEnumConstantStep = requestOrEmpty(this, DECLARED_GET_DIRECT_INTERFACES).map(implementsStep::implements_)
                                                                                                         .orElse(implementsStep);
         EnumBodyStep bodyStep = Provider.requestOrEmpty(this, ENUM_GET_ENUM_CONSTANTS)
                                         .map(enumEnumConstantStep::enumConstant)
                                         .orElse(enumEnumConstantStep);
         bodyStep = requestOrEmpty(this, DECLARED_GET_FIELDS).map(bodyStep::field).orElse(bodyStep);
         bodyStep = requestOrEmpty(this, DECLARED_GET_METHODS).map(bodyStep::method).orElse(bodyStep);
         bodyStep = Provider.requestOrEmpty(this, ENUM_GET_CONSTRUCTORS).map(bodyStep::constructor).orElse(bodyStep);
         return bodyStep.renderDeclaration(renderingContext);
      }

      @Override
      default String renderQualifiedName(RenderingContext renderingContext)
      {
         return requestOrThrow(this, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME);
      }

      @Override
      default String renderType(RenderingContext renderingContext)
      {
         return class_().package_(requestOrThrow(this, DECLARED_GET_PACKAGE))
                        .name(requestOrThrow(this, NAMEABLE_GET_NAME))
                        .renderType(renderingContext);
      }

      @Override
      default String renderName(RenderingContext renderingContext)
      {
         return renderQualifiedName(renderingContext);
      }
   }

   /**
    * represents the generic parameter at a class, method, constructor etc.
    */
   interface Generic
         extends ReferenceType,
                 Nameable,
                 Annotationable,
                 Erasable,
                 GenericRenderable
   {
      @Override
      default String renderDeclaration(RenderingContext renderingContext)
      {
         GenericExtendsStep extendsStep = generic().name(requestOrThrow(this, NAMEABLE_GET_NAME));

         Optional<Type> extendsType = Provider.requestOrEmpty(this, GENERIC_GET_BOUND);
         if (extendsType.isPresent())
         {
            return (switch (extendsType.get())
                    {
                       case Class cClass -> extendsStep.extends_(cClass);
                       case Interface cInterface -> extendsStep.extends_(cInterface);
                       case Generic generic -> extendsStep.extends_(generic);
                       default -> throw new IllegalStateException();
                    }).renderDeclaration(renderingContext);
         }
         return extendsStep.renderDeclaration(renderingContext);
      }

      @Override
      default String renderType(RenderingContext renderingContext)
      {
         return renderName(renderingContext);
      }

      @Override
      default String renderName(RenderingContext renderingContext)
      {
         return requestOrThrow(this, NAMEABLE_GET_NAME);
      }
   }

   /**
    * represents primitive types, but not there wrapper classes. for example int, long, short
    */
   interface Primitive
         extends VariableType,
                 Nameable
   {
      @Override
      default String renderType(RenderingContext renderingContext)
      {
         return renderName(renderingContext);
      }
   }

   interface boolean_
         extends Primitive
   {
      @Override
      default String renderName(RenderingContext renderingContext)
      {
         return "boolean";
      }
   }

   interface byte_
         extends Primitive
   {
      @Override
      default String renderName(RenderingContext renderingContext)
      {
         return "byte";
      }
   }

   interface char_
         extends Primitive
   {
      @Override
      default String renderName(RenderingContext renderingContext)
      {
         return "char";
      }
   }

   interface double_
         extends Primitive
   {
      @Override
      default String renderName(RenderingContext renderingContext)
      {
         return "double";
      }
   }

   interface float_
         extends Primitive
   {
      @Override
      default String renderName(RenderingContext renderingContext)
      {
         return "float";
      }
   }

   interface int_
         extends Primitive
   {
      @Override
      default String renderName(RenderingContext renderingContext)
      {
         return "int";
      }
   }

   interface long_
         extends Primitive
   {
      @Override
      default String renderName(RenderingContext renderingContext)
      {
         return "long";
      }
   }

   interface short_
         extends Primitive
   {
      @Override
      default String renderName(RenderingContext renderingContext)
      {
         return "short";
      }
   }

   interface Executable
         extends Annotationable,
                 Nameable,
                 Modifiable,
                 ModuleEnclosed,
                 Documented {}

   interface Constructor
         extends Executable,
                 AccessModifiable,
                 ConstructorRenderable
   {

      @Override
      default String renderDeclaration(RenderingContext renderingContext)
      {
         ConstructorAnnotateStep annotateStep = constructor();

         ConstructorModifierStep modifierStep = requestOrEmpty(this, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).map(annotateStep::annotate)
                                                                                                                 .orElse(annotateStep);
         ConstructorGenericStep genericStep = requestOrEmpty(this, MODIFIABLE_GET_MODIFIERS).map(modifierStep::modifier).orElse(modifierStep);
         genericStep = requestOrEmpty(this, EXECUTABLE_GET_GENERICS).map(genericStep::generic).orElse(genericStep);

         Declared type = requestOrThrow(this, EXECUTABLE_GET_SURROUNDING);
         ConstructorReceiverStep receiverStep = switch (type)
         {
            case Class cClass -> genericStep.type(cClass);
            case Enum cEnum -> genericStep.type(cEnum);
            case Record cRecord -> genericStep.type(cRecord);
            default -> throw new IllegalStateException("Unexpected value: " + type);
         };

         ConstructorParameterStep parameterStep = requestOrEmpty(this, EXECUTABLE_GET_RECEIVER)
               .map(receiverStep::receiver)
               .orElse(receiverStep);

         ConstructorThrowsStep throwsStep = parameterStep.parameter(requestOrThrow(this, EXECUTABLE_GET_PARAMETERS));
         throwsStep = requestOrEmpty(this, EXECUTABLE_GET_THROWS).map(throwsStep::throws_).orElse(throwsStep);
         return throwsStep.renderDeclaration(renderingContext);
      }
   }

   interface Method
         extends Executable,
                 StaticModifiable,
                 DefaultModifiable,
                 AccessModifiable,
                 AbstractModifiable,
                 FinalModifiable,
                 StrictfpModifiable,
                 NativeModifiable,
                 MethodRenderable
   {
      @Override
      default String renderDeclaration(RenderingContext renderingContext)
      {
         MethodReceiverStep receiverStep = method()
               .annotate(requestOrEmpty(this, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).orElse(emptyList()))
               .modifier(requestOrEmpty(this, MODIFIABLE_GET_MODIFIERS).orElse(emptySet()))
               .generic(requestOrEmpty(this, EXECUTABLE_GET_GENERICS).orElse(emptyList()))
               .result(Provider.requestOrThrow(this, METHOD_GET_RESULT))
               .name(requestOrThrow(this, NAMEABLE_GET_NAME));

         return requestOrEmpty(this, EXECUTABLE_GET_RECEIVER).map(receiverStep::receiver).orElse(receiverStep)
                                                             .parameter(requestOrThrow(this, EXECUTABLE_GET_PARAMETERS))
                                                             .throws_(requestOrEmpty(this, EXECUTABLE_GET_THROWS).orElse(emptyList()))
                                                             .renderDeclaration(renderingContext);
      }

      @Override
      default String renderName(RenderingContext renderingContext)
      {
         return requestOrThrow(this, NAMEABLE_GET_NAME);
      }
   }

   interface Variable
         extends Nameable,
                 Annotationable,
                 Modifiable,
                 ModuleEnclosed {}

   interface EnumConstant
         extends Variable,
                 Documented,
                 EnumConstantRenderable
   {
      @Override
      default String renderDeclaration(RenderingContext renderingContext)
      {
         return enumConstant()
               .annotate(requestOrEmpty(this, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).orElse(emptyList()))
               .name(requestOrThrow(this, NAMEABLE_GET_NAME)).renderDeclaration(renderingContext);
      }
   }

   interface Field
         extends Variable,
                 AccessModifiable,
                 FinalModifiable,
                 StaticModifiable,
                 Documented,
                 FieldRenderable
   {
      @Override
      default String renderDeclaration(RenderingContext renderingContext)
      {
         FieldInitializationStep initializationStep = field()
               .annotate(requestOrEmpty(this, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).orElse(emptyList()))
               .modifier(requestOrEmpty(this, MODIFIABLE_GET_MODIFIERS).orElse(emptySet()))
               .type(requestOrThrow(this, VARIABLE_GET_TYPE))
               .name(requestOrThrow(this, NAMEABLE_GET_NAME));

         if (!Provider.requestOrEmpty(this, FIELD_IS_CONSTANT).orElse(false))
         {
            return initializationStep.renderDeclaration(renderingContext);
         }
         return initializationStep.initializer(Provider.requestOrThrow(this, FIELD_GET_CONSTANT_VALUE).toString())
                                  .renderDeclaration(renderingContext);
      }

      @Override
      default String renderName(RenderingContext renderingContext)
      {
         return requestOrThrow(this, NAMEABLE_GET_NAME);
      }
   }

   /// Parameter of a method or constructor
   ///
   /// @see Operations#EXECUTABLE_GET_PARAMETERS
   interface Parameter
         extends Variable,
                 FinalModifiable,
                 ParameterRenderable
   {
      @Override
      default String renderDeclaration(RenderingContext renderingContext)
      {
         ParameterAnnotateStep annotateStep = parameter()
               .annotate(requestOrEmpty(this, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).orElse(emptyList()));

         ParameterTypeStep parameterTypeStep = annotateStep;
         if (requestOrEmpty(this, MODIFIABLE_HAS_MODIFIER, Modifier.STATIC).orElse(false))
         {
            parameterTypeStep = annotateStep.final_();
         }

         ParameterNameStep nameStep = parameterTypeStep.type(requestOrThrow(this, VARIABLE_GET_TYPE));
         ParameterVarargsStep varargsStep = nameStep.name(requestOrThrow(this, NAMEABLE_GET_NAME));

         if (!Provider.requestOrEmpty(this, PARAMETER_IS_VAR_ARGS).orElse(false))
         {
            return varargsStep.renderDeclaration(renderingContext);
         }
         return varargsStep.varArgs().renderDeclaration(renderingContext);
      }

      @Override
      default String renderName(RenderingContext renderingContext)
      {
         return requestOrThrow(this, NAMEABLE_GET_NAME);
      }
   }

   /// This represents a java beans Property. Only a [Getter][Operations#PROPERTY_GET_GETTER] is mandatory
   ///
   /// [Getter][Operations#PROPERTY_GET_GETTER]
   /// - not static
   /// - the name starts with "get" and is longer then 3 or
   /// the name starts with "is" and has the return type [Boolean] or [Boolean#TYPE] and is longer then 2
   ///   - if a "is" and a "get" setter are present "is" is preferred
   ///
   /// [Setter][Operations#PROPERTY_GET_SETTER]
   /// - not static
   /// - the names has to match the getter name
   /// - the name starts with "set" and is longer then 3
   /// - has one parameter
   /// - the parameter has the same type as the Getter
   ///
   /// [Field][Operations#PROPERTY_GET_FIELD]
   /// - not static
   /// - has the same type as the Getter
   /// - if the name of the Getter without prefix is longer then 1 and has an uppercase Character
   /// at index 0 or 1 the field name has to match exactly
   /// or the name matches with the first Character converted to lowercase
   ///    - [Java Beans 8.8](https://www.oracle.com/java/technologies/javase/javabeans-spec.html)
   interface Property
         extends Nameable {}

   /// A Receiver is a way to annotate `this` in an inner class [Constructor][Constructor] or instance [Method][Method]
   ///
   /// @see Operations#EXECUTABLE_GET_RECEIVER
   /// @see Operations#RECEIVER_GET_TYPE
   /// @see Operations#ANNOTATIONABLE_GET_ANNOTATION_USAGES
   interface Receiver
         extends Annotationable,
                 ReceiverRenderable
   {

      @Override
      default String renderDeclaration(RenderingContext renderingContext)
      {
         return receiver().annotate(requestOrEmpty(this, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).orElse(emptyList()))
                          .renderDeclaration(renderingContext);
      }
   }

   interface Result
         extends Annotationable,
                 ResultRenderable
   {
      @Override
      default String renderDeclaration(RenderingContext renderingContext)
      {
         return result().annotate(requestOrEmpty(this, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).orElse(emptyList()))
                        .type(Provider.requestOrThrow(this, RESULT_GET_TYPE)).renderDeclaration(renderingContext);
      }
   }

   interface RecordComponent
         extends Nameable,
                 Annotationable,
                 ModuleEnclosed,
                 RecordComponentRenderable
   {
      @Override
      default String renderDeclaration(RenderingContext renderingContext)
      {
         return recordComponent().annotate(requestOrEmpty(this, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).orElse(emptyList()))
                                 .type(Provider.requestOrThrow(this, RECORD_COMPONENT_GET_TYPE))
                                 .name(requestOrThrow(this, NAMEABLE_GET_NAME))
                                 .renderDeclaration(renderingContext);
      }
   }

   interface Package
         extends Nameable,
                 QualifiedNameable,
                 Annotationable,
                 ModuleEnclosed,
                 Documented,
                 PackageInfoRenderable,
                 PackageRenderable
   {
      @Override
      default String renderPackageInfo(RenderingContext renderingContext)
      {
         return packageInfo().annotate(requestOrEmpty(this, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).orElse(emptyList()))
                             .name(requestOrThrow(this, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME))
                             .renderPackageInfo(renderingContext);
      }

      @Override
      default String renderQualifiedName(RenderingContext renderingContext)
      {
         return requestOrThrow(this, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME);
      }
   }

   interface Module
         extends Nameable,
                 QualifiedNameable,
                 Annotationable,
                 Documented,
                 ModuleInfoRenderable,
                 ModuleNameRenderable
   {
      @Override
      default String renderModuleInfo(RenderingContext renderingContext)
      {
         List<? extends Directive> directives = Provider.requestOrEmpty(this, MODULE_GET_DIRECTIVES).orElse(emptyList());

         return moduleInfo().annotate(requestOrEmpty(this, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).orElse(emptyList()))
                            .name(requestOrThrow(this, NAMEABLE_GET_NAME))
                            .requires(directives.stream().filter(Requires.class::isInstance).map(Requires.class::cast).toList())
                            .exports(directives.stream().filter(Exports.class::isInstance).map(Exports.class::cast).toList())
                            .opens(directives.stream().filter(Opens.class::isInstance).map(Opens.class::cast).toList())
                            .uses(directives.stream().filter(Uses.class::isInstance).map(Uses.class::cast).toList())
                            .provides(directives.stream().filter(Provides.class::isInstance).map(Provides.class::cast).toList())
                            .renderModuleInfo(renderingContext);
      }

      @Override
      default String renderQualifiedName(RenderingContext renderingContext)
      {
         return requestOrThrow(this, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME);
      }
   }

   /// Relation between modules
   interface Directive
         extends ImplementationDefined {}

   /// Exports a [Package][Operations#GET_PACKAGE] to
   /// a [List\<Module\>][Operations#EXPORTS_GET_TARGET_MODULES]
   /// or to [All][Operations#EXPORTS_TO_ALL]
   interface Exports
         extends Directive,
                 ExportsRenderable
   {
      @Override
      default String renderDeclaration(RenderingContext renderingContext)
      {
         return exports().package_(Provider.requestOrThrow(this, EXPORTS_GET_PACKAGE))
                         .to(Provider.requestOrEmpty(this, EXPORTS_GET_TARGET_MODULES).orElse(emptyList()))
                         .renderDeclaration(renderingContext);
      }
   }

   /// Allows reflection access to a [Package][Operations#OPENS_GET_PACKAGE]
   /// for a [List\<Module\>][Operations#OPENS_GET_TARGET_MODULES]
   ///  or to [All][Operations#OPENS_TO_ALL]
   interface Opens
         extends Directive,
                 OpensRenderable
   {
      @Override
      default String renderDeclaration(RenderingContext renderingContext)
      {
         return opens().package_(Provider.requestOrThrow(this, OPENS_GET_PACKAGE))
                       .to(Provider.requestOrEmpty(this, OPENS_GET_TARGET_MODULES).orElse(emptyList()))
                       .renderDeclaration(renderingContext);
      }
   }

   /// Provides a [Service][Operations#PROVIDES_GET_SERVICE]
   /// to other modules using [Implementations][Operations#PROVIDES_GET_IMPLEMENTATIONS]
   ///
   /// @see Uses
   interface Provides
         extends Directive,
                 ProvidesRenderable
   {
      @Override
      default String renderDeclaration(RenderingContext renderingContext)
      {
         return provides().service(Provider.requestOrThrow(this, PROVIDES_GET_SERVICE))
                          .with(Provider.requestOrEmpty(this, PROVIDES_GET_IMPLEMENTATIONS).orElse(emptyList()))
                          .renderDeclaration(renderingContext);
      }
   }

   /// Dependency on another Module
   interface Requires
         extends Directive,
                 RequiresRenderable
   {
      @Override
      default String renderDeclaration(RenderingContext renderingContext)
      {
         RequiresNameStep nameStep;
         if (Provider.requestOrEmpty(this, REQUIRES_IS_STATIC).orElse(false))
         {
            nameStep = requires().static_();
         }
         else if (Provider.requestOrEmpty(this, REQUIRES_IS_TRANSITIVE).orElse(false))
         {
            nameStep = requires().transitive();
         }
         else
         {
            nameStep = requires();
         }
         return nameStep.dependency(Provider.requestOrThrow(this, REQUIRES_GET_DEPENDENCY))
                        .renderDeclaration(renderingContext);
      }
   }

   /// Uses a Service of another module
   ///
   /// @see Provides
   interface Uses
         extends Directive,
                 UsesRenderable
   {
      @Override
      default String renderDeclaration(RenderingContext renderingContext)
      {
         return Dsl.uses(Provider.requestOrThrow(this, USES_GET_SERVICE)).renderDeclaration(renderingContext);
      }
   }

   interface ModuleEnclosed
         extends ImplementationDefined {}

   /// anything that can be annotated
   interface Annotationable
         extends ImplementationDefined {}

   interface Nameable
         extends ImplementationDefined {}

   interface Documented
         extends ImplementationDefined {}

   interface AnnotationValue
         extends ImplementationDefined,
                 AnnotationValueRenderable
   {

      @Override
      default String render(RenderingContext renderingContext)
      {
         Object object = Provider.requestOrThrow(this, ANNOTATION_VALUE_GET_VALUE);

         return (switch (object)
                 {
                    case Character c -> annotationValue(c);
                    case String s -> annotationValue(s);
                    case Boolean b -> annotationValue(b);
                    case Byte b -> annotationValue(b);
                    case Short s -> annotationValue(s);
                    case Integer i -> annotationValue(i);
                    case Long l -> annotationValue(l);
                    case Float f -> annotationValue(f);
                    case EnumConstant e -> annotationValue(e);
                    case Type t -> annotationValue(t);
                    case AnnotationUsage a -> annotationValue(a);
                    case List<?> l ->
                       //noinspection unchecked
                          annotationValue((List<AnnotationValueRenderable>) l);

                    default -> throw new IllegalStateException("Unexpected value: " + object);
                 }).render(renderingContext);
      }
   }

   interface Erasable {}

   /// {@link Annotation} represents the java file for the java concept of an annotation. This on the other hand represents
   /// a usage of such an annotation. like <br>
   /// {@code @Documented("testValue) public class Test{ }}
   interface AnnotationUsage
         extends ImplementationDefined,
                 AnnotationUsageRenderable
   {

      @Override
      default String renderDeclaration(RenderingContext renderingContext)
      {
         AnnotationUsageNameStep nameStep = annotationUsage().type(Provider.requestOrThrow(this, ANNOTATION_USAGE_GET_ANNOTATION));

         for (Map.Entry<? extends Method, ? extends AnnotationValue> entry : Provider.requestOrThrow(this, ANNOTATION_USAGE_GET_VALUES)
                                                                                     .entrySet())
         {
            nameStep = nameStep.name(requestOrThrow(((Method) entry), NAMEABLE_GET_NAME))
                               .value(entry.getValue());
         }
         return nameStep.renderDeclaration(renderingContext);
      }
   }

   interface QualifiedNameable
         extends ImplementationDefined {}

   interface AbstractModifiable
         extends Modifiable {}

   interface AccessModifiable
         extends Modifiable {}

   interface DefaultModifiable
         extends Modifiable {}

   interface FinalModifiable
         extends Modifiable {}

   interface Modifiable
         extends ImplementationDefined {}

   interface NativeModifiable
         extends Modifiable {}

   interface Sealable
         extends Modifiable {}

   interface StaticModifiable
         extends Modifiable {}

   interface StrictfpModifiable
         extends Modifiable {}
}
