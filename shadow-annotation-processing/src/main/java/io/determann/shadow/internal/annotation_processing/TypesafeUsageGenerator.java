package io.determann.shadow.internal.annotation_processing;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.dsl.Dsl;
import io.determann.shadow.api.annotation_processing.dsl.class_.ClassRenderable;
import io.determann.shadow.api.annotation_processing.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.annotation_processing.dsl.interface_.InterfaceGenericStep;
import io.determann.shadow.api.annotation_processing.dsl.method.MethodRenderable;
import org.jetbrains.annotations.NotNullByDefault;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.determann.shadow.api.annotation_processing.dsl.Dsl.class_;
import static io.determann.shadow.api.annotation_processing.dsl.Dsl.field;
import static io.determann.shadow.api.annotation_processing.dsl.RenderingContext.createRenderingContext;
import static java.lang.Character.toLowerCase;
import static java.util.stream.Stream.concat;

@NotNullByDefault
public class TypesafeUsageGenerator
      extends Ap.Processor
{
   private static final String GENERATE_TYPESAFE_USAGE_NAME = "io.determann.shadow.api.annotation_processing.generate.GenerateTypesafeUsage";
   private static final String GENERATE_TYPESAFE_USAGE_FOR_NAME = "io.determann.shadow.api.annotation_processing.generate.GenerateTypesafeUsageFor";

   @Override
   public void process(Ap.Context context)
   {
      for (Ap.Annotation annotated : getAnnotated(context))
      {
         context.writeAndCompileSourceFile(createTypeSafeUsage(annotated));
      }
   }

   private static Set<Ap.Annotation> getAnnotated(Ap.Context context)
   {
      Ap.Annotation genMetaModel = context.getAnnotationOrThrow(GENERATE_TYPESAFE_USAGE_NAME);
      Ap.Annotation
            genMetaModelFor
            = context.getAnnotationOrThrow(GENERATE_TYPESAFE_USAGE_FOR_NAME);

      //noinspection unchecked
      return concat(context.getAnnotationsAnnotatedWith(genMetaModel).stream(),
                    context.getAnnotationsAnnotatedWith(genMetaModelFor)
                           .stream()
                           .map(annotation -> annotation.getUsageOfOrThrow(genMetaModelFor))
                           .map(annotationUsage -> annotationUsage.getValueOrThrow("value"))
                           .map(annotationValue -> ((Ap.AnnotationValue.Values<Ap.AnnotationValue.TypeValue>) annotationValue))
                           .map(Ap.AnnotationValue.Values::getValue)
                           .flatMap(Collection::stream)
                           .map(Ap.AnnotationValue.TypeValue::getValue)
                           .filter(Ap.Annotation.class::isInstance)
                           .map(Ap.Annotation.class::cast))
            .collect(Collectors.toSet());
   }

   public static ClassRenderable createTypeSafeUsage(Ap.Annotation annotation)
   {
      String fieldName = getFieldName(annotation);

      return class_().package_(annotation.getPackage())
                     .import_(Dsl.import_("java.util.Objects"))
                     .import_(Dsl.import_("java.util.Map"))
                     .import_(Dsl.import_("io.determann.shadow.api.query.Implementation"))
                     .import_(Dsl.import_().static_().method("io.determann.shadow.api.query.Provider", "requestOrThrow"))
                     .import_(Dsl.import_().static_().declared("io.determann.shadow.api.query.Operations"))
                     .annotate(Dsl.generated(TypesafeUsageGenerator.class.getName()))
                     .public_().name(annotation.getName() + "TypesafeUsage")
                     .implements_("io.determann.shadow.api.annotation_processing.Ap.AnnotationUsage")
                     .field(field().private_()
                                   .static_()
                                   .final_()
                                   .type("java.lang.String")
                                   .name("QUALIFIED_ANNOTATION_NAME")
                                   .initializer("\"" + annotation.getQualifiedName() + "\""))
                     .field(field().private_().final_().type("io.determann.shadow.api.C.AnnotationUsage").name(fieldName))
                     .constructor(Dsl.constructor()
                                     .public_()
                                     .surroundingType()
                                     .parameter(Dsl.parameter("io.determann.shadow.api.C.AnnotationUsage", "usage"))
                                     .body("""
                                           Objects.requireNonNull(usage);
                                           C.Annotation annotation = requestOrThrow(usage, ANNOTATION_USAGE_GET_ANNOTATION);
                                           String qualifiedName = requestOrThrow(annotation, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME);
                                           
                                           if (!QUALIFIED_ANNOTATION_NAME.equals(qualifiedName)) {
                                           
                                              throw new IllegalArgumentException("This Meta-Model represents a usage of \\"" +
                                                                                 QUALIFIED_ANNOTATION_NAME + "\\" not \\"" + qualifiedName + "\\"");
                                           }
                                           this.%s = usage;
                                           """.formatted(fieldName)))
                     .method(createAccessors(annotation, fieldName))
                     .method("""
                             @Override
                             public Map<Ap.Method, Ap.AnnotationValue> getValues() {
                                return (Map<Ap.Method, Ap.AnnotationValue>) requestOrThrow(%s, ANNOTATION_USAGE_GET_VALUES);
                             }""".formatted(fieldName))
                     .method("""
                             @Override
                             public Ap.Annotation getAnnotation() {
                                return (Ap.Annotation) requestOrThrow(%s, ANNOTATION_USAGE_GET_ANNOTATION);
                             }""".formatted(fieldName))
                     .method("""
                             @Override
                             public Implementation getImplementation() {
                                return %s.getImplementation();
                             }""".formatted(fieldName));
   }

   private static String getFieldName(Ap.Annotation annotation)
   {
      String typeName = annotation.getName();
      return toLowerCase(typeName.charAt(0)) + typeName.substring(1);
   }

   private static List<MethodRenderable> createAccessors(Ap.Annotation annotation, String fieldName)
   {
      return annotation.getMethods().stream().map(method ->
                                                  {
                                                     Ap.Type returnType = method.getReturnType();

                                                     DeclaredRenderable accessorType = createAccessorType(returnType);

                                                     return Dsl.method().public_().resultType(accessorType)
                                                               .name(method.getName())
                                                               .body("return (%s) requestOrThrow(%s, ANNOTATION_USAGE_GET_VALUE, \"%s\");"
                                                                           .formatted(accessorType.renderSimpleName(createRenderingContext()),
                                                                                      fieldName,
                                                                                      method.getName()));
                                                  }).toList();
   }

   private static DeclaredRenderable createAccessorType(Ap.Type returnType)
   {
      InterfaceGenericStep genericStep =
            Dsl.interface_().package_("io.determann.shadow.api.annotation_processing")
               .name(switch (returnType)
                     {
                        case Ap.Class aClass when "java.lang.String".equals(aClass.getQualifiedName()) -> "Ap.AnnotationValue.StringValue";
                        case Ap.Class aClass when "java.lang.Class".equals(aClass.getQualifiedName()) -> "Ap.AnnotationValue.TypeValue";
                        case Ap.boolean_ aBoolean -> "Ap.AnnotationValue.BooleanValue";
                        case Ap.byte_ aByte -> "Ap.AnnotationValue.ByteValue";
                        case Ap.short_ aShort -> "Ap.AnnotationValue.ShortValue";
                        case Ap.int_ anInt -> "Ap.AnnotationValue.IntegerValue";
                        case Ap.long_ aLong -> "Ap.AnnotationValue.LongValue";
                        case Ap.char_ aChar -> "Ap.AnnotationValue.CharacterValue";
                        case Ap.float_ aFloat -> "Ap.AnnotationValue.FloatValue";
                        case Ap.double_ aDouble -> "Ap.AnnotationValue.DoubleValue";
                        case Ap.Enum anEnum -> "Ap.AnnotationValue.EnumValue";
                        case Ap.Annotation annotation -> "Ap.AnnotationValue.AnnotationUsageValue";
                        case Ap.Array array -> "Ap.AnnotationValue.Values";
                        default -> throw new IllegalStateException();
                     });
      if (!(returnType instanceof Ap.Array array))
      {
         return genericStep;
      }
      return genericStep.genericUsage(createAccessorType(array.getComponentType()));
   }
}